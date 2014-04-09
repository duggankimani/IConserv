package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.requests.DeleteDocumentRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteDocumentResponse;

public class DeleteDocumentRequestHandler extends
		BaseActionHandler<DeleteDocumentRequest, DeleteDocumentResponse> {

	@Inject
	public DeleteDocumentRequestHandler() {
	}

	@Override
	public void execute(DeleteDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		Long documentId = action.getDocumentId();
		assert documentId!=null;
		boolean deleted = DB.getDocumentDao().deleteDocument(documentId);
		
		DeleteDocumentResponse response = (DeleteDocumentResponse)actionResult;
		response.setDelete(deleted);
	}

	@Override
	public Class<DeleteDocumentRequest> getActionType() {
		return DeleteDocumentRequest.class;
	}
}
