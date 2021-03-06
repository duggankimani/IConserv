package com.wira.pmgt.shared.requests;

import com.wira.pmgt.client.model.TaskType;
import com.wira.pmgt.shared.model.SearchFilter;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetTaskListResult;

import java.lang.String;

public class GetTaskList extends BaseRequest<GetTaskListResult> {

	private String userId;
	private TaskType type;
	private Long processInstanceId;
	private Long documentId;
	private SearchFilter filter;
	
	@SuppressWarnings("unused")
	private GetTaskList() {
	}

	public GetTaskList(String userId, TaskType type) {
		this.userId = userId;
		this.type = type;
		
	}

	public GetTaskList(String userId, SearchFilter filter) {
		this.userId = userId;
		this.filter = filter;
		this.type=TaskType.SEARCH;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public TaskType getType() {
		return type;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskListResult();
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public SearchFilter getFilter() {
		return filter;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

}
