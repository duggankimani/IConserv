package com.wira.pmgt.server.dao.biz.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;

import org.hibernate.annotations.Cascade;

import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.ProgramStatus;

/**
 * Setup Information
 * <p>
 * All Projects/ Outcomes/
 * @author duggan
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="ProgramDetail.findByType", 
			query="SELECT distinct(p) FROM ProgramDetail p left join p.programAccess access " +
					"where (true=:isCurrentUserAdmin or (access.userId=:userId or access.groupId in (:groupIds))) " +
					"and p.isActive=:isActive and p.type=:type and p.period=:period order by p.name"), 
	
	@NamedQuery(name="ProgramDetail.findAll", query="SELECT distinct(p) FROM ProgramDetail p left join p.programAccess access " +
			"where (true=:isCurrentUserAdmin or (access.userId=:userId or access.groupId in (:groupIds))) " +
			"and p.isActive=:isActive and p.period=:period " +
			"order by p.name"),		
	@NamedQuery(name="ProgramDetail.findAllIds", query="SELECT distinct(p.id) FROM ProgramDetail p left join p.programAccess access " +
			"where (true=:isCurrentUserAdmin or (access.userId=:userId or access.groupId in (:groupIds))) " +
			"and p.isActive=:isActive and p.period=:period " +
			"order by p.name"),
	@NamedQuery(name="ProgramDetail.findById", query="SELECT p FROM ProgramDetail p where p.id=:id"),
	@NamedQuery(name="ProgramDetail.findByCodeAndPeriod", query="FROM ProgramDetail p where p.code=:code and p.period=:period")
})

@NamedNativeQueries({
	//TODO: LOOK FOR A LIGHT WEIGHT MECHANISM THAT ALLOWS A SUBSET OF FIELDS TO BE SELECTED
	/**
	 * This query loads the children of children starting from Outcome>Activity>Task>Task etc 
	 * 
	 * @author duggan
	 *
	 */
	@NamedNativeQuery(name="ProgramDetail.getCalendar",
			resultClass=ProgramDetail.class,
			query="with recursive programdetail_tree as ( "+
			"select *,array[id] as path_info "+
			"from ProgramDetail where parentid in (:parentIds) and type!='OBJECTIVE' and isActive=1 "+
			"union all "+
			"select c.*, p.path_info||c.id "+
			"from ProgramDetail c join programdetail_tree p on c.parentid=p.id " +
			"where (c.startDate is not null and c.endDate is not null) and " +
				"(((c.status is null or c.status=:statusCreated) and c.startDate<(:currentDate)) " +
			"or (c.status is not null and c.status!=:statusClosed and c.endDate<:currentDate)) " +
			") "+
			"select * " +
			"from programdetail_tree " +
			"where startdate is not null and enddate is not null and status!='CLOSED'" +
			"order by path_info ")
})

@SqlResultSetMapping(name="ProgramDetail.calendarMappings",
	entities=@EntityResult(entityClass=ProgramDetail.class, fields={
		@FieldResult(name="id",column="id"),
		@FieldResult(name="name",column="name"),
		@FieldResult(name="description",column="description"),
		@FieldResult(name="type",column="type"),
		@FieldResult(name="parentid",column="programParentId"),
		@FieldResult(name="startDate",column="startDate"),
		@FieldResult(name="endDate",column="endDate")
	}
	))
	
public class ProgramDetail 	extends ProgramBasicDetail{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private ProgramDetailType type;
	
	private String code;//Autogenerated item code for future matching
	
