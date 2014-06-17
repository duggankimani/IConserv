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
	private Double allocation;//amount already allocated from this fund
	
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

	public Double getAllocation() {
		return allocation;
	}

	public void setAllocation(Double allocation) {
		this.allocation = allocation;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof ProgramFundDTO))
			return false;
		
		ProgramFundDTO other = (ProgramFundDTO)obj;
		if(fund==null || other.fund==null){
			return false;
		}
		
		return fund.equals(other.fund);
	}
	
	@Override
	public String toString() {
		return fund.getName()+" : "+amount+" ("+allocation+")";
	}
	
}