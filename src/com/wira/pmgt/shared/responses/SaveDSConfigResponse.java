package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.DSConfiguration;

public class SaveDSConfigResponse extends BaseResponse {

	private DSConfiguration configuration;

	public SaveDSConfigResponse() {
	}

	public DSConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(DSConfiguration configuration) {
		this.configuration = configuration;
	}
}
