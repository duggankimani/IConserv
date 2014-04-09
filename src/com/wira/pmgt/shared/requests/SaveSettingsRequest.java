package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.settings.Setting;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveSettingsResponse;

import java.util.List;

public class SaveSettingsRequest extends BaseRequest<SaveSettingsResponse> {

	private List<Setting> settings;

	public SaveSettingsRequest() {
	}

	public SaveSettingsRequest(List<Setting> settings) {
		this.settings = settings;
	}

	public List<Setting> getSettings() {
		return settings;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveSettingsResponse();
	}
}
