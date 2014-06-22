package com.wira.pmgt.shared.model.program;

import java.io.Serializable;

import com.wira.pmgt.shared.model.TargetConstraint;

/**
 * 
 * @author duggan
 *
 */
public class TargetAndOutcomeDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Double target;
	private Double actualOutcome;
	private String measure;
	private String outcomeRemarks;
	private Long programDetailId;
	private TargetConstraint targetContraint=TargetConstraint.ATLEAST;
	private String key;
	
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

	public Long getProgramDetailId() {
		return programDetailId;
	}

	public void setProgramDetailId(Long programActivityId) {
		this.programDetailId = programActivityId;
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
	
	public TargetAndOutcomeDTO clone(){
		TargetAndOutcomeDTO copy = new TargetAndOutcomeDTO();
		copy.setActualOutcome(actualOutcome);
		copy.setId(programDetailId);
		copy.setKey(key);
		copy.setMeasure(measure);
		copy.setOutcomeRemarks(outcomeRemarks);
		copy.setProgramDetailId(programDetailId);
		copy.setTarget(target);
		copy.setTargetContraint(targetContraint);
		
		return copy;
	}
	
}
