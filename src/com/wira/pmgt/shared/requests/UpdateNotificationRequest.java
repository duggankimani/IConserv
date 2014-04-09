package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.UpdateNotificationRequestResult;

import java.lang.Long;
import java.lang.Boolean;

public class UpdateNotificationRequest extends
		BaseRequest<UpdateNotificationRequestResult> {

	private Long notificationId;
	private Boolean read;

	@SuppressWarnings("unused")
	private UpdateNotificationRequest() {
		// For serialization only
	}

	public UpdateNotificationRequest(Long notificationId, Boolean read) {
		this.notificationId = notificationId;
		this.read = read;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new UpdateNotificationRequestResult();
	}
	
	public Long getNotificationId() {
		return notificationId;
	}

	public Boolean getRead() {
		return read;
	}
}
