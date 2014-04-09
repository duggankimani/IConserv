package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.AttachmentDaoHelper;
import com.wira.pmgt.shared.requests.GetAttachmentsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetAttachmentsResponse;

public class GetAttachmentsRequestActionHandler extends
		BaseActionHandler<GetAttachmentsRequest, GetAttachmentsResponse> {

	@Inject
	public GetAttachmentsRequestActionHandler() {
	}

	@Override
	public void execute(GetAttachmentsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetAttachmentsResponse response = (GetAttachmentsResponse)actionResult;
		response.setAttachments(AttachmentDaoHelper.getAttachments(action.getDocumentId()));
	}
	
	@Override
	public Class<GetAttachmentsRequest> getActionType() {
		return GetAttachmentsRequest.class;
	}
}
