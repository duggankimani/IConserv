package com.wira.pmgt.server.actionhandlers;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.requests.CreateProgramRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateProgramResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class CreateProgramRequestHandler extends
		BaseActionHandler<CreateProgramRequest, CreateProgramResponse> {

	@Inject
	public CreateProgramRequestHandler() {
	}

	@Override
	public void execute(CreateProgramRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		((CreateProgramResponse)actionResult).setProgram(ProgramDaoHelper.save(action.getProgram()));
	}

	@Override
	public Class<CreateProgramRequest> getActionType() {
		return CreateProgramRequest.class;
	}
}
