package com.wira.pmgt.server.actionhandlers;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetFundsRequestHandler extends
		BaseActionHandler<GetFundsRequest, GetFundsResponse> {

	@Inject
	public GetFundsRequestHandler() {
	}

	@Override
	public void execute(GetFundsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetFundsResponse response = (GetFundsResponse)actionResult;
		
		response.setFunds(ProgramDaoHelper.getFunds(action.getParentId()));
	}

	@Override
	public Class<GetFundsRequest> getActionType() {
		return GetFundsRequest.class;
	}
}
