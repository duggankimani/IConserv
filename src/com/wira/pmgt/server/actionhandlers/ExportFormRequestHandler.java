package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.FormDaoHelper;
import com.wira.pmgt.shared.requests.ExportFormRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.ExportFormResponse;

public class ExportFormRequestHandler extends
		BaseActionHandler<ExportFormRequest, ExportFormResponse> {

	@Inject
	public ExportFormRequestHandler() {
	}

	@Override
	public void execute(ExportFormRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		Long formId = action.getFormId();
		String xml = FormDaoHelper.exportForm(formId);
		
		ExportFormResponse response = (ExportFormResponse)actionResult;
		response.setXml(xml);
	}

	@Override
	public Class<ExportFormRequest> getActionType() {
		return ExportFormRequest.class;
	}
}
