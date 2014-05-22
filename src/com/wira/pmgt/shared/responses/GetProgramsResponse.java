package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.program.IsProgramDetail;

public class GetProgramsResponse extends BaseResponse {

	private List<IsProgramDetail> programs;

	public GetProgramsResponse() {
	}

	public List<IsProgramDetail> getPrograms() {
		return programs;
	}

	public void setPrograms(List<IsProgramDetail> programs) {
		this.programs = programs;
	}
	
	public IsProgramDetail getSingleResult(){
		if(programs==null || programs.isEmpty()){
			return null;
		}
		
		return programs.get(0);
	}
}
