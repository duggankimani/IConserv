package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.program.ProgramSummary;

public class GetProgramCalendarResponse extends BaseResponse {

	private List<ProgramSummary> summary;

	public GetProgramCalendarResponse() {
	}

	public GetProgramCalendarResponse(List<ProgramSummary> summary) {
		this.summary = summary;
	}

	public List<ProgramSummary> getSummary() {
		return summary;
	}

	public void setSummary(List<ProgramSummary> summary) {
		this.summary = summary;
	}
}
