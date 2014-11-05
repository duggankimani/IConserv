package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.GetFundsResponse;

public class GetFundsRequest extends BaseRequest<GetFundsResponse> {

	/**
	 * Get Funds available to an activity/task (Limited to the funds available to the parent activity)
	 */
	private Long parentId;
	
	public GetFundsRequest() {
	}
	
	public GetFundsRequest(Long parentId) {
		this.parentId = parentId;
	}
	
	public com.wira.pmgt.shared.responses.BaseResponse createDefaultActionResponse() {
		return new GetFundsResponse();
	}

	public Long getParentId() {
		return parentId;
	};
}
