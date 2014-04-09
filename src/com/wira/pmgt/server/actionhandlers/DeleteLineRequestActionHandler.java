package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.shared.requests.DeleteLineRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteLineResponse;

public class DeleteLineRequestActionHandler extends
		BaseActionHandler<DeleteLineRequest, DeleteLineResponse> {

	@Inject
	public DeleteLineRequestActionHandler() {
	}

	@Override
	public void execute(DeleteLineRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		DocumentDaoHelper.delete(action.getLine());
		DeleteLineResponse response = (DeleteLineResponse)actionResult;
		response.setDelete(true);
	}

	@Override
	public void undo(DeleteLineRequest action, DeleteLineResponse result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<DeleteLineRequest> getActionType() {
		return DeleteLineRequest.class;
	}
}
