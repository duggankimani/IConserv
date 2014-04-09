package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetUserRequestResult;

import java.lang.String;

public class GetUserRequest extends BaseRequest<GetUserRequestResult> {

	private String userId;

	@SuppressWarnings("unused")
	private GetUserRequest() {
		// For serialization only
	}

	public GetUserRequest(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetUserRequestResult();
	}
}
