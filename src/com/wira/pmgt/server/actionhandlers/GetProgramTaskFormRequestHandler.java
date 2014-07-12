package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.model.program.ProgramTaskForm;
import com.wira.pmgt.shared.requests.GetProgramTaskForm;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramTaskFormResponse;

public class GetProgramTaskFormRequestHandler extends
		BaseActionHandler<GetProgramTaskForm, GetProgramTaskFormResponse> {

	@Inject
	public GetProgramTaskFormRequestHandler() {
	}

	@Override
	public void execute(GetProgramTaskForm action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<ProgramTaskForm> forms = ProgramDaoHelper.getTaskForms(action.getProgramId());
		GetProgramTaskFormResponse resp = (GetProgramTaskFormResponse)actionResult;
		resp.setTaskForms(forms);
	}
	
	@Override
	public Class<GetProgramTaskForm> getActionType() {
		return GetProgramTaskForm.class;
	}
}
