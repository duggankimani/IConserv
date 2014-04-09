package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.SettingsDaoHelper;
import com.wira.pmgt.shared.model.settings.Setting;
import com.wira.pmgt.shared.requests.GetSettingsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetSettingsResponse;

public class GetSettingsRequestActionHandler extends
		BaseActionHandler<GetSettingsRequest, GetSettingsResponse> {

	@Inject
	public GetSettingsRequestActionHandler() {
	}

	@Override
	public void execute(GetSettingsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<Setting> settings = SettingsDaoHelper.getSettings(action.getSettingNames());
		
		GetSettingsResponse response = (GetSettingsResponse)actionResult;		
		response.setSettings(settings);
	}
	
	@Override
	public Class<GetSettingsRequest> getActionType() {
		return GetSettingsRequest.class;
	}
}
