package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetCommentsResponse;

import java.lang.Long;

public class GetCommentsRequest extends BaseRequest<GetCommentsResponse> {

	private Long documentId;

	@SuppressWarnings("unused")
	private GetCommentsRequest() {
		// For serialization only
	}

	public GetCommentsRequest(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetCommentsResponse();
	}
}
