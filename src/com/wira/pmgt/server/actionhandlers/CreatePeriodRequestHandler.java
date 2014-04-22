package com.wira.pmgt.server.actionhandlers;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.requests.CreatePeriodRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreatePeriodResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class CreatePeriodRequestHandler extends
		BaseActionHandler<CreatePeriodRequest, CreatePeriodResponse> {

	@Inject
	public CreatePeriodRequestHandler() {
	}

	@Override
	public void execute(CreatePeriodRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		PeriodDTO period = ProgramDaoHelper.save(action.getPeriod());
		((CreatePeriodResponse)actionResult).setPeriod(period);
	}

	@Override
	public Class<CreatePeriodRequest> getActionType() {
		return CreatePeriodRequest.class;
	}
}
