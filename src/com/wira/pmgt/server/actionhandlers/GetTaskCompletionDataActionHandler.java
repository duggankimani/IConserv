package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.requests.GetTaskCompletionRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetTaskCompletionResponse;

public class GetTaskCompletionDataActionHandler extends
		BaseActionHandler<GetTaskCompletionRequest, GetTaskCompletionResponse> {

	@Inject
	public GetTaskCompletionDataActionHandler() {
	}

	@Override
	public void execute(GetTaskCompletionRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
	
		GetTaskCompletionResponse response = (GetTaskCompletionResponse)actionResult;
		response.setData(DB.getDashboardDao().getTaskCompletionData());
	}
	
	@Override
	public Class<GetTaskCompletionRequest> getActionType() {
		return GetTaskCompletionRequest.class;
	}
}
