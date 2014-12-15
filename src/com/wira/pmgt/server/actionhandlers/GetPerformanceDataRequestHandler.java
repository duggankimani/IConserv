package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.model.program.PerformanceModel;
import com.wira.pmgt.shared.requests.GetPerformanceDataRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPerformanceDataResponse;

public class GetPerformanceDataRequestHandler extends 
BaseActionHandler<GetPerformanceDataRequest, GetPerformanceDataResponse>{
	
	@Override
	public void execute(GetPerformanceDataRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		List<PerformanceModel> list = ProgramDaoHelper.getPerformanceData(action.getMetric(), action.getPeriodId());
		((GetPerformanceDataResponse)actionResult).setData(list);
	}

	public Class<GetPerformanceDataRequest> getActionType() {
		return GetPerformanceDataRequest.class;
	};
}
