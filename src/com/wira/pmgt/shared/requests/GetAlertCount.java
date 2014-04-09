package com.wira.pmgt.shared.requests;

import java.util.HashMap;

import com.wira.pmgt.client.model.TaskType;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.requests.GetAlertCountResult;
import com.wira.pmgt.shared.responses.BaseResponse;

public class GetAlertCount extends BaseRequest<GetAlertCountResult> {

	public GetAlertCount() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetAlertCountResult(new HashMap<TaskType, Integer>());
	}
}
