package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDSConfigurationsResponse;

public class GetDSConfigurationsRequest extends
		BaseRequest<GetDSConfigurationsResponse> {

	public GetDSConfigurationsRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDSConfigurationsResponse();
	}
}
