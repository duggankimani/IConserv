package com.wira.pmgt.shared.model.program;

/**
 * 
 * @author duggan
 *
 */
public class TargetAndOutcomeDTO {

	private Long id;
	private Double target;
	private Double actualOutcome;
	private String measure;
	private String outcomeRemarks;
	private Long programActivityId;
	
	public TargetAndOutcomeDTO() {
	}
	
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
	public Double getActualOutcome() {
		return actualOutcome;
	}
	public void setActualOutcome(Double actualOutcome) {
		this.actualOutcome = actualOutcome;
	}

	public Long getProgramActivityId() {
		return programActivityId;
	}

	public void setProgramActivityId(Long programActivityId) {
		this.programActivityId = programActivityId;
	}
	
}
