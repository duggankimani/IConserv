package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.program.ProgramTreeModel;

public class GetProgramsTreeResponse extends BaseResponse {

	private List<ProgramTreeModel> models = null;
	
	public GetProgramsTreeResponse(){
		
	}

	public List<ProgramTreeModel> getModels() {
		return models;
	}

	public void setModels(List<ProgramTreeModel> models) {
		this.models = models;
	}
}
