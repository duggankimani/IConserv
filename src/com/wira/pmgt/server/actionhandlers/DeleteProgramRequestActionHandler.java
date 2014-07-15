package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.requests.DeleteProgramRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteProgramResponse;

public class DeleteProgramRequestActionHandler extends
		BaseActionHandler<DeleteProgramRequest, DeleteProgramResponse> {

	@Inject
	public DeleteProgramRequestActionHandler() {
	}

	@Override
	public void execute(DeleteProgramRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		ProgramDaoHelper.delete(action.getProgramId());
	}

	@Override
	public Class<DeleteProgramRequest> getActionType() {
		return DeleteProgramRequest.class;
	}
}
