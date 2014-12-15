package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPeriodResponse;

public class GetPeriodRequest extends BaseRequest<GetPeriodResponse> {

	private Long periodId;

	public GetPeriodRequest() {
	}
	
	public GetPeriodRequest(Long periodId) {
		this.periodId = periodId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetPeriodResponse();
	}

	public Long getPeriodId() {
		return periodId;
	}
}
