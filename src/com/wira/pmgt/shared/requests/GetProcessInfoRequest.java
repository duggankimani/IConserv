package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProcessInfoResponse;

import java.lang.Long;

public class GetProcessInfoRequest extends BaseRequest<GetProcessInfoResponse> {

	private Long activityId;

	@SuppressWarnings("unused")
	private GetProcessInfoRequest() {
		// For serialization only
	}

	public GetProcessInfoRequest(Long activityId) {
		this.activityId = activityId;
	}

	public Long getActivityId() {
		return activityId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessInfoResponse();
	}
}
