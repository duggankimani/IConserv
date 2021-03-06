package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetErrorRequestResult;

/**
 * This is the Request/Command/Action Object for retrieving
 * Errors from the database  
 * 
 * @author duggan
 *
 */
public class GetErrorRequest extends BaseRequest<GetErrorRequestResult> {

	private Long errorId;

	@SuppressWarnings("unused")
	private GetErrorRequest() {
		// For serialization only
	}

	public GetErrorRequest(Long errorId) {
		this.errorId = errorId;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetErrorRequestResult();
	}
	
	public Long getErrorId() {
		return errorId;
	}
}
