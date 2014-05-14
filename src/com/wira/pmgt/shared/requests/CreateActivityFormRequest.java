package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateActivityFormResponse;

import java.lang.Long;

public class CreateActivityFormRequest extends
		BaseRequest<CreateActivityFormResponse> {

	private Long activityId;

	@SuppressWarnings("unused")
	private CreateActivityFormRequest() {
		// For serialization only
	}

	public CreateActivityFormRequest(Long activityId) {
		this.activityId = activityId;
	}

	public Long getActivityId() {
		return activityId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new CreateActivityFormResponse();
	}
}
