package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.ProcessDef;

import java.util.List;

public class GetProcessesResponse extends BaseResponse {

	private List<ProcessDef> processes;

	public GetProcessesResponse() {
		// For serialization only
	}

	public GetProcessesResponse(List<ProcessDef> processes) {
		this.processes = processes;
	}

	public List<ProcessDef> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessDef> processes) {
		this.processes = processes;
	}
}
