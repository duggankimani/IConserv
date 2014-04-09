package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.requests.GetLongTasksRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetLongTasksResponse;

public class GetLongTasksRequestActionHandler extends
		BaseActionHandler<GetLongTasksRequest, GetLongTasksResponse> {

	@Inject
	public GetLongTasksRequestActionHandler() {
	}

	@Override
	public void execute(GetLongTasksRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		GetLongTasksResponse response = (GetLongTasksResponse)actionResult;
		response.setLongTasks(DB.getDashboardDao().getLongLivingTasks());
	}
	
	@Override
	public Class<GetLongTasksRequest> getActionType() {
		return GetLongTasksRequest.class;
	}
}
