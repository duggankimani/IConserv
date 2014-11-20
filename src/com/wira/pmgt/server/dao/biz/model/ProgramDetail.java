package com.wira.pmgt.server.dao.biz.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Transient;

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
					"and p.isActive=:isActive and "
					+ " p.type=:type order by p.name"), 
	
	@NamedQuery(name="ProgramDetail.findByTypeAndPeriod", 
	query="SELECT distinct(p) FROM ProgramDetail p left join p.programAccess access " +
			"where (true=:isCurrentUserAdmin or (access.userId=:userId or access.groupId in (:groupIds))) " +
			"and p.isActive=:isActive and p.type=:type and p.period=:period order by p.name"), 
			
	@NamedQuery(name="ProgramDetail.findActivitiesByProgramAndOutcome", 
	query="SELECT p FROM ProgramDetail p " +
			"where p.parent.id=:parentId and p.activityOutcome.id=:activityOutcomeId " +
			"and p.isActive=:isActive order by p.name"), 

	@NamedQuery(name="ProgramDetail.findAll", query="SELECT distinct(p) FROM ProgramDetail p left join p.programAccess access " +
			"where (true=:isCurrentUserAdmin or (access.userId=:userId or access.groupId in (:groupIds))) " +
			"and p.isActive=:isActive and p.period=:period " +
			"order by p.name"),		
	@NamedQuery(name="ProgramDetail.findAllIds", query="SELECT distinct(p.id) FROM ProgramDetail p " +
			"left join p.programAccess access " +
			"where p.type=:type " +
			"and (true=:isCurrentUserAdmin or (access.userId=:userId or access.groupId in (:groupIds))) " +
			"and p.isActive=:isActive and p.period=:period "),
	@NamedQuery(name="ProgramDetail.findById", query="SELECT p FROM ProgramDetail p where p.id=:id"),
	@NamedQuery(name="ProgramDetail.findByCodeAndPeriod", query="FROM ProgramDetail p where p.code=:code and p.period=:period")
})

@NamedNativeQueries({
	/**
	 * This query loads the children of children starting from Outcome>Activity>Task>Task etc 
	 * 
	 * @author duggan
	 *
	 */	
	@NamedNativeQuery(name="ProgramDetail.getCalendar",
			resultSetMapping="ProgramDetail.calendarMappings",
			query="with recursive programdetail_tree as ( "+
			"select id as programId,id, parentid,type,startdate,enddate,status,name,description, array[id] as path_info "+
			"from ProgramDetail where id in (:parentIds) and type!='OBJECTIVE' and isActive=1 " +
			"and (status is null or status!=:statusClosed) "+
			"union all "+
			"select path_info[1] as programId,c.id,c.parentid,c.type,c.startdate,c.enddate,c.status,c.name,c.description, p.path_info||c.id "+
			"from ProgramDetail c join programdetail_tree p on c.parentid=p.id " +
			"where (c.status is null or c.status!=:statusClosed) " +
			") "+
			"select " +
			//"cast(path_info as varchar(30)) path," +
			"programId,id,parentid,type,startdate,enddate,status,name,description " +
			"from programdetail_tree " +
			"where (startDate is not null and endDate is not null) " +
			"and (" +
			"((status is null or status=:statusCreated) and startDate<(:currentDate)) " +
			"or ((status is null or status!=:statusClosed) and endDate<:currentDate)" +
			") " +
			"order by path_info "),
			
	@NamedNativeQuery(name="ProgramDetail.getTaskForms",
		resultSetMapping="ProgramDetail.taskFormsMapping",
		query=("select id,caption from adform where name in(:formNames)")),

	@NamedNativeQuery(name="ProgramDetail.getAnalysisData",
		resultSetMapping="ProgramDetail.analysisDataMapping",
		query=("select id,name,description,budgetamount,actualamount,totalallocation,commitedamount" +
				" from programdetail where type='PROGRAM' and periodid=:periodid order by name")),
	
	@NamedNativeQuery(name="ProgramDetail.getBudgetAnalysis",
			resultSetMapping="ProgramDetail.performanceAnalysis",
			query=("select * from func_calculateperformance()")),
	
	@NamedNativeQuery(name="ProgramDetail.getPerfomanceByTimelines",
			resultSetMapping="ProgramDetail.performanceAnalysis",
			query=("select * from func_getPerformanceByTimelines()")),
	
	@NamedNativeQuery(name="ProgramDetail.getPerformanceByKPIs",
		resultSetMapping="ProgramDetail.performanceAnalysis",
		query=("select * from func_getPerformanceByKPIs()")),
	
	@NamedNativeQuery(name="ProgramDetail.getProgramTree",
		resultSetMapping="ProgramDetail.programTreeMapping",
		query=("with recursive programdetail_tree as ( "+
			"select m.id as programId,m.id,m.name, m.parentid,m.type,status,m.outcomeid,(select i.name from programdetail i where i.id=m.outcomeid) outcomename, 1 as level, array[id] as path_info "+ 
			"from programdetail m where m.id in (select id from programdetail where type='PROGRAM' and periodid=:periodId and id in (:programIds))  "+
			"union all "+
			"select path_info[1] as programId,c.id,c.name,c.parentid,c.type,c.status,c.outcomeid,(select i.name from programdetail i where i.id=c.outcomeid) outcomename, p.level+1, p.path_info||c.id "+ 
			"from programdetail c join programdetail_tree p on c.parentid=p.id) "+
			"select programId,id,name,parentid,type,status,outcomeid,outcomename from programdetail_tree order by programid,outcomeid"))

})

