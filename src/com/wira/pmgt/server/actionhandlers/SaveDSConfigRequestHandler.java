package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DSConfigHelper;
import com.wira.pmgt.shared.model.DSConfiguration;
import com.wira.pmgt.shared.requests.SaveDSConfigRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveDSConfigResponse;

public class SaveDSConfigRequestHandler extends
		BaseActionHandler<SaveDSConfigRequest, SaveDSConfigResponse> {

	@Inject
	public SaveDSConfigRequestHandler() {
	}

	@Override
	public void execute(SaveDSConfigRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		DSConfiguration configToSave = action.getConfiguration();
		
		DSConfiguration saved = DSConfigHelper.save(configToSave);
		
		SaveDSConfigResponse response = (SaveDSConfigResponse)actionResult;
		
		response.setConfiguration(saved);
	}

	@Override
	public Class<SaveDSConfigRequest> getActionType() {
		return SaveDSConfigRequest.class;
	}
}
