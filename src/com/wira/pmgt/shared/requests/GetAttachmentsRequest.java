package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetAttachmentsResponse;

import java.lang.Long;

public class GetAttachmentsRequest extends
		BaseRequest<GetAttachmentsResponse> {

	private Long documentId;

	@SuppressWarnings("unused")
	private GetAttachmentsRequest() {
		// For serialization only
	}

	public GetAttachmentsRequest(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetAttachmentsResponse();
	}
}
