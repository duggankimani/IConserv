package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetContextRequestResult;

public class GetContextRequest extends BaseRequest<GetContextRequestResult> {

	public GetContextRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetContextRequestResult();
	}
}
