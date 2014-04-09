package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.model.Doc;
import com.wira.pmgt.shared.model.HTSummary;
import com.wira.pmgt.shared.model.HTask;
import com.wira.pmgt.shared.requests.GetItemRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetItemResult;

public class GetItemActionHandler extends
		BaseActionHandler<GetItemRequest, GetItemResult> {

	@Inject
	public GetItemActionHandler() {
	}

	@Override
	public void execute(GetItemRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {

		Doc doc=null;
		if(action.isTask()){
			HTSummary summary = JBPMHelper.get().getSummary(action.getItemId());
		}else{
			doc = DocumentDaoHelper.getDocument(action.getItemId());
		}
		
	}

	@Override
	public void undo(GetItemRequest action, GetItemResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<GetItemRequest> getActionType() {
		return GetItemRequest.class;
	}
}
