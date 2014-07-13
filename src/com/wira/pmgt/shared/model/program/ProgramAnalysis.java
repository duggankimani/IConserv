package com.wira.pmgt.shared.model.program;

import java.io.Serializable;

public class ProgramAnalysis implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private Double budgetAmount=0.0; //Total budget amount (accumulation of source of funds)
	private Double actualAmount=0.0; //Actual amount spent
	private Double commitedAmount=0.0;
	private Double totalAllocation=0.0;
	private Double actual=0.0;
	private Double diff=0.0;
	
	public ProgramAnalysis(Long id,	String name,String description,
	Double budgetAmount, //Total budget amount (accumulation of source of funds)
	Double actualAmount, //Actual amount spent
	Double commitedAmount,Double totalAllocation){
		this.id=id;
		this.name = name;
		this.description=description;
		
		this.budgetAmount = budgetAmount==null? 0.0:budgetAmount;
		this.actualAmount= actualAmount==null? 0.0:actualAmount;
		this.commitedAmount= commitedAmount==null?0.0:commitedAmount;
		this.totalAllocation= totalAllocation==null? 0.0:totalAllocation;
		actual= this.actualAmount==null || this.actualAmount==0 ? this.commitedAmount: this.actualAmount;
		diff = this.budgetAmount-this.actual;
	}
	
	public ProgramAnalysis() {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Double getCommitedAmount() {
		return commitedAmount;
	}
	public void setCommitedAmount(Double commitedAmount) {
		this.commitedAmount = commitedAmount;
	}
	public Double getTotalAllocation() {
		return totalAllocation;
	}
	public void setTotalAllocation(Double totalAllocation) {
		this.totalAllocation = totalAllocation;
	}

	public Double getActual() {
		return actual;
	}

	public Double getDiff() {
		return diff;
	}

	public boolean isWithinBudget() {
		return diff>-1;
	}
	

}
