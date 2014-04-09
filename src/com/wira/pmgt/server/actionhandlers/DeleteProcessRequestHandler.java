package com.wira.pmgt.server.actionhandlers;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProcessDefHelper;
import com.wira.pmgt.shared.requests.DeleteProcessRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteProcessResponse;

public class DeleteProcessRequestHandler extends
		BaseActionHandler<DeleteProcessRequest, DeleteProcessResponse> {

	@Inject
	public DeleteProcessRequestHandler() {
	}
	
	@Override
	public void execute(DeleteProcessRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		ProcessDefHelper.delete(action.getProcessId());
		
	}
	
	@Override
	public Class<DeleteProcessRequest> getActionType() {
		return DeleteProcessRequest.class;
	}
}
