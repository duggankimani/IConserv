package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramsTreeResponse;

public class GetProgramsTreeRequest extends BaseRequest<GetProgramsTreeResponse> {

	private Long programId;
	private Long periodId;

	public GetProgramsTreeRequest() {
	}
	
	public GetProgramsTreeRequest(Long programId, Long periodId) {
		this.programId = programId;
		this.periodId = periodId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProgramsTreeResponse();
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Long getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Long periodId) {
		this.periodId = periodId;
	}
}
