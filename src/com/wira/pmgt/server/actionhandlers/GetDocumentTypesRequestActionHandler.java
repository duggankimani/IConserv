package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.shared.requests.GetDocumentTypesRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDocumentTypesResponse;

public class GetDocumentTypesRequestActionHandler extends
		BaseActionHandler<GetDocumentTypesRequest, GetDocumentTypesResponse> {

	@Inject
	public GetDocumentTypesRequestActionHandler() {
	}

	@Override
	public void execute(GetDocumentTypesRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetDocumentTypesResponse response = (GetDocumentTypesResponse)actionResult;
		response.setDocumentTypes(DocumentDaoHelper.getDocumentTypes());
	}

	@Override
	public Class<GetDocumentTypesRequest> getActionType() {
		return GetDocumentTypesRequest.class;
	}
}
