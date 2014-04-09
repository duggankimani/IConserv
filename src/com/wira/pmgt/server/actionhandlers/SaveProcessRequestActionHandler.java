package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProcessDefHelper;
import com.wira.pmgt.shared.model.ProcessDef;
import com.wira.pmgt.shared.requests.SaveProcessRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveProcessResponse;

public class SaveProcessRequestActionHandler extends
		BaseActionHandler<SaveProcessRequest, SaveProcessResponse> {

	@Inject
	public SaveProcessRequestActionHandler() {
	}
	
	@Override
	public void execute(SaveProcessRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		ProcessDef def = action.getProcessDef();
		
		def = ProcessDefHelper.save(def);
		
		SaveProcessResponse response = (SaveProcessResponse)actionResult;
		response.setProcessDef(def);
	}
	
	@Override
	public Class<SaveProcessRequest> getActionType() {
		return SaveProcessRequest.class;
	}
}
