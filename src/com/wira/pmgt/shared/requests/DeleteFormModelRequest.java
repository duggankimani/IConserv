package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.form.FormModel;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteFormModelResponse;

public class DeleteFormModelRequest extends
		BaseRequest<DeleteFormModelResponse> {

	private FormModel model;

	public DeleteFormModelRequest() {
		// For serialization only
	}

	public DeleteFormModelRequest(FormModel model) {
		this.model = model;
	}

	public FormModel getModel() {
		return model;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new DeleteFormModelResponse();
	}
}
