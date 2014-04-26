package com.wira.pmgt.server.dao.biz.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

import com.wira.pmgt.shared.model.ProgramDetailType;

/**
 * Setup Information
 * <p>
 * All Projects/ Outcomes/
 * @author duggan
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="ProgramDetail.findByType", query="FROM ProgramDetail p where p.isActive=:isActive and p.type=:type and p.period=:period order by id"), 
	@NamedQuery(name="ProgramDetail.findAll", query="FROM ProgramDetail p where p.isActive=:isActive order by id"),
	@NamedQuery(name="ProgramDetail.findById", query="FROM ProgramDetail p where p.id=:id")
})
public class ProgramDetail 	extends ProgramBasicDetail{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private ProgramDetailType type;
	
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
			org.hibernate.annotations.CascadeType.REMOVE
			})
	private Set<ProgramFund> sourceOfFunds = new HashSet<>();	

	private Double budgetAmount; //Total budget amount (accumulation of source of funds)
	private Double actualAmount; //Actual amount spent
	
	private Date startDate; //For Activities & tasks - Start Date (Programs run for a whole year)
	private Date endDate; //For Activities & tasks - End Date
	
	//An activity may have other sub-activities
	@ManyToOne
	@JoinColumn(name="parentid", referencedColumnName="id", nullable=true)
	private ProgramDetail parent;
	
	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL)
	private Set<ProgramDetail> children = new HashSet<>();
	
	/**
	 * Objectives and Outcomes have a many to Many Relationship
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

	public void setBudgetAmount(Double budgetAmount) {
		this.budgetAmount = budgetAmount;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
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

	public void setTargets(Set<TargetAndOutcome> targets) {
		this.targets = targets;
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
//		for(ProgramFund fund:this.sourceOfFunds){
//			if(!sourceOfFundz.contains(fund)){
//				fund.setProgramDetail(null);
//				fund.setFund(null);
//				//create an opha
//			}
//		}
		sourceOfFunds.clear();//clear the set
		
		for(ProgramFund fund: sourceOfFundz){
			fund.setProgramDetail(this);
			this.sourceOfFunds.add(fund);
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

}
