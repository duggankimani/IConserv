package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.program.Metric;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPerformanceDataResponse;

public class GetPerformanceDataRequest extends BaseRequest<GetPerformanceDataResponse> {
	
	private Metric metric;
	
	@SuppressWarnings("unused")
	private GetPerformanceDataRequest() {
	}
	
	public GetPerformanceDataRequest(Metric metric) {
		this.metric = metric;
	}

	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetPerformanceDataResponse();
	}

	public Metric getMetric() {
		return metric;
	}

}
