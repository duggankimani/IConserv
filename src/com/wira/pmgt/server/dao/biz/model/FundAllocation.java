package com.wira.pmgt.server.dao.biz.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class FundAllocation implements Serializable{


	private static final long serialVersionUID = -5038433776061027590L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(optional=false)
	@JoinColumn(name="programfundid")
	private ProgramFund fund;
	
	private Double allocation;
	
	private Double actual;//Actual consumption from the fund
	
	public Long getId() {
		return id;
	}
	
	public ProgramFund getFund() {
		return fund;
	}

	public Double getAllocation() {
		return allocation;
	}

	public Double getActual() {
		return actual;
	}

	public void setActual(Double actual) {
		this.actual = actual;
	}
	
}
