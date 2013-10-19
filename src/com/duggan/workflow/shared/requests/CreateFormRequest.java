package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.CreateFormResponse;
import com.duggan.workflow.shared.model.form.Form;

public class CreateFormRequest extends BaseRequest<CreateFormResponse> {

	private Form form;

	@SuppressWarnings("unused")
	private CreateFormRequest() {
		// For serialization only
	}

	public CreateFormRequest(Form form) {
		this.form = form;
	}

	public Form getForm() {
		return form;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new CreateFormResponse();
	}
}