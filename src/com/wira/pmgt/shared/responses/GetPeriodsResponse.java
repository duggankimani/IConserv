package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.program.PeriodDTO;

/**
 * 
 * @author duggan
 *
 */
public class GetPeriodsResponse extends BaseResponse {

	private List<PeriodDTO> periods;

	public GetPeriodsResponse() {
	}

	public List<PeriodDTO> getPeriods() {
		return periods;
	}

	public void setPeriods(List<PeriodDTO> periods) {
		this.periods = periods;
	}
}
