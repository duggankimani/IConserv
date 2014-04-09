package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CheckPasswordRequestResult;

import java.lang.String;

public class CheckPasswordRequest extends
		BaseRequest<CheckPasswordRequestResult> {

	private String userId;
	private String password;

	@SuppressWarnings("unused")
	private CheckPasswordRequest() {
		// For serialization only
	}

	public CheckPasswordRequest(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new CheckPasswordRequestResult();
	}
}
