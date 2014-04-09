package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.ProcessDef;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveProcessResponse;

public class SaveProcessRequest extends BaseRequest<SaveProcessResponse> {

	private ProcessDef processDef;

	@SuppressWarnings("unused")
	private SaveProcessRequest() {
	}

	public SaveProcessRequest(ProcessDef processDef) {
		this.processDef = processDef;
	}

	public ProcessDef getProcessDef() {
		return processDef;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new SaveProcessResponse();
	}
}
