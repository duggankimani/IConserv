package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteAttachmentResponse;

import java.lang.Long;

public class DeleteAttachmentRequest extends
		BaseRequest<DeleteAttachmentResponse> {

	private Long attachmentId;

	@SuppressWarnings("unused")
	private DeleteAttachmentRequest() {
		// For serialization only
	}

	public DeleteAttachmentRequest(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Long getAttachmentId() {
		return attachmentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new DeleteAttachmentResponse();
	}
}
