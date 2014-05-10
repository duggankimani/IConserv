package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.jbpm.TaskApiHelper;
import com.wira.pmgt.shared.requests.AssignTaskRequest;
import com.wira.pmgt.shared.responses.AssignTaskResponse;
import com.wira.pmgt.shared.responses.BaseResponse;

public class AssignTaskRequestHandler extends
		BaseActionHandler<AssignTaskRequest, AssignTaskResponse> {

	@Inject
	public AssignTaskRequestHandler() {
	}

	@Override
	public void execute(AssignTaskRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		TaskApiHelper.createTask(action.getTaskInfo());
	}

	@Override
	public Class<AssignTaskRequest> getActionType() {
		return AssignTaskRequest.class;
	}
}
