package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.NotificationDaoHelper;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.requests.GetNotificationsAction;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetNotificationsActionResult;

public class GetNotificationsActionHandler extends
		BaseActionHandler<GetNotificationsAction, GetNotificationsActionResult> {

	@Inject
	public GetNotificationsActionHandler() {
	}
	
	@Override
	public void execute(GetNotificationsAction action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		List<Notification> notifications = NotificationDaoHelper.getAllNotifications(action.getUserId());
		
		GetNotificationsActionResult result = (GetNotificationsActionResult)actionResult;
		result.setNotifications(notifications);
		
	}
	
	@Override
	public Class<GetNotificationsAction> getActionType() {
		return GetNotificationsAction.class;
	}
}
