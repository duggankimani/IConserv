package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.GetFundsResponse;

public class GetFundsRequest extends BaseRequest<GetFundsResponse> {

	public GetFundsRequest() {
	}
	
	public com.wira.pmgt.shared.responses.BaseResponse createDefaultActionResponse() {
		return new GetFundsResponse();
	};
}