	private String target;//target
	private String indicator;//indicator
	private String actual;//outputs/deliverable
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="programDetail")
	private Set<TargetAndOutcome> targets = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name="periodid", referencedColumnName="id", nullable=true)
	private Period period;	//Calendar year within which this 
	
	@OneToMany(mappedBy="programDetail")
	@Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,
			org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.PERSIST,
			org.hibernate.annotations.CascadeType.MERGE,
			org.hibernate.annotations.CascadeType.DELETE,
			org.hibernate.annotations.CascadeType.EVICT,
			org.hibernate.annotations.CascadeType.REMOVE,
			org.hibernate.annotations.CascadeType.REFRESH
			})
	private Set<ProgramFund> sourceOfFunds = new HashSet<>();	

	private Double budgetAmount=0.0; //Total budget amount (accumulation of source of funds)
	private Double actualAmount=0.0; //Actual amount spent
	
	private Date startDate; //For Activities & tasks - Start Date (Programs run for a whole year)
	private Date endDate; //For Activities & tasks - End Date
	
	//An activity may have other sub-activities
	@ManyToOne
	@JoinColumn(name="parentid", referencedColumnName="id", nullable=true)
	private ProgramDetail parent;
	
	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL)
	private Set<ProgramDetail> children = new HashSet<>();
	
	/**
	 * Objectives and Outcomes/Activities have a many to Many Relationship
	 * 
	 * Also Objectives belong to a single Program
	 */
	@ManyToMany
	@JoinTable(name="programobjective",
			joinColumns=@JoinColumn(name="programdetailid"),
			inverseJoinColumns=@JoinColumn(name="objectiveId")
			)
	private Set<ProgramDetail> objectives = new HashSet<>();
	
	@ManyToMany(mappedBy="objectives")
	private Set<ProgramDetail> outcomes = new HashSet<>();
	
	private Long processInstanceId;
	
	@OneToMany(mappedBy="programDetail",fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,
			org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.PERSIST,
			org.hibernate.annotations.CascadeType.MERGE,
			org.hibernate.annotations.CascadeType.DELETE,
			org.hibernate.annotations.CascadeType.EVICT,
			org.hibernate.annotations.CascadeType.REMOVE,
			org.hibernate.annotations.CascadeType.REFRESH
			})
	private Set<ProgramAccess> programAccess = new HashSet<>();
	
	@Enumerated(EnumType.STRING)
	private ProgramStatus status = ProgramStatus.CREATED;
	
	public ProgramDetail() {
	}

	public ProgramDetailType getType() {
		return type;
	}

	public void setType(ProgramDetailType type) {
		this.type = type;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Double getBudgetAmount() {
		return budgetAmount;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public Set<TargetAndOutcome> getTargets() {
		return targets;
	}

	public void setTargets(Collection<TargetAndOutcome> targetsAndOutcomes) {
		this.targets.clear();
		for(TargetAndOutcome target: targetsAndOutcomes){
			target.setProgramDetail(this);
			this.targets.add(target);
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Set<ProgramFund> getSourceOfFunds() {
		return sourceOfFunds;
	}

	public void setSourceOfFunds(Set<ProgramFund> sourceOfFundz) {
		Set<ProgramFund> canBeRmoved = new HashSet<>();
		for(ProgramFund fund: sourceOfFunds){
			//delete causes referencial integrity errors - Foreign key issues
			//So we set their budgets to zero instead; but remove those we can
			if(fund.getAllocation()==null){
				canBeRmoved.add(fund);
			}else if(!sourceOfFundz.contains(fund)){
				fund.setAmount(0.0);
			}
		}		
		this.sourceOfFunds.removeAll(canBeRmoved);
		
		//Put everything in a list to allow retrieval
		List<ProgramFund> previousValues = new ArrayList<>();
		previousValues.addAll(this.sourceOfFunds);
		
		
		for(ProgramFund fund: sourceOfFundz){
			if(previousValues.contains(fund)){
				
				//We've found a fund that already exists in the previously persisted set
				//This may happen is the same Fund is budgeted for twice/ or the programfund in 
				//question cannot be deleted due to references by child funds - allocations
				ProgramFund previous = previousValues.get(previousValues.indexOf(fund));
				assert this.sourceOfFunds.remove(previous);
				
				//Double previousAmount =previous.getAmount()==null? 0.0: previous.getAmount();
				Double amount = fund.getAmount()==null? 0.0: fund.getAmount();				
				
				previous.setAmount(amount);
				System.err.println(previous.getFund().getName()+
						" :: Contained=true;calcuated new amounts >>>> "+previous.getAmount());
				
				previous.setProgramDetail(this);
				assert this.sourceOfFunds.add(previous);
			}else{
				System.err.println(fund.getFund().getName()+" :: Direct set >>>> "+fund.getAmount());
				//add new values
				fund.setProgramDetail(this);
				this.sourceOfFunds.add(fund);
			}
			//replace if exists
			
			
		}
	}

	public ProgramDetail getParent() {
		return parent;
	}

	public void setParent(ProgramDetail parent) {
		this.parent = parent;
	}

	public Set<ProgramDetail> getChildren() {
		return children;
	}

	public void setChildren(Set<ProgramDetail> children) {
		this.children.clear();
		for(ProgramDetail child: children){
			this.children.add(child);
			child.setParent(this);
		}
	}

	public Set<ProgramDetail> getObjectives() {
		return objectives;
	}

	public void setObjectives(Set<ProgramDetail> objs) {
		objectives.clear();
		outcomes.remove(this);
		
		if(objs!=null){
			for(ProgramDetail detail: objs){
				objectives.add(detail);
				outcomes.add(this);
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof ProgramDetail))
			return false;
		
		ProgramDetail other = (ProgramDetail)obj;
		
		if(getId()!=null && other.getId()!=null){
			return getId().equals(other.getId());
		}else if(type!=null && period!=null && getName()!=null){
			return type==other.type && period.equals(other.period) && getName().equals(other.getName());
		}
		
		return false;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAccess(Collection<ProgramAccess> permissions) {
		this.programAccess.clear();
		
		if(permissions!=null)
		for(ProgramAccess access: permissions){
			//this has to be done first
			this.programAccess.add(access);
			access.setProgramDetail(this);
		}
	}
	
	public Collection<ProgramAccess> getProgramAccess(){
		return programAccess;
	}

	public ProgramStatus getStatus() {
		return status;
	}

	public void setStatus(ProgramStatus status) {
		this.status = status;
	}

}
