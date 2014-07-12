package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.program.ProgramTaskForm;

public class GetProgramTaskFormResponse extends BaseResponse {

	private List<ProgramTaskForm> taskForms;

	public GetProgramTaskFormResponse() {
		// For serialization only
	}

	public GetProgramTaskFormResponse(List<ProgramTaskForm> taskForms) {
		this.taskForms = taskForms;
	}

	public List<ProgramTaskForm> getTaskForms() {
		return taskForms;
	}

	public void setTaskForms(List<ProgramTaskForm> taskForms) {
		this.taskForms = taskForms;
	}
}
