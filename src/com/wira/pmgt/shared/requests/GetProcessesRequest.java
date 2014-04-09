package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProcessesResponse;

public class GetProcessesRequest extends BaseRequest<GetProcessesResponse> {

	public GetProcessesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessesResponse();
	}
}
