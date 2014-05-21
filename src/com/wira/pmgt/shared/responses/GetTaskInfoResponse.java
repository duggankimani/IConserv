package com.wira.pmgt.shared.responses;

import java.lang.Long;

public class GetTaskInfoResponse extends BaseResponse {

	private Long documentId;
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	private Long processInstanceId;

	public GetTaskInfoResponse() {
	}

	public GetTaskInfoResponse(Long documentId, Long processInstanceId) {
		this.documentId = documentId;
		this.processInstanceId = processInstanceId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}
	
}
