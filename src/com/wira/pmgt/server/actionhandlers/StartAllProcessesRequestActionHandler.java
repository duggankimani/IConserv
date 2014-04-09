package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.jbpm.ProcessMigrationHelper;
import com.wira.pmgt.shared.requests.StartAllProcessesRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.StartAllProcessesResponse;

public class StartAllProcessesRequestActionHandler extends
		BaseActionHandler<StartAllProcessesRequest, StartAllProcessesResponse> {

	@Inject
	public StartAllProcessesRequestActionHandler() {
	}

	@Override
	public void execute(StartAllProcessesRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		ProcessMigrationHelper.init();		
	}
	
	@Override
	public Class<StartAllProcessesRequest> getActionType() {
		return StartAllProcessesRequest.class;
	}
}
