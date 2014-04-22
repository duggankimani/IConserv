package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPeriodsResponse;

public class GetPeriodsRequest extends BaseRequest<GetPeriodsResponse> {

	public GetPeriodsRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetPeriodsResponse();
	}
}
