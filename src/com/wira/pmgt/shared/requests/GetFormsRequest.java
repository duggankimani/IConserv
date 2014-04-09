package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetFormsResponse;

public class GetFormsRequest extends BaseRequest<GetFormsResponse> {

	public GetFormsRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetFormsResponse();
	}
}
