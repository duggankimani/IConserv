package com.wira.pmgt.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.wira.pmgt.shared.model.DSConfiguration;

/**
 * 
 * @author duggan
 *
 */
public class GetDSStatusResponse extends BaseResponse {

	private List<DSConfiguration> configs = new ArrayList<DSConfiguration>();

	public GetDSStatusResponse() {
	}

	public List<DSConfiguration> getConfigs() {
		return configs;
	}

	public void setConfigs(List<DSConfiguration> configs) {
		this.configs = configs;
	}

	public void addConfig(DSConfiguration config) {
		configs.add(config);
	}

}
