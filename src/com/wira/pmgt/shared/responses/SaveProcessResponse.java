package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.ProcessDef;

public class SaveProcessResponse extends BaseResponse {

	private ProcessDef processDef;

	public SaveProcessResponse() {
	}

	public SaveProcessResponse(ProcessDef processDef) {
		this.processDef = processDef;
	}

	public ProcessDef getProcessDef() {
		return processDef;
	}

	public void setProcessDef(ProcessDef processDef) {
		this.processDef = processDef;
	}
}
