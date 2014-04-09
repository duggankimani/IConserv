package com.wira.pmgt.server.actionhandlers;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.task.Task;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.shared.model.BooleanValue;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.ProcessMappings;
import com.wira.pmgt.shared.requests.ExecuteWorkflow;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.ExecuteWorkflowResult;

public class ExecuteWorkflowActionHandler extends
		BaseActionHandler<ExecuteWorkflow, ExecuteWorkflowResult> {

	@Inject
	public ExecuteWorkflowActionHandler() {
	}

	@Override
	public void execute(ExecuteWorkflow action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Map<String, Object> vals = new HashMap<>();
		
		Map<String, Value> values = action.getValues();
		if(values!=null){
			long processInstanceId=0L;
			Task task = JBPMHelper.get().getTaskClient().getTask(action.getTaskId());
			processInstanceId = task.getTaskData().getProcessInstanceId();
						
			Document document = DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId,false);
			assert document!=null;
			
			ProcessMappings mappings = JBPMHelper.get().getProcessDataMappings(action.getTaskId());
			
			for(String key: values.keySet()){
				Value value = values.get(key);				
				vals.put(key, value==null?null: value.getValue());
				if(key!=null){
					key = mappings.getOutputName(key);
					document.setValue(key,value);
				}
			}
			vals.put("documentOut", document);
		}
		
		
		JBPMHelper.get().execute(action.getTaskId(),action.getUserId(), action.getAction(), vals);
		
		ExecuteWorkflowResult result = (ExecuteWorkflowResult)actionResult;		
		result.setDocument(JBPMHelper.get().getSummary(action.getTaskId()));
	}

	@Override
	public void undo(ExecuteWorkflow action, ExecuteWorkflowResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<ExecuteWorkflow> getActionType() {
		return ExecuteWorkflow.class;
	}
}
