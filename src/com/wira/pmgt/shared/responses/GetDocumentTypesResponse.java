package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.DocumentType;

import java.util.List;

public class GetDocumentTypesResponse extends BaseResponse {

	private List<DocumentType> documentTypes;

	public GetDocumentTypesResponse() {
	}

	public GetDocumentTypesResponse(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}
}
