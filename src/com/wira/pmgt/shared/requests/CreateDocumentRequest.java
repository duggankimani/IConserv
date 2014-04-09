package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateDocumentResult;

public class CreateDocumentRequest extends
		BaseRequest<CreateDocumentResult> {

	private Document document;

	@SuppressWarnings("unused")
	private CreateDocumentRequest() {
		
	}

	public CreateDocumentRequest(Document document) {
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new CreateDocumentResult();
	}
}
