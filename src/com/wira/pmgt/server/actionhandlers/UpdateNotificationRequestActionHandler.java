package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.NotificationDaoHelper;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.requests.UpdateNotificationRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.UpdateNotificationRequestResult;

public class UpdateNotificationRequestActionHandler extends
		BaseActionHandler<UpdateNotificationRequest, UpdateNotificationRequestResult> {

	@Inject
	public UpdateNotificationRequestActionHandler() {
	}
	
	@Override
	public void execute(UpdateNotificationRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Long noteId = action.getNotificationId();
		Boolean isRead = action.getRead();
		
		NotificationDaoHelper.updateNotification(noteId, isRead);
		
		Notification notification = NotificationDaoHelper.getNotification(noteId);
		UpdateNotificationRequestResult result = (UpdateNotificationRequestResult) actionResult;
		result.setNotification(notification);
				
	}

	@Override
	public Class<UpdateNotificationRequest> getActionType() {
		return UpdateNotificationRequest.class;
	}
}
