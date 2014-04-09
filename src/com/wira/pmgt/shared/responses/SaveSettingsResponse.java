package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.settings.Setting;

public class SaveSettingsResponse extends BaseResponse {

	private List<Setting> settings;

	public SaveSettingsResponse() {
	}

	public List<Setting> getSettings() {
		return settings;
	}

	public void setSettings(List<Setting> settings) {
		this.settings = settings;
	}

}
