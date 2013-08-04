package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetNotificationsActionResult;

import java.lang.String;

public class GetNotificationsAction extends
		BaseRequest<GetNotificationsActionResult> {

	private String userId;

	@SuppressWarnings("unused")
	private GetNotificationsAction() {
	}

	public GetNotificationsAction(String userId) {
		this.userId = userId;
	}
	
	@Override
	public BaseResult createDefaultActionResponse() {		
		return new GetNotificationsActionResult();
	}

	public String getUserId() {
		return userId;
	}
}