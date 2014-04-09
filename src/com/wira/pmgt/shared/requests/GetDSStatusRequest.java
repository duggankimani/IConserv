package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDSStatusResponse;

import java.lang.String;

public class GetDSStatusRequest extends BaseRequest<GetDSStatusResponse> {

	private String configName;

	@SuppressWarnings("unused")
	public GetDSStatusRequest() {
		// For serialization only
	}

	public GetDSStatusRequest(String configName) {
		this.configName = configName;
	}

	public String getConfigName() {
		return configName;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDSStatusResponse();
	}
}
