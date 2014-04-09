package com.wira.pmgt.server.report;

import java.util.List;

import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.Notification;

public class DocumentReport {

	public DocumentReport() {
	}
	
	Document document;
	List<Notification> notifications;

	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
}
