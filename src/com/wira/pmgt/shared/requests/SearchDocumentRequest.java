package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SearchDocumentRequestResult;

import java.lang.String;
import java.lang.Long;

public class SearchDocumentRequest extends
		BaseRequest<SearchDocumentRequestResult> {

	private String subject;
	private Long documentId;

	@SuppressWarnings("unused")
	private SearchDocumentRequest() {
		// For serialization only
	}

	public SearchDocumentRequest(String subject, Long documentId) {
		this.subject = subject;
		this.documentId = documentId;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SearchDocumentRequestResult();
	}
	
	public String getSubject() {
		return subject;
	}

	public Long getDocumentId() {
		return documentId;
	}
}
