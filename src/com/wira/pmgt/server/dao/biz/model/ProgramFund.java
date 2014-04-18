package com.wira.pmgt.server.dao.biz.model;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Double;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * This holds the amount funded from a single Fund
 * for a Single Program
 * 
 * @author duggan
 *
 */
@Entity
public class ProgramFund implements Serializable {

	private static final long serialVersionUID = -5816043324216522679L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long fundId;
	private Long programId;
	private Double amount;
	
	@ManyToOne
	@JoinColumn(name="fundid", referencedColumnName="id", nullable=true)
	private Fund fund;
	
	@ManyToOne
	@JoinColumn(name="programid", referencedColumnName="id", nullable=true)
	private ProgramDetail programDetail;
	

	public ProgramFund() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFundId(Long fundId) {
		this.fundId = fundId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public Long getFundId() {
		return fundId;
	}

	public Long getProgramId() {
		return programId;
	}

	public Double getAmount() {
		return amount;
	}
}
