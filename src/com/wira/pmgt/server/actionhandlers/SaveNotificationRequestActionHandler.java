package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.NotificationDaoHelper;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.requests.SaveNotificationRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveNotificationResponse;

public class SaveNotificationRequestActionHandler extends
		BaseActionHandler<SaveNotificationRequest, SaveNotificationResponse> {

	@Inject
	public SaveNotificationRequestActionHandler() {
	}

	@Override
	public void execute(SaveNotificationRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Notification note = action.getNotification();
		note = NotificationDaoHelper.saveNotification(note);
		
		SaveNotificationResponse response = (SaveNotificationResponse)actionResult;
		response.setNotification(note);
	}

	@Override
	public Class<SaveNotificationRequest> getActionType() {
		return SaveNotificationRequest.class;
	}
}
