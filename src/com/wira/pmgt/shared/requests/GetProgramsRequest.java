package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;

import java.lang.Long;

public class GetProgramsRequest extends BaseRequest<GetProgramsResponse> {

	private Long id;
	ProgramDetailType type;
	boolean loadChildren;
	
	public GetProgramsRequest() {
	}

	public GetProgramsRequest(Long id, boolean loadChildren) {
		this.id = id;
		this.loadChildren = loadChildren;
	}
	
	public GetProgramsRequest(ProgramDetailType type, boolean loadChildren){
		this.type = type;
		this.loadChildren= loadChildren;
	}

	public Long getId() {
		return id;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProgramsResponse();
	}
	
	public boolean isLoadChildren(){
		return loadChildren;
	}
	
	public ProgramDetailType getType(){
		return type;
	}
}
