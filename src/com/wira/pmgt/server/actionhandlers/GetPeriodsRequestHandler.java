package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPeriodsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetPeriodsRequestHandler extends
		BaseActionHandler<GetPeriodsRequest, GetPeriodsResponse> {

	@Inject
	public GetPeriodsRequestHandler() {
	}

	@Override
	public void execute(GetPeriodsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<PeriodDTO> periods = ProgramDaoHelper.getPeriods();
		((GetPeriodsResponse)actionResult).setPeriods(periods);
	}
	
	@Override
	public Class<GetPeriodsRequest> getActionType() {
		return GetPeriodsRequest.class;
	}
}
