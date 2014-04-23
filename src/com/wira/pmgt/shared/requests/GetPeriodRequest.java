package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPeriodResponse;

public class GetPeriodRequest extends BaseRequest<GetPeriodResponse> {

	public GetPeriodRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetPeriodResponse();
	}
}
