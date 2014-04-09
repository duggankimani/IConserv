package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateFormResponse;

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
