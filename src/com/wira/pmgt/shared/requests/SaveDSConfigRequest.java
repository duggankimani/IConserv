package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.DSConfiguration;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveDSConfigResponse;

public class SaveDSConfigRequest extends BaseRequest<SaveDSConfigResponse> {

	private DSConfiguration configuration;

	@SuppressWarnings("unused")
	private SaveDSConfigRequest() {
		// For serialization only
	}

	public SaveDSConfigRequest(DSConfiguration configuration) {
		this.configuration = configuration;
	}

	public DSConfiguration getConfiguration() {
		return configuration;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveDSConfigResponse();
	}
}
