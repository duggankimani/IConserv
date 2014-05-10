package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.AssignTaskResponse;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.model.TaskInfo;

public class AssignTaskRequest extends BaseRequest<AssignTaskResponse> {

	private TaskInfo taskInfo;

	@SuppressWarnings("unused")
	private AssignTaskRequest() {
	}

	public AssignTaskRequest(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public TaskInfo getTaskInfo() {
		return taskInfo;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new AssignTaskResponse();
	}
}
