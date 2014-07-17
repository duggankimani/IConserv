package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;

import java.lang.Long;

public class GetProgramsRequest extends BaseRequest<GetProgramsResponse> {

	/**
	 * Id of the program to load
	 */
	private Long id;
	
	//if null, use id
	private String code;
	
	//if null, assume current period
	private Long periodId;
	
	private ProgramDetailType type;
	private boolean loadChildren;

	/**
	 * Program; Outcome set for searching activities
	 * This is necessitated but the fact that outcomes do not have a direct (parent-child) relationship with activities 
	 */
	private Long programId,outcomeId;
	
	/**
	 * Return all Programs
	 */
	public GetProgramsRequest() {
	}

	/**
	 * Load Program by Id, and optionally load all programsdetails/Activities under the retrieved program
	 * @param id
	 * @param loadChildren
	 */
	public GetProgramsRequest(Long id, boolean loadChildren) {
		this.id = id;
		this.loadChildren = loadChildren;
	}
	
	/**
	 * Load Program based on code; Used to load programs of the same type but in different periods  
	 * 
	 * <p>
	 * @param code
	 * @param periodId
	 * @param loadChildren
	 */
	public GetProgramsRequest(String code,Long periodId, boolean loadChildren) {
		this.code = code;
		this.periodId = periodId;
		this.loadChildren = loadChildren;
	}
	
	/**
	 * Load all Programs of a given type;
	 * <p>
	 * @param type {@link ProgramDetailType}
	 * @param loadChildren Whether to load details of the programs returned (Details are programs whose parentId is the retrived program)
	 */
	public GetProgramsRequest(ProgramDetailType type, boolean loadChildren){
		this.type = type;
		this.loadChildren= loadChildren;
	}
	
	/**
	 * Search for all activities under the programId & linked to outcomeId 
	 * <p>
	 * @param programId Parent Program Id for the activities to be retrieved
	 * @param outcomeId Outcome Id of the activities
	 * @param loadChildren Load details of the children
	 */
	public GetProgramsRequest(Long programId, Long outcomeId, boolean loadChildren){
		this.programId = programId;
		this.outcomeId = outcomeId;
		this.loadChildren = loadChildren;
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

	public Long getOutcomeId() {
		return outcomeId;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

}
