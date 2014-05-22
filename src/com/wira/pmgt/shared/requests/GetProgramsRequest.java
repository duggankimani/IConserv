package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;

import java.lang.Long;

public class GetProgramsRequest extends BaseRequest<GetProgramsResponse> {

	private Long id;
	private String code;
	private Long periodId;
	ProgramDetailType type;
	boolean loadChildren;
	boolean loadObjectives;
	
	public GetProgramsRequest() {
	}

	public GetProgramsRequest(Long id, boolean loadChildren) {
		this.id = id;
		this.loadChildren = loadChildren;
	}
	public GetProgramsRequest(Long id, boolean loadChildren, boolean loadObjectives){
		this(id,loadChildren);
		this.loadObjectives=loadObjectives;
	}
	
	public GetProgramsRequest(ProgramDetailType type, boolean loadChildren){
		this.type = type;
		this.loadChildren= loadChildren;
	}
	public GetProgramsRequest(ProgramDetailType type, boolean loadChildren, boolean loadObjectives){
		this(type,loadChildren);
		this.loadObjectives = loadObjectives;
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

	public boolean isLoadObjectives() {
		return loadObjectives;
	}

	public void setLoadObjectives(boolean loadObjectives) {
		this.loadObjectives = loadObjectives;
	}

	public Long getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Long periodId) {
		this.periodId = periodId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
