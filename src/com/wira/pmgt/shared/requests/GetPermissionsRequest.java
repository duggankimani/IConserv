package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.responses.ApprovalRequestResult;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPermissionsResponse;

public class GetPermissionsRequest extends BaseRequest<GetPermissionsResponse> {

	private String userId;
	private Long periodId;

	@SuppressWarnings("unused")
	private GetPermissionsRequest() {
	}

	public GetPermissionsRequest(String userId, Long periodId) {
		this.userId = userId;
		this.periodId = periodId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new ApprovalRequestResult(false);
	}

	public String getUserId() {
		return userId;
	}

	public Long getPeriodId() {
		return periodId;
	}
	
}
