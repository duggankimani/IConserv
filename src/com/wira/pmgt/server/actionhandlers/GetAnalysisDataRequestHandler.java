package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.model.program.ProgramAnalysis;
import com.wira.pmgt.shared.requests.GetAnalysisDataRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetAnalysisDataResponse;

public class GetAnalysisDataRequestHandler extends
		BaseActionHandler<GetAnalysisDataRequest, GetAnalysisDataResponse> {

	@Inject
	public GetAnalysisDataRequestHandler() {
	}

	@Override
	public void execute(GetAnalysisDataRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		List<ProgramAnalysis> data = ProgramDaoHelper.getAnalysisData(action.getPeriodId());
		((GetAnalysisDataResponse)actionResult).setData(data);
	}

	@Override
	public Class<GetAnalysisDataRequest> getActionType() {
		return GetAnalysisDataRequest.class;
	}
}
