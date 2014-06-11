package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramCalendarResponse;

public class GetProgramCalendarRequest extends
		BaseRequest<GetProgramCalendarResponse> {

	public GetProgramCalendarRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProgramCalendarResponse();
	}
}
