package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.AttachmentDaoHelper;
import com.wira.pmgt.shared.requests.DeleteAttachmentRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteAttachmentResponse;

public class DeleteAttachmentRequestHandler extends
		BaseActionHandler<DeleteAttachmentRequest, DeleteAttachmentResponse> {

	@Inject
	public DeleteAttachmentRequestHandler() {
	}

	@Override
	public void execute(DeleteAttachmentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		boolean deleted = AttachmentDaoHelper.delete(action.getAttachmentId());
		
		DeleteAttachmentResponse response = (DeleteAttachmentResponse)actionResult;
		response.setIsDeleted(deleted);
	}

	@Override
	public Class<DeleteAttachmentRequest> getActionType() {
		return DeleteAttachmentRequest.class;
	}
}
