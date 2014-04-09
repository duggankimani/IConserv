package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProcessResponse;

import java.lang.Long;

public class GetProcessRequest extends BaseRequest<GetProcessResponse> {

	private Long processDefId;

	@SuppressWarnings("unused")
	private GetProcessRequest() {
		// For serialization only
	}

	public GetProcessRequest(Long processDefId) {
		this.processDefId = processDefId;
	}

	public Long getProcessDefId() {
		return processDefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessResponse();
	}
}
