package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetUsersResponse;

public class GetUsersRequest extends BaseRequest<GetUsersResponse> {

	public GetUsersRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetUsersResponse();
	}
}
