package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.Document;

public class CreateDocumentResult extends BaseResponse {

	private Document document;

	@SuppressWarnings("unused")
	public CreateDocumentResult() {

	}

	public CreateDocumentResult(Document document) {
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
