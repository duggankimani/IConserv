package com.wira.pmgt.shared.model.program;

import java.io.Serializable;

public class ProgramFundDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private FundDTO fund;
	private Long programId;
	private Double amount;//fund amount 
	
	public ProgramFundDTO() {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public FundDTO getFund() {
		return fund;
	}
	public void setFund(FundDTO fund) {
		this.fund = fund;
	}
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}