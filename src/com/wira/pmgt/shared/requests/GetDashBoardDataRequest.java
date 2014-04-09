package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDashBoardDataResponse;

public class GetDashBoardDataRequest extends
		BaseRequest<GetDashBoardDataResponse> {

	public GetDashBoardDataRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDashBoardDataResponse();
	}
}
