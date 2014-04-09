package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProcessDefHelper;
import com.wira.pmgt.shared.model.ProcessDef;
import com.wira.pmgt.shared.requests.GetProcessRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProcessResponse;

public class GetProcessRequestActionHandler extends
		BaseActionHandler<GetProcessRequest, GetProcessResponse> {

	@Inject
	public GetProcessRequestActionHandler() {
	}
	
	@Override
	public void execute(GetProcessRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Long processDefId = action.getProcessDefId();
		
		ProcessDef process = ProcessDefHelper.getProcessDef(processDefId);
		
		GetProcessResponse response = (GetProcessResponse)actionResult;
		
		response.setProcessDef(process);
	}
	
	@Override
	public Class<GetProcessRequest> getActionType() {
		return GetProcessRequest.class;
	}
}
