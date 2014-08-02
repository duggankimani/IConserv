package com.wira.pmgt.server.actionhandlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.requests.GetProgramsTreeRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramsTreeResponse;

public class GetProgramsTreeRequestHandler extends BaseActionHandler<GetProgramsTreeRequest, GetProgramsTreeResponse> {
	
	@Override
	public void execute(GetProgramsTreeRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		((GetProgramsTreeResponse)actionResult).setModels(ProgramDaoHelper.getProgramTree(action.getPeriodId(), action.getProgramId()));
	}
	
	public java.lang.Class<GetProgramsTreeRequest> getActionType() {
		return GetProgramsTreeRequest.class;
	};

}
