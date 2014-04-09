package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.form.Form;

public class CreateFormResponse extends BaseResponse {

	private Form form;

	public CreateFormResponse() {
	}

	public CreateFormResponse(Form form) {
		this.form = form;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}
