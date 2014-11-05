package com.wira.pmgt.server.dao.biz.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.wira.pmgt.server.dao.model.PO;
import com.wira.pmgt.shared.model.TargetConstraint;

@Entity
public class TargetAndOutcome extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private Double target;
	
	private Double actualOutcome;
	
	private TargetConstraint targetContraint=TargetConstraint.ATLEAST;
	
	@Column(nullable=false)
	private String measure;
	
	@Column(nullable=false)
	private String key;
	
	private String outcomeRemarks;//this may be provided in place of counts
	
	@ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="programid",nullable=false,referencedColumnName="id")
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
	
	@Override
	public boolean equals(Object obj) {
		TargetAndOutcome other = (TargetAndOutcome)obj;
		return other.measure.equals(measure);
	}
	public TargetConstraint getTargetContraint() {
		return targetContraint;
	}
	public void setTargetContraint(TargetConstraint targetContraint) {
		this.targetContraint = targetContraint;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