@SqlResultSetMappings({
	@SqlResultSetMapping(name="ProgramDetail.calendarMappings",
		columns={
		//@ColumnResult(name="path"),
		@ColumnResult(name="programId"),
		@ColumnResult(name="id"),
		@ColumnResult(name="parentid"),
		@ColumnResult(name="type"),
		@ColumnResult(name="startdate"),
		@ColumnResult(name="enddate"),
		@ColumnResult(name="status"),
		@ColumnResult(name="name"),
		@ColumnResult(name="description")
		}
	),
	@SqlResultSetMapping(name="ProgramDetail.taskFormsMapping",
		columns={
		@ColumnResult(name="id"),
		@ColumnResult(name="caption")
		}
	),
	@SqlResultSetMapping(name="ProgramDetail.analysisDataMapping",
	columns={
	@ColumnResult(name="id"),
	@ColumnResult(name="name"),
	@ColumnResult(name="description"),
	@ColumnResult(name="budgetAmount"),
	@ColumnResult(name="actualAmount"),
	@ColumnResult(name="commitedAmount"),
	@ColumnResult(name="totalAllocation")
	}),
	
	@SqlResultSetMapping(name="ProgramDetail.performanceAnalysis",
			columns={
			@ColumnResult(name="name"),
			@ColumnResult(name="description"),
			@ColumnResult(name="id"),
			@ColumnResult(name="countsuccess"),
			@ColumnResult(name="countfail"),
			@ColumnResult(name="countnodata"),
			@ColumnResult(name="percsuccess"),
			@ColumnResult(name="percfail"),
			@ColumnResult(name="avegpercsuccess")
	}),
	
	@SqlResultSetMapping(name="ProgramDetail.programTreeMapping",
		columns={
		@ColumnResult(name="programId"),
		@ColumnResult(name="id"),
		@ColumnResult(name="parentid"),
		@ColumnResult(name="type"),
		@ColumnResult(name="status"),
		@ColumnResult(name="outcomeid"),
		@ColumnResult(name="name"),
		@ColumnResult(name="outcomename")
	})
})	
public class ProgramDetail 	extends ProgramBasicDetail{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private ProgramDetailType type;
	
	private String code;//Autogenerated item code for future matching
	
