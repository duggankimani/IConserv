package com.wira.pmgt.shared.requests;

import com.gwtplatform.dispatch.shared.Result;
import com.wira.pmgt.client.model.TaskType;
import com.wira.pmgt.shared.responses.BaseResponse;

import java.util.HashMap;

public class GetAlertCountResult extends BaseResponse {

	private HashMap<TaskType, Integer> counts;

	@SuppressWarnings("unused")
	private GetAlertCountResult() {
		// For serialization only
	}

	public GetAlertCountResult(HashMap<TaskType, Integer> counts) {
		this.counts = counts;
	}

	public HashMap<TaskType, Integer> getCounts() {
		return counts;
	}
}
