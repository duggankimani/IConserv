package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.TaskInfo;

public class GetTaskInfoResponse extends BaseResponse{

	private TaskInfo taskInfo;

	public GetTaskInfoResponse() {
	}

	public GetTaskInfoResponse(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public TaskInfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}
}
