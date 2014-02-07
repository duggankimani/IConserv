package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Notification;

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