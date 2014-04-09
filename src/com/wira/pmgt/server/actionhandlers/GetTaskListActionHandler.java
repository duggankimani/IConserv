package com.wira.pmgt.server.actionhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.client.model.TaskType;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.model.Doc;
import com.wira.pmgt.shared.model.DocStatus;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.HTSummary;
import com.wira.pmgt.shared.requests.GetTaskList;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetTaskListResult;

/**
 * 
 * @author duggan
 * 
 */
public class GetTaskListActionHandler extends
		BaseActionHandler<GetTaskList, GetTaskListResult> {

	@Inject
	public GetTaskListActionHandler() {
	}

	@Override
	public void execute(GetTaskList action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		String userId = action.getUserId()==null? SessionHelper.getCurrentUser().getUserId():
			action.getUserId();
		
		TaskType type = action.getType();

		List<Doc> summary = new ArrayList<>();
		DocStatus status = null;
		
		switch (type) {
		case DRAFT:
			status = DocStatus.DRAFTED;
			summary = DocumentDaoHelper.getAllDocuments(status);
			break;
		case APPROVED:
			status = DocStatus.APPROVED;
			summary = DocumentDaoHelper.getAllDocuments(status);
			break;
		case INPROGRESS:
			status = DocStatus.INPROGRESS;
			summary = DocumentDaoHelper.getAllDocuments(status);
			break;
		case REJECTED:
			status = DocStatus.REJECTED;
			summary = DocumentDaoHelper.getAllDocuments(status);
			break;
		case SEARCH:
			
			if(action.getFilter()!=null){				
				summary.addAll(DocumentDaoHelper.search(userId,action.getFilter()));
				summary.addAll(JBPMHelper.get().searchTasks(userId, action.getFilter()));
			}else if(action.getProcessInstanceId()!=null || action.getDocumentId()!=null){
				
				Long processInstanceId = action.getProcessInstanceId();
				
				if(processInstanceId==null || processInstanceId==0L){
					processInstanceId = DocumentDaoHelper.getProcessInstanceIdByDocumentId(action.getDocumentId());
				}
								
				Document doc = DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId);
				if(doc!=null)
					summary.add(doc);
				
				List<HTSummary> tasks = JBPMHelper.get().getTasksForUser(userId, processInstanceId);
				
				if(tasks!=null){
					summary.addAll(tasks);
				}
			}
			
			break;
			
		default:
			summary = getPendingApprovals(userId, type);
			break;
		}

		GetTaskListResult result = (GetTaskListResult) actionResult;
		
		Collections.sort(summary);
		
		result.setTasks(summary);

	}

	private List<Doc> getPendingApprovals(String userId, TaskType type) {

		List<HTSummary> tasks = new ArrayList<>();

		try {
			tasks = JBPMHelper.get().getTasksForUser(userId, type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Doc> summary = new ArrayList<>();
		for (HTSummary s : tasks) {
			summary.add(s);
		}

		return summary;
	}

	@Override
	public void undo(GetTaskList action, GetTaskListResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetTaskList> getActionType() {
		return GetTaskList.class;
	}
}
