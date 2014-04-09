package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.settings.SETTINGNAME;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetSettingsResponse;

import java.util.List;

public class GetSettingsRequest extends BaseRequest<GetSettingsResponse> {

	private List<SETTINGNAME> settingNames;

	public GetSettingsRequest() {
	}
	
	public GetSettingsRequest(List<SETTINGNAME> names){
		this.settingNames = names;
	}

	public List<SETTINGNAME> getSettingNames() {
		return settingNames;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetSettingsResponse();
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}
}
