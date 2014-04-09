package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.ExportFormResponse;

import java.lang.Long;

public class ExportFormRequest extends BaseRequest<ExportFormResponse> {

	private Long formId;

	@SuppressWarnings("unused")
	private ExportFormRequest() {
		// For serialization only
	}

	public ExportFormRequest(Long formId) {
		this.formId = formId;
	}

	public Long getFormId() {
		return formId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		// TODO Auto-generated method stub
		return new ExportFormResponse();
	}
}
