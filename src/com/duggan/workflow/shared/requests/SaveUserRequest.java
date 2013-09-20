package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveUserResponse;
import com.duggan.workflow.shared.model.HTUser;

/**
 * 
 * @author duggan
 *
 */
public class SaveUserRequest extends BaseRequest<SaveUserResponse> {

	private HTUser user;

	@SuppressWarnings("unused")
	private SaveUserRequest() {
	}

	public SaveUserRequest(HTUser user) {
		this.user = user;
	}

	public HTUser getUser() {
		return user;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new SaveUserResponse();
	}
}