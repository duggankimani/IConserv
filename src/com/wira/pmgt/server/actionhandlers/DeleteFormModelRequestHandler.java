package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.FormDaoHelper;
import com.wira.pmgt.shared.requests.DeleteFormModelRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteFormModelResponse;

public class DeleteFormModelRequestHandler extends
		BaseActionHandler<DeleteFormModelRequest, DeleteFormModelResponse> {

	@Inject
	public DeleteFormModelRequestHandler() {
	}

	@Override
	public void execute(DeleteFormModelRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		FormDaoHelper.delete(action.getModel());
	}

	@Override
	public Class<DeleteFormModelRequest> getActionType() {
		return DeleteFormModelRequest.class;
	}
}
