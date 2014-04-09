package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.FormDaoHelper;
import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.requests.CreateFormRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateFormResponse;

public class CreateFormRequestActionHandler extends
		BaseActionHandler<CreateFormRequest, CreateFormResponse> {

	@Inject
	public CreateFormRequestActionHandler() {
	}
	
	@Override
	public void execute(CreateFormRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		Form form = action.getForm();
		form =  FormDaoHelper.createForm(form, true);
		
		CreateFormResponse response = (CreateFormResponse)actionResult;
		response.setForm(form);
	}
	
	@Override
	public Class<CreateFormRequest> getActionType() {
		return CreateFormRequest.class;
	}
}
