package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetTaskInfoResponse;

import java.lang.Long;

public class GetTaskInfoRequest extends BaseRequest<GetTaskInfoResponse> {

	private Long activityId;

	@SuppressWarnings("unused")
	private GetTaskInfoRequest() {
		// For serialization only
	}

	public GetTaskInfoRequest(Long activityId) {
		this.activityId = activityId;
	}

	public Long getActivityId() {
		return activityId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetTaskInfoResponse();
	}
}
