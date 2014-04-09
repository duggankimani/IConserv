package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetGroupsResponse;

public class GetGroupsRequest extends BaseRequest<GetGroupsResponse> {

	public GetGroupsRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetGroupsResponse();
	}
}
