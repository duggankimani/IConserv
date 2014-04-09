package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.SettingsDaoHelper;
import com.wira.pmgt.server.helper.email.EmailServiceHelper;
import com.wira.pmgt.shared.requests.SaveSettingsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveSettingsResponse;

public class SaveSettingsRequestActionHandler extends
		BaseActionHandler<SaveSettingsRequest, SaveSettingsResponse> {

	@Inject
	public SaveSettingsRequestActionHandler() {
	}

	@Override
	public void execute(SaveSettingsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		SettingsDaoHelper.save(action.getSettings());
		SaveSettingsResponse response = (SaveSettingsResponse)actionResult;
		response.setSettings(SettingsDaoHelper.getSettings(null));
		
		//proactively re-initialize email service incase there was a change
		EmailServiceHelper.initProperties();
	}
	
	@Override
	public Class<SaveSettingsRequest> getActionType() {
		return SaveSettingsRequest.class;
	}
}
