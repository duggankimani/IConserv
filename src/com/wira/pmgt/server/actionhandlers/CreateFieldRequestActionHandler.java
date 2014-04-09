package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.FormDaoHelper;
import com.wira.pmgt.shared.model.form.Field;
import com.wira.pmgt.shared.requests.CreateFieldRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateFieldResponse;

public class CreateFieldRequestActionHandler extends
		BaseActionHandler<CreateFieldRequest, CreateFieldResponse> {

	@Inject
	public CreateFieldRequestActionHandler() {
	}

	@Override
	public void execute(CreateFieldRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Field field = action.getField();
		
		Field rtnfield = FormDaoHelper.createField(field);
		
		CreateFieldResponse response = (CreateFieldResponse)actionResult;
		
		response.setField(rtnfield);
	}

	@Override
	public Class<CreateFieldRequest> getActionType() {
		return CreateFieldRequest.class;
	}
}
