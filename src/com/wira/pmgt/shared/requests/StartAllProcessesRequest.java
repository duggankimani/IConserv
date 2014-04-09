package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.StartAllProcessesResponse;

public class StartAllProcessesRequest extends
		BaseRequest<StartAllProcessesResponse> {

	public StartAllProcessesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new StartAllProcessesResponse();
	}
}
