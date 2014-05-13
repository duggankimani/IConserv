package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.jbpm.TaskApiHelper;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.requests.ApprovalRequest;
import com.wira.pmgt.shared.responses.ApprovalRequestResult;
import com.wira.pmgt.shared.responses.BaseResponse;

public class ApprovalRequestActionHandler extends
		BaseActionHandler<ApprovalRequest, ApprovalRequestResult> {

	@Inject
	public ApprovalRequestActionHandler() {
	}

	@Override
	public void execute(ApprovalRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		Document doc= action.getDocument();
		
		String userId = action.getUsername();
		TaskApiHelper.startWorkflow(doc,userId);
		
		ApprovalRequestResult result = (ApprovalRequestResult)actionResult;
		result.setSuccessfulSubmit(true);
		result.setDocument(doc);
	}
	

	@Override
	public void undo(ApprovalRequest action, ApprovalRequestResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<ApprovalRequest> getActionType() {
		return ApprovalRequest.class;
	}
}
