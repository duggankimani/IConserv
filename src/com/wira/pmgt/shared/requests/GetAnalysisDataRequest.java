package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetAnalysisDataResponse;

public class GetAnalysisDataRequest extends
		BaseRequest<GetAnalysisDataResponse> {

	private Long periodId;

	@SuppressWarnings("unused")
	private GetAnalysisDataRequest() {
		// For serialization only
	}

	public GetAnalysisDataRequest(Long periodId) {
		this.periodId= periodId;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetAnalysisDataResponse();
	}

	public Long getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Long periodId) {
		this.periodId = periodId;
	}
}
