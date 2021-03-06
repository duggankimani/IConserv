package com.wira.pmgt.server.dao.biz.model;

import java.lang.Long;
import java.lang.Double;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.wira.pmgt.server.dao.model.PO;

/**
 * This holds the amount funded from a single Fund
 * for a Single Program
 * 
 * @author duggan
 *
 */
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"fundid","programid"}))
@Entity
public class ProgramFund extends PO {

	private static final long serialVersionUID = -5816043324216522679L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//Total amount assigned to a program/activity/task from a fund (e.g. 30M from USAID for Wildlife Program)
	private Double amount;
	private Double allocatedAmount;//allocated during budgeting
	private Double commitedAmount;//Committed at program start
	public void setCommitedAmount(Double commitedAmount) {
		this.commitedAmount = commitedAmount;
	}

	private Double actualAmount;//Report actual utilization
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="fundid", referencedColumnName="id", nullable=false)
	private Fund fund;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
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

	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof ProgramFund)){
			return false;
		}
		
		ProgramFund other = (ProgramFund)obj;
		if(other.fund==null || fund==null || other.programDetail==null || programDetail==null)
			return false;
		
		return fund.equals(other.fund) && programDetail.equals(other.programDetail);
	}

	public Double getCommitedAmount() {
		return commitedAmount;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
		//reset committed figures
		
	}

	public Double getAllocatedAmount() {
		return allocatedAmount;
	}

	public void commitFunds() {
		commitedAmount=amount;
	}

	public void resetCommited() {
		this.commitedAmount=0.0;
	}
	
	@Override
	public String toString() {
		return "id="+id+": fundId="+fund.getId();
	}
	
}
