package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.responses.ApprovalRequestResult;
import com.wira.pmgt.shared.responses.BaseResponse;

import java.lang.String;

public class ApprovalRequest extends BaseRequest<ApprovalRequestResult> {

	private String username;
	private Document document;

	@SuppressWarnings("unused")
	private ApprovalRequest() {
	}

	public ApprovalRequest(String username, Document doc) {
		this.username = username;
		this.document = doc;
	}

	public String getUsername() {
		return username;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new ApprovalRequestResult(false);
	}

	public Document getDocument() {
		return document;
	}
	
}
