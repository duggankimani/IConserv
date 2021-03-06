package com.wira.pmgt.shared.requests;

import java.util.HashMap;
import java.util.Map;

import com.wira.pmgt.shared.model.Actions;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.ExecuteWorkflowResult;

/**
 * 
 * @author duggan
 *
 */
public class ExecuteWorkflow extends BaseRequest<ExecuteWorkflowResult> {

	private Actions action;
	private Long taskId;
	private String userId;
	private Map<String, Value> values = new HashMap<String, Value>();
	
	
	public ExecuteWorkflow(){
		
	}
	
	public ExecuteWorkflow(Long taskId, String userId, Actions action) {
		this.action = action;
		this.taskId=taskId;
		this.userId = userId;
	}

	public Actions getAction() {
		return action;
	}

	public void setAction(Actions action) {
		this.action = action;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new ExecuteWorkflowResult();
	}

	public void addValue(String name, Value value){
		values.put(name, value);
	}
	
	public void clear(){
		values.clear();
	}
	
	public Map<String, Value> getValues() {
		return values;
	}

	public void setValues(Map<String, Value> values) {
		this.values = values;
	}
}
