package com.wira.pmgt.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.wira.pmgt.shared.model.ApproverAction;
import com.wira.pmgt.shared.model.NotificationType;

@Entity
@Table(name="localnotification")
public class NotificationModel extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Long documentId;
	
	private String targetUserId;
	
	private String subject;
	
	@Column(length=5000)
	private String description;
	
	/**
	 * Document Owner
	 */
	private String owner;
	
	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;
	
	private Boolean isRead; /*Mark as read/unread*/
	
	@Enumerated(EnumType.STRING)
	private ApproverAction approverAction;
	
	private String fileName;
	
	private Long fileId;
	
	public NotificationModel() {
		isRead=true;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public Boolean IsRead() {
		return isRead;
	}

	public void setRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public ApproverAction getApproverAction() {
		return approverAction;
	}

	public void setApproverAction(ApproverAction approverAction) {
		this.approverAction = approverAction;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
