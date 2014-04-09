package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.DocumentLine;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.DeleteLineResponse;

public class DeleteLineRequest extends BaseRequest<DeleteLineResponse> {

	private DocumentLine line;

	@SuppressWarnings("unused")
	private DeleteLineRequest() {
		// For serialization only
	}

	public DeleteLineRequest(DocumentLine line) {
		this.line = line;
	}

	public DocumentLine getLine() {
		return line;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new DeleteLineResponse();
	}
}
