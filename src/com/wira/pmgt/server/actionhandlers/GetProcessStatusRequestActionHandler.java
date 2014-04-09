package com.wira.pmgt.server.actionhandlers;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.shared.requests.GetProcessStatusRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProcessStatusRequestResult;

public class GetProcessStatusRequestActionHandler extends
		BaseActionHandler<GetProcessStatusRequest, GetProcessStatusRequestResult> {

	static Logger logger = Logger.getLogger(GetProcessStatusRequestActionHandler.class);
	
	@Inject
	public GetProcessStatusRequestActionHandler() {
	}

	@Override
	public void execute(GetProcessStatusRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetProcessStatusRequestResult result = (GetProcessStatusRequestResult)actionResult;
		
		try{
			result.setNodes(JBPMHelper.get().getWorkflowProcessDia(action.getProcessInstanceId()));
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Loading Workflow diagram failed cause: "+e.getMessage());
			// no throwing exceptions back to the client
		}
		
	}
	
	@Override
	public Class<GetProcessStatusRequest> getActionType() {
		return GetProcessStatusRequest.class;
	}
}
