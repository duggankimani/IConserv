package com.wira.pmgt.shared.responses;

import java.lang.Boolean;

import com.wira.pmgt.shared.model.Document;

public class ApprovalRequestResult extends BaseResponse {

	private Boolean successfulSubmit;
	private Document document;

	@SuppressWarnings("unused")
	private ApprovalRequestResult() {
		// For serialization only
	}

	public ApprovalRequestResult(Boolean successfulSubmit) {
		this.successfulSubmit = successfulSubmit;
	}

	public Boolean getSuccessfulSubmit() {
		return successfulSubmit;
	}

	public void setSuccessfulSubmit(Boolean successfulSubmit) {
		this.successfulSubmit = successfulSubmit;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
