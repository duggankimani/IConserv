package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.LogoutActionResult;

public class LogoutAction extends BaseRequest<LogoutActionResult> {

	public LogoutAction() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new LogoutActionResult();
	}
}
