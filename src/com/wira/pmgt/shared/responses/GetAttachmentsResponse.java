package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.Attachment;

public class GetAttachmentsResponse extends BaseResponse {

	private List<Attachment> attachments;

	public GetAttachmentsResponse() {
	}

	public GetAttachmentsResponse(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
}
