package com.wira.pmgt.shared.model.program;

import java.util.Date;
import java.util.List;

import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.Listable;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.UserGroup;

public abstract class IsProgramDetail extends ProgramSummary implements Listable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private ProgramDetailType type;
	private Double budgetAmount; //Total budget amount (accumulation of source of funds)
	private Double actualAmount; //Actual amount spent
	private Date startDate; //For Activities & tasks - Start Date (Programs run for a whole year)
	private Date endDate; //For Activities & tasks - End Date
	private List<ProgramFundDTO> funding;
	private List<TargetAndOutcomeDTO> targetsAndOutcomes;
	private PeriodDTO period;
	private Long parentId; //Parent Activity or Task
	private List<IsProgramDetail> children;
	private List<IsProgramDetail> objectives; //Objectives of an outcome
	private List<ProgramSummary> programSummary; //Used to build breadcrumb 
	private List<HTUser> assignedUsers;
	private List<UserGroup> assignedGroups;
	private Long documentId;
	private ProgramStatus status;
	
	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public IsProgramDetail() {
	}

	public ProgramDetailType getType() {
		return type;
	}

	public void setType(ProgramDetailType type) {
		this.type = type;
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

	public List<TargetAndOutcomeDTO> getTargetsAndOutcomes() {
		return targetsAndOutcomes;
	}

	public void setTargetsAndOutcomes(List<TargetAndOutcomeDTO> targetsAndOutcomes) {
		this.targetsAndOutcomes = targetsAndOutcomes;
	}

	public List<ProgramFundDTO> getFunding() {
		return funding;
	}

	public void setFunding(List<ProgramFundDTO> funding) {
		this.funding = funding;
	}

	public PeriodDTO getPeriod() {
		return period;
	}

	public void setPeriod(PeriodDTO period) {
		this.period = period;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<IsProgramDetail> getChildren() {
		return children;
	}

	public void setChildren(List<IsProgramDetail> children) {
		this.children = children;
	}

	public List<ProgramSummary> getProgramSummary() {
		return programSummary;
	}

	public void setProgramSummary(List<ProgramSummary> programSummary) {
		this.programSummary = programSummary;
	}

	public List<IsProgramDetail> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<IsProgramDetail> objectives) {
		this.objectives = objectives;
	}
	
	@Override
	public String getDisplayName() {
		
		return getName();
	}

	public List<UserGroup> getAssignedGroups() {
		return assignedGroups;
	}

	public void setAssignedGroups(List<UserGroup> assignedGroups) {
		this.assignedGroups = assignedGroups;
	}

	public List<HTUser> getAssignedUsers() {
		return assignedUsers;
	}

	public void setAssignedUsers(List<HTUser> assignedUsers) {
		this.assignedUsers = assignedUsers;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ProgramStatus getStatus() {
		return status;
	}

	public void setStatus(ProgramStatus status) {
		this.status = status;
	}

}
