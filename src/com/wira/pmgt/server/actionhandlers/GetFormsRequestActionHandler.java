package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.FormDaoHelper;
import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.requests.GetFormsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetFormsResponse;

public class GetFormsRequestActionHandler extends
		BaseActionHandler<GetFormsRequest, GetFormsResponse> {

	@Inject
	public GetFormsRequestActionHandler() {
	}

	@Override
	public void execute(GetFormsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		
		List<Form> forms = FormDaoHelper.getForms();
		
		GetFormsResponse response = (GetFormsResponse)actionResult;
		
		response.setForms(forms);
		
	}

	@Override
	public Class<GetFormsRequest> getActionType() {
		return GetFormsRequest.class;
	}
}
