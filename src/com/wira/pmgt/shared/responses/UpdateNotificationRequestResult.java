package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.Notification;

public class UpdateNotificationRequestResult extends BaseResponse {

	Notification notification;
	
	public UpdateNotificationRequestResult() {
		
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}
}