	@OneToMany(mappedBy="programDetail")
	@Cascade(value={
			org.hibernate.annotations.CascadeType.DELETE,
			org.hibernate.annotations.CascadeType.REMOVE
			})
	private Set<TargetAndOutcome> targets = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name="periodid", referencedColumnName="id", nullable=true)
	private Period period;	//Calendar year within which this 
	
	//we manage this manually
	@OneToMany(mappedBy="programDetail")
	@Cascade(value={
			org.hibernate.annotations.CascadeType.DELETE,
			org.hibernate.annotations.CascadeType.REMOVE
			})
	private Set<ProgramFund> sourceOfFunds = new HashSet<>();	

	
	private Double budgetAmount=0.0; //Total budget amount (accumulation of source of funds)
	private Double actualAmount=0.0; //Actual amount spent
	private Double commitedAmount=0.0;
	private Double totalAllocation=0.0;
	
	private Date startDate; //For Activities & tasks - Start Date (Programs run for a whole year)
	private Date endDate; //For Activities & tasks - End Date
	
	//An activity may have other sub-activities
	@ManyToOne
	@JoinColumn(name="parentid", referencedColumnName="id", nullable=true)
	private ProgramDetail parent;
	
	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL)
	private Set<ProgramDetail> children = new HashSet<>();
	
	
	
	/**
	 * A Collection of outcomes for each program
	 * 
	 */
	@ManyToMany
	@JoinTable(name="programoutcome",
			joinColumns=@JoinColumn(name="programid"),
			inverseJoinColumns=@JoinColumn(name="outcomeId")
			)
	private Set<ProgramDetail> programOutcomes = new HashSet<>();
	
	/**
	 * A collection of programs under a given outcome (Reverse reference of programOutcomes)
	 * Given an outcome, this is the set of programs that have this outcome.
	 */
	@ManyToMany(mappedBy="programOutcomes")
	private Set<ProgramDetail> outcomePrograms = new HashSet<>();
	
	
	
	/**
	 * An activity in a program is linked to one programOutcome, One outcome to many activities
	 */
	@OneToMany
	private Set<ProgramDetail> outcomeActivities = new HashSet<>();	
	
	/**
	 * Many activities to one outcome (Reverse Reference)
	 */
	@ManyToOne
	@JoinColumn(name="outcomeid", referencedColumnName="id", nullable=true)
	private ProgramDetail activityOutcome;
	
	@Column(length=20)
	private String budgetLine;
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
	
	private Double progress=0.0;
	
	public void setProgress(Double progress) {
		this.progress = progress;
	}

	private Double rating;
	
	@Transient
	private Long programId;
	
	private Date dateCompleted;
	
	public ProgramDetail() {
	}
	
	//programId,id,parentid,type,startdate,enddate,status
	public ProgramDetail(Long id, Long programId, Long parentId, String type, Date startDate,
			Date endDate, String status){
		
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

//	public void setActualAmount(double actualAmount) {
//		this.actualAmount = actualAmount;
//	}

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

//	public void setSourceOfFunds(Collection<ProgramFund> sourceOfFundz) {
//				
//		this.sourceOfFunds.clear();
//		
//		for(ProgramFund fund: sourceOfFundz){
//			fund.setProgramDetail(this);
//			this.sourceOfFunds.add(fund);
//		}
//	}

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

	public Set<ProgramDetail> getProgramOutcomes() {
		return programOutcomes;
	}

	public void setProgramOutcomes(Collection<ProgramDetail> outcomesForProgram) {
		programOutcomes.clear();
		outcomePrograms.remove(this);
		
		if(outcomesForProgram!=null){
			for(ProgramDetail detail: outcomesForProgram){
				programOutcomes.add(detail);
				outcomePrograms.add(this);
			}
		}
	}

	public void setActivityOutcome(ProgramDetail outcome){
		this.activityOutcome = outcome;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof ProgramDetail))
			return false;
		
		ProgramDetail other = (ProgramDetail)obj;
		
//		if(getId()!=null && other.getId()!=null){
//			return getId().equals(other.getId());
//		}else
			
		if(type!=null && period!=null && getName()!=null){
			return type==other.type && period.equals(other.period) && getName().equals(other.getName());
		}else if(type!=null && getName()!=null){
			return type==other.type && getName().equals(other.getName());
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

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Double getProgress() {
		return progress;
	}
	
	/**
	 * Update Task Progress based on status change
	 * for leaf elements (Tasks with no children)
	 * 
	 * This update will update all the other parent elements 
	 * using the trigger procedure: proc_updatestatus
	 */
	@PreUpdate
	@PrePersist
	public void calculateProgress(){
		//Synchronize task status with progrss
		if(status!=null 
				&& type==ProgramDetailType.TASK
				&& this.getChildren().isEmpty()){
			//progress is either computed from status or the average completion of children items
			progress= new Double(status.getPercCompletion());
		}
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Double getTotalAllocation() {
		return totalAllocation;
	}

	public Double getCommitedAmount() {
		return commitedAmount;
	}

	public ProgramDetail getActivityOutcome() {
		return activityOutcome;
	}

	public String getBudgetLine() {
		return budgetLine;
	}

	public void setBudgetLine(String budgetLine) {
		this.budgetLine = budgetLine;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}
	
}
