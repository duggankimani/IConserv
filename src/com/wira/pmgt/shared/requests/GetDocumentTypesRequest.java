package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDocumentTypesResponse;

public class GetDocumentTypesRequest extends
		BaseRequest<GetDocumentTypesResponse> {

	public GetDocumentTypesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {

		return new GetDocumentTypesResponse();
	}
}
