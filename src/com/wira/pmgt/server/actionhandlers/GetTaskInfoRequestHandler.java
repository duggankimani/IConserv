package com.wira.pmgt.server.actionhandlers;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.requests.GetTaskInfoRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetTaskInfoResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTaskInfoRequestHandler extends
		BaseActionHandler<GetTaskInfoRequest, GetTaskInfoResponse> {

	@Inject
	public GetTaskInfoRequestHandler() {
	}

	@Override
	public void execute(GetTaskInfoRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		TaskInfo info = ProgramDaoHelper.getTaskInfo(action.getProgramId());
		((GetTaskInfoResponse)actionResult).setTaskInfo(info);
	}

	@Override
	public Class<GetTaskInfoRequest> getActionType() {
		return GetTaskInfoRequest.class;
	}
}
