package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.dashboard.LongTask;

public class GetLongTasksResponse extends BaseResponse {

	private List<LongTask> longTasks;

	public GetLongTasksResponse() {
		// For serialization only
	}

	public List<LongTask> getLongTasks() {
		return longTasks;
	}

	public void setLongTasks(List<LongTask> longTasks) {
		this.longTasks = longTasks;
	}
}
