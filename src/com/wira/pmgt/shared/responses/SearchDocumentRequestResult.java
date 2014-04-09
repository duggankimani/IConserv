package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.Document;

import java.util.List;

public class SearchDocumentRequestResult extends BaseResponse {

	private List<Document> document;

	public SearchDocumentRequestResult() {
	}

	public SearchDocumentRequestResult(List<Document> document) {
		this.document = document;
	}

	public List<Document> getDocument() {
		return document;
	}

	public void setDocument(List<Document> document) {
		this.document = document;
	}
}
