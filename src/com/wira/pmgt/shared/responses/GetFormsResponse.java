package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.form.Form;

import java.util.List;

public class GetFormsResponse extends BaseResponse {

	private List<Form> forms;

	public GetFormsResponse() {
	}

	public GetFormsResponse(List<Form> forms) {
		this.forms = forms;
	}

	public List<Form> getForms() {
		return forms;
	}

	public void setForms(List<Form> forms) {
		this.forms = forms;
	}
}
