package com.wira.pmgt.server.dao.biz.model;

import java.lang.String;
import java.lang.Long;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.wira.pmgt.server.dao.model.PO;
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
public class ProgramDetail extends PO {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private static final long serialVersionUID = 6975295488889785294L;
	
	@Column(unique=true, nullable=false)
	private String name;
	
	private String description;
	
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
	
	@OneToMany(mappedBy="programDetail", cascade=CascadeType.ALL)
	private Set<ProgramFund> sourceOfFunds = new HashSet<>();	

	private Double budgetAmount; //Total budget amount (accumulation of source of funds)
	private Double actualAmount; //Actual amount spent
	
	private Date startDate; //For Activities & tasks - Start Date (Programs run for a whole year)
	private Date endDate; //For Activities & tasks - End Date
	
	@ManyToOne
	@JoinColumn(name="parentid", referencedColumnName="id", nullable=true)
	private ProgramDetail parent;
	
	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL)
	private Set<ProgramDetail> children = new HashSet<>();
	
	public ProgramDetail() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
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

	public void setSourceOfFunds(Set<ProgramFund> sourceOfFunds) {
		this.sourceOfFunds.clear();
		for(ProgramFund fund: sourceOfFunds){
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
		this.children.clear();
		for(ProgramDetail child: children){
			this.children.add(child);
			child.setParent(this);
		}
	}

}
