package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateProgramResponse;
import com.wira.pmgt.shared.model.program.IsProgramDetail;

public class CreateProgramRequest extends
		BaseRequest<CreateProgramResponse> {

	private IsProgramDetail program;

	@SuppressWarnings("unused")
	private CreateProgramRequest() {
		// For serialization only
	}

	public CreateProgramRequest(IsProgramDetail program) {
		this.program = program;
	}

	public IsProgramDetail getProgram() {
		return program;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new CreateProgramResponse();
	}
}
