package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProcessDefHelper;
import com.wira.pmgt.shared.model.ProcessDef;
import com.wira.pmgt.shared.requests.GetProcessesRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProcessesResponse;

public class GetProcessesRequestActionHandler extends
		BaseActionHandler<GetProcessesRequest, GetProcessesResponse> {

	@Inject
	public GetProcessesRequestActionHandler() {
	}

	@Override
	public void execute(GetProcessesRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		List<ProcessDef> processes = ProcessDefHelper.getAllProcesses();
		GetProcessesResponse response = (GetProcessesResponse)actionResult;
		
		response.setProcesses(processes);
	}

	@Override
	public Class<GetProcessesRequest> getActionType() {
		return GetProcessesRequest.class;
	}
}
