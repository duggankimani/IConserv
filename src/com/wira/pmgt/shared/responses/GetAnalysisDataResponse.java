package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.program.ProgramAnalysis;

public class GetAnalysisDataResponse extends BaseResponse {

	private List<ProgramAnalysis> data;

	public GetAnalysisDataResponse() {
	}

	public GetAnalysisDataResponse(List<ProgramAnalysis> data) {
		this.data = data;
	}

	public List<ProgramAnalysis> getData() {
		return data;
	}

	public void setData(List<ProgramAnalysis> data) {
		this.data = data;
	}
}
