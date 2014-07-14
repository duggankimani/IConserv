package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteProgramResponse;

import java.lang.Long;

public class DeleteProgramRequest extends
		BaseRequest<DeleteProgramResponse> {

	private Long programId;

	public DeleteProgramRequest() {
	}

	public DeleteProgramRequest(Long programId) {
		this.programId = programId;
	}

	public Long getProgramId() {
		return programId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		// TODO Auto-generated method stub
		return new DeleteProgramResponse();
	}
}
