package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreatePeriodResponse;
import com.wira.pmgt.shared.model.program.PeriodDTO;

public class CreatePeriodRequest extends BaseRequest<CreatePeriodResponse> {

	private PeriodDTO period;

	public CreatePeriodRequest() {
	}
	
	public CreatePeriodRequest(PeriodDTO period) {
		this.period = period;
	}

	public PeriodDTO getPeriod() {
		return period;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new CreatePeriodResponse();
	}
}
