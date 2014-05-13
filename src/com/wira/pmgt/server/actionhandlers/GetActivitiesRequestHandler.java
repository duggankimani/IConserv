package com.wira.pmgt.server.actionhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.CommentDaoHelper;
import com.wira.pmgt.server.dao.helper.NotificationDaoHelper;
import com.wira.pmgt.shared.model.Activity;
import com.wira.pmgt.shared.model.Comment;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.model.NotificationType;
import com.wira.pmgt.shared.requests.GetActivitiesRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetActivitiesResponse;

public class GetActivitiesRequestHandler extends
		BaseActionHandler<GetActivitiesRequest, GetActivitiesResponse> {

	@Inject
	public GetActivitiesRequestHandler() {
	}

	@Override
	public void execute(GetActivitiesRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Long documentId = action.getDocumentId();
		List<Activity> activities = new ArrayList<>();		
		activities.addAll(NotificationDaoHelper.getAllNotifications(documentId,
				NotificationType.TASKCOMPLETED_OWNERNOTE,
				NotificationType.APPROVALREQUEST_OWNERNOTE,
				NotificationType.TASKASSIGNMENT_ASSIGNEENOTE,
				NotificationType.TASKDELEGATED, NotificationType.FILE_UPLOADED));
				
		activities.addAll(CommentDaoHelper.getAllCommentsByDocumentId(documentId));		
		Collections.sort(activities);
				
		Map<Activity, List<Activity>> activityMap = new LinkedHashMap<>();
		
		for(Activity activity: activities){
			if(activity instanceof Notification || !action.isCategorized()){
				activityMap.put(activity, null);
			}else{
				Comment comment = (Comment)activity;
				//System.err.println("Adding Comment >>>"+comment.getId()+" :: Parent = "+comment.getParentId());
				//check if this is a child
				
				if(comment.getParentId()==null){
					//possible parent
					activityMap.put(comment, new ArrayList<Activity>());
				}else{
					Comment parent = new Comment();
					parent.setId(comment.getParentId());
					List<Activity> children = activityMap.get(parent);
					
					if(children==null){
						//System.err.println("#############SERVER IGNORING CHILD.............");						
					}else{
						children.add(comment);//activity map loaded						
					}
					
				}
			}
		}
		
		GetActivitiesResponse response = (GetActivitiesResponse)actionResult;
		response.setActivityMap(activityMap);
		
	}
	
	@Override
	public Class<GetActivitiesRequest> getActionType() {
		return GetActivitiesRequest.class;
	}
}
