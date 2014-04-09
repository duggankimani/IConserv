package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.Notification;

public class SaveNotificationResponse extends BaseResponse {

	private Notification notification;

	public SaveNotificationResponse() {
	}

	public SaveNotificationResponse(Notification notification) {
		this.notification = notification;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}
}
