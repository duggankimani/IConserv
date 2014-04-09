package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.Notification;

import java.util.List;

public class GetNotificationsActionResult extends BaseResponse{

	private List<Notification> notifications;

	public GetNotificationsActionResult() {
		// For serialization only
	}

	public GetNotificationsActionResult(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
}
