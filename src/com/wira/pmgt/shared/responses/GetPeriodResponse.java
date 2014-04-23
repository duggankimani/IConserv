package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.program.PeriodDTO;

public class GetPeriodResponse extends BaseResponse {

	private PeriodDTO period;

	public GetPeriodResponse() {
	}

	public PeriodDTO getPeriod() {
		return period;
	}

	public void setPeriod(PeriodDTO period) {
		this.period = period;
	}
	
}
