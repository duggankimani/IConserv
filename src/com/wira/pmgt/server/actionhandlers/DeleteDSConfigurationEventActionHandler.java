package com.wira.pmgt.server.actionhandlers;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DSConfigHelper;
import com.wira.pmgt.shared.requests.DeleteDSConfigurationEvent;
import com.wira.pmgt.shared.responses.BaseResponse;

public class DeleteDSConfigurationEventActionHandler
		extends
		BaseActionHandler<DeleteDSConfigurationEvent, BaseResponse> {

	@Inject
	public DeleteDSConfigurationEventActionHandler() {
	}

	@Override
	public void execute(DeleteDSConfigurationEvent action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		DSConfigHelper.delete(action.getConfigurationId());
	}

	@Override
	public Class<DeleteDSConfigurationEvent> getActionType() {
		return DeleteDSConfigurationEvent.class;
	}
}
