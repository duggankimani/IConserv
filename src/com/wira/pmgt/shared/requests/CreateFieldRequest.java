package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.form.Field;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateFieldResponse;

public class CreateFieldRequest extends BaseRequest<CreateFieldResponse> {

	private Field field;

	@SuppressWarnings("unused")
	private CreateFieldRequest() {
		// For serialization only
	}

	public CreateFieldRequest(Field field) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new CreateFieldResponse();
	}
}
