package com.wira.pmgt.server.dao.biz.model;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Double;

import javax.persistence.CascadeType;
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
	private Double amount;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="fundid", referencedColumnName="id", nullable=false)
	private Fund fund;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="programid", referencedColumnName="id", nullable=false)
	private ProgramDetail programDetail;

	public ProgramFund() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public Double getAmount() {
		return amount;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public ProgramDetail getProgramDetail() {
		return programDetail;
	}

	public void setProgramDetail(ProgramDetail programDetail) {
		this.programDetail = programDetail;
	}
}
