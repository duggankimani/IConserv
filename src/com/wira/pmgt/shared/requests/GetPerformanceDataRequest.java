package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.program.Metric;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPerformanceDataResponse;

public class GetPerformanceDataRequest extends BaseRequest<GetPerformanceDataResponse> {
	
	private Metric metric;
	private Long periodId;
	
	@SuppressWarnings("unused")
	private GetPerformanceDataRequest() {
	}
	
	public GetPerformanceDataRequest(Metric metric) {
		this.metric = metric;
	}
	
	public GetPerformanceDataRequest(Metric metric, Long periodId) {
		this.metric = metric;
		this.periodId = periodId;
	}

	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetPerformanceDataResponse();
	}

	public Metric getMetric() {
		return metric;
	}

	public Long getPeriodId() {
		return periodId;
	}

}
