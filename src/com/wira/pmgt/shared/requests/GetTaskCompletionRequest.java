package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetTaskCompletionResponse;

public class GetTaskCompletionRequest extends
		BaseRequest<GetTaskCompletionResponse> {

	public GetTaskCompletionRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskCompletionResponse();
	}
}
