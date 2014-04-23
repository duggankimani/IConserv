package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.program.ProgramDTO;

public class CreateProgramResponse extends BaseResponse {

	private ProgramDTO program;

	public CreateProgramResponse() {
		// For serialization only
	}
	
	public ProgramDTO getProgram() {
		return program;
	}

	public void setProgram(ProgramDTO program) {
		this.program = program;
	}

}
