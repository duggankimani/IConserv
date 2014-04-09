package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetLongTasksResponse;

public class GetLongTasksRequest extends BaseRequest<GetLongTasksResponse> {

	public GetLongTasksRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetLongTasksResponse();
	}
}
