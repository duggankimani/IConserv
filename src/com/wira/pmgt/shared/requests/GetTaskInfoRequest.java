package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetTaskInfoResponse;

import java.lang.Long;

public class GetTaskInfoRequest extends BaseRequest<GetTaskInfoResponse> {

	private Long programId;

	@SuppressWarnings("unused")
	private GetTaskInfoRequest() {
	}

	public GetTaskInfoRequest(Long programId) {
		this.programId = programId;
	}

	public Long getProgramId() {
		return programId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetTaskInfoResponse();
	}
}
