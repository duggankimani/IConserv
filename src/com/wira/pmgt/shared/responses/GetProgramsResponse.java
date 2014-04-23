package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class GetProgramsResponse extends BaseResponse {

	private List<IsProgramActivity> programs;

	public GetProgramsResponse() {
	}

	public List<IsProgramActivity> getPrograms() {
		return programs;
	}

	public void setPrograms(List<IsProgramActivity> programs) {
		this.programs = programs;
	}
	
	public IsProgramActivity getSingleResult(){
		if(programs==null || programs.isEmpty()){
			return null;
		}
		
		return programs.get(0);
	}
}
