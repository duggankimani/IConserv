package com.wira.pmgt.server.actionhandlers;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.requests.CreateActivityFormRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateActivityFormResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class CreateActivityFormRequestHandler
		extends 
		BaseActionHandler<CreateActivityFormRequest, CreateActivityFormResponse> {

	@Inject
	public CreateActivityFormRequestHandler() {
	}

	@Override
	public void execute(CreateActivityFormRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		Long activityId= action.getActivityId();
		
		Long formId = ProgramDaoHelper.generateFormFor(activityId);
		
		CreateActivityFormResponse response = (CreateActivityFormResponse)actionResult;
		response.setFormId(formId);
	}

	@Override
	public Class<CreateActivityFormRequest> getActionType() {
		return CreateActivityFormRequest.class;
	}
}
