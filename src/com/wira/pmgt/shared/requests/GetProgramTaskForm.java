package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramTaskFormResponse;

import java.lang.Long;

public class GetProgramTaskForm extends BaseRequest<GetProgramTaskFormResponse> {

	private Long programId;

	@SuppressWarnings("unused")
	private GetProgramTaskForm() {
		// For serialization only
	}

	public GetProgramTaskForm(Long programId) {
		this.programId = programId;
	}

	public Long getProgramId() {
		return programId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProgramTaskFormResponse();
	}
	
}
