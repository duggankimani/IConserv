package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.ProcessDef;

public class ManageKnowledgeBaseResponse extends BaseResponse {

	private ProcessDef processDef;
	
	public ManageKnowledgeBaseResponse() {
		
	}

	public ProcessDef getProcessDef() {
		return processDef;
	}

	public void setProcessDef(ProcessDef processDef) {
		this.processDef = processDef;
	}
}
