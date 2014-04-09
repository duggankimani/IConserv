package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.dao.helper.NotificationDaoHelper;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.requests.SearchDocumentRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SearchDocumentRequestResult;

public class SearchDocumentRequestActionHandler extends
		BaseActionHandler<SearchDocumentRequest, SearchDocumentRequestResult> {

	@Inject
	public SearchDocumentRequestActionHandler() {
	}

	@Override
	public void execute(SearchDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		SearchDocumentRequestResult result = (SearchDocumentRequestResult)actionResult;
		
		List<Document> notes = DocumentDaoHelper.search(action.getSubject());
		
		result.setDocument(notes);
		
	}
	
	@Override
	public Class<SearchDocumentRequest> getActionType() {
		return SearchDocumentRequest.class;
	}
}
