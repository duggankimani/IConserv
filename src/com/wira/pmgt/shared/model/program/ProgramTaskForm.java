package com.wira.pmgt.shared.model.program;

import java.io.Serializable;
import java.lang.String;
import java.lang.Long;

public class ProgramTaskForm implements Serializable {

	private static final long serialVersionUID = 324374778302842866L;
	private String formName;
	private Long formId;

	public ProgramTaskForm() {
	}
	
	public ProgramTaskForm(Long formId, String formName) {
		this.formId = formId;
		this.formName = formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String getFormName() {
		return formName;
	}

	public Long getFormId() {
		return formId;
	}
}
