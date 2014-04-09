package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveCommentResponse;
import com.wira.pmgt.shared.responses.SaveNotificationResponse;

public class SaveNotificationRequest extends BaseRequest<SaveNotificationResponse> {

	private Notification notification;

	@SuppressWarnings("unused")
	private SaveNotificationRequest() {
		// For serialization only
	}

	public SaveNotificationRequest(Notification notification) {
		this.notification = notification;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveNotificationResponse();
	}

	public Notification getNotification() {
		return notification;
	}
}
