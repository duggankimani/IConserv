package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.exceptions.IllegalApprovalRequestException;
import com.wira.pmgt.shared.model.DocStatus;
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
		doc.setStatus(DocStatus.INPROGRESS);
		doc = DocumentDaoHelper.save(doc);
		
		if(doc.getProcessInstanceId()!=null){
			throw new IllegalApprovalRequestException(doc);
		}
		String userId = action.getUsername();
		if(userId==null)
			userId = SessionHelper.getCurrentUser().getUserId();
		
		JBPMHelper.get().createApprovalRequest(userId, doc);
		
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
