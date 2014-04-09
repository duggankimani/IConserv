package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.shared.model.Doc;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.requests.GetDocumentRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDocumentResult;

/**
 * 
 * @author duggan
 *
 */
public class GetDocumentRequestHandler extends
		BaseActionHandler<GetDocumentRequest, GetDocumentResult> {

	@Inject
	public GetDocumentRequestHandler() {
	}

	@Override
	public void execute(GetDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Doc doc = null;
		if(action.getTaskId()==null){
			doc = DocumentDaoHelper.getDocument(action.getDocumentId());
		}else{
			doc = JBPMHelper.get().getTask(action.getTaskId());
		}
		
		
		GetDocumentResult result = (GetDocumentResult)actionResult;
		
		result.setDoc(doc);		
		
	}

	@Override
	public void undo(GetDocumentRequest action, GetDocumentResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetDocumentRequest> getActionType() {
		return GetDocumentRequest.class;
	}
}
