package com.wira.pmgt.server.actionhandlers;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.requests.GetPeriodRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPeriodResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetPeriodRequestHandler extends
		BaseActionHandler<GetPeriodRequest, GetPeriodResponse> {

	@Inject
	public GetPeriodRequestHandler() {
	}

	@Override
	public void execute(GetPeriodRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		((GetPeriodResponse)actionResult).setPeriod(ProgramDaoHelper.getActivePeriod());
	}

	@Override
	public Class<GetPeriodRequest> getActionType() {
		return GetPeriodRequest.class;
	}
}
