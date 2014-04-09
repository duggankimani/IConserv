package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.Doc;

public class ExecuteWorkflowResult extends BaseResponse {

	Doc document;
	
	public ExecuteWorkflowResult() {
	}

	public Doc getDocument() {
		return document;
	}

	public void setDocument(Doc document) {
		this.document = document;
	}
	
}
