package com.wira.pmgt.server.dao.biz.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.wira.pmgt.server.dao.model.PO;

@Entity
public class TargetAndOutcome extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Double target;
	private Double actualOutcome;
	private String measure;
	private String outcomeRemarks;//this may be provided in place of counts
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="programdetailid",nullable=false,referencedColumnName="id")
	private ProgramDetail programDetail;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getTarget() {
		return target;
	}
	public void setTarget(Double target) {
		this.target = target;
	}
	public Double getActualOutcome() {
		return actualOutcome;
	}
	public void setActualOutcome(Double actualOutcome) {
		this.actualOutcome = actualOutcome;
	}
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	public String getOutcomeRemarks() {
		return outcomeRemarks;
	}
	public void setOutcomeRemarks(String outcomeRemarks) {
		this.outcomeRemarks = outcomeRemarks;
	}
	public ProgramDetail getProgramDetail() {
		return programDetail;
	}
	public void setProgramDetail(ProgramDetail programDetail) {
		this.programDetail = programDetail;
	}

}
