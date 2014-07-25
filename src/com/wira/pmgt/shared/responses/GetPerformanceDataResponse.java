package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.program.PerformanceModel;

public class GetPerformanceDataResponse extends BaseResponse{

	List<PerformanceModel> data;
	
	public GetPerformanceDataResponse() {
	}

	public List<PerformanceModel> getData() {
		return data;
	}

	public void setData(List<PerformanceModel> data) {
		this.data = data;
	}

}
