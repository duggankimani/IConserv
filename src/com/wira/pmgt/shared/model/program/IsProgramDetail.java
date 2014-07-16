package com.wira.pmgt.shared.model.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.Listable;
import com.wira.pmgt.shared.model.UserGroup;

public abstract class IsProgramDetail extends ProgramSummary implements Listable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private Double budgetAmount; //Total budget amount (accumulation of source of funds)
	private Double actualAmount; //Actual amount spent
	private List<ProgramFundDTO> funding;
	private List<TargetAndOutcomeDTO> targetsAndOutcomes;
	private PeriodDTO period;
	private List<IsProgramDetail> children;
	private List<ProgramSummary> programSummary; //Used to build breadcrumb 
	private List<IsProgramDetail> programOutcomes;
	private List<HTUser> assignedUsers;
	private List<UserGroup> assignedGroups;
	private Long documentId;
	private Double progress = 0.0; 
	private Double rating;
	private Long activityOutcomeId;
	
	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public IsProgramDetail() {
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

	public Double getProgress() {
		return progress;
	}

	public void setProgress(Double progress) {
		this.progress = progress;
	}
	
	public static void sort(List<IsProgramDetail> programs) {
		Collections.sort(programs, new Comparator<IsProgramDetail>(){
			@Override
			public int compare(IsProgramDetail o1, IsProgramDetail o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	public void sort() {
		if(children!=null){
			sort(children);
		}
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof IsProgramDetail) || obj==null){
			return false;
		}
		IsProgramDetail other = (IsProgramDetail)obj;
		
		return getId()==other.getId();
	}

	public Long getActivityOutcomeId() {
		return activityOutcomeId;
	}

	public void setActivityOutcomeId(Long activityOutcomeId) {
		this.activityOutcomeId = activityOutcomeId;
	}

	public List<IsProgramDetail> getProgramOutcomes() {
		return programOutcomes;
	}

	public void setProgramOutcomes(List<IsProgramDetail> programOutcomes) {
		this.programOutcomes = programOutcomes;
	}
	

	public void addProgramOutcomes(IsProgramDetail programDTO) {
		if(programOutcomes==null){
			programOutcomes = new ArrayList<IsProgramDetail>();
		}
		
		programOutcomes.add(programDTO);
	}

	public void addChild(IsProgramDetail activity) {
		if(children==null){
			children = new ArrayList<IsProgramDetail>();
		}
		
		children.add(activity);
	}
}
