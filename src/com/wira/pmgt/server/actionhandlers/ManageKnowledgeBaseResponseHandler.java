package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProcessDefHelper;
import com.wira.pmgt.server.helper.jbpm.ProcessMigrationHelper;
import com.wira.pmgt.shared.model.ManageProcessAction;
import com.wira.pmgt.shared.model.ProcessDef;
import com.wira.pmgt.shared.requests.ManageKnowledgeBaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.ManageKnowledgeBaseResponse;

public class ManageKnowledgeBaseResponseHandler
		extends
		BaseActionHandler<ManageKnowledgeBaseRequest, ManageKnowledgeBaseResponse> {

	@Inject
	public ManageKnowledgeBaseResponseHandler() {
	}

	@Override
	public void execute(ManageKnowledgeBaseRequest request,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		ManageProcessAction action = request.getAction();
		
		switch (action) {
		case ACTIVATE:
			ProcessMigrationHelper.start(request.getProcessDefId(), request.isForce());
			break;
		case DEACTIVATE:
			ProcessMigrationHelper.stop(request.getProcessDefId());
			break;
		}
		
		ProcessDef def = ProcessDefHelper.getProcessDef(request.getProcessDefId());
		
		ManageKnowledgeBaseResponse response = (ManageKnowledgeBaseResponse)actionResult;
		response.setProcessDef(def);
	}

	@Override
	public Class<ManageKnowledgeBaseRequest> getActionType() {
		return ManageKnowledgeBaseRequest.class;
	}
}
