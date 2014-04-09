package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDocumentResult;

public class GetDocumentRequest extends BaseRequest<GetDocumentResult> {

	private Long documentId;
	private Long taskId;

	@SuppressWarnings("unused")
	private GetDocumentRequest() {
		// For serialization only
	}

	public GetDocumentRequest(Long documentId, Long taskId) {
		this.documentId = documentId;
		this.taskId = taskId;
	}
	
	public BaseResponse createDefaultActionResponse() {
		return new GetDocumentResult();
	}

	public Long getDocumentId() {
		return documentId;
	}

	public Long getTaskId() {
		return taskId;
	};
}