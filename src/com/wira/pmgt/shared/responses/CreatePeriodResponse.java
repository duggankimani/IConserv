package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.program.PeriodDTO;

public class CreatePeriodResponse extends BaseResponse {

	private PeriodDTO period;

	public CreatePeriodResponse() {
	}

	public PeriodDTO getPeriod() {
		return period;
	}

	public void setPeriod(PeriodDTO period) {
		this.period = period;
	}
}
