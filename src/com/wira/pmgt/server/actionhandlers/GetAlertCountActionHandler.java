package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.dao.helper.NotificationDaoHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.requests.GetAlertCount;
import com.wira.pmgt.shared.requests.GetAlertCountResult;
import com.wira.pmgt.shared.responses.BaseResponse;

public class GetAlertCountActionHandler extends
		BaseActionHandler<GetAlertCount, GetAlertCountResult> {

	@Inject
	public GetAlertCountActionHandler() {
	}

	@Override
	public void execute(GetAlertCount action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		GetAlertCountResult countResult = (GetAlertCountResult)actionResult;
		JBPMHelper.get().getCount(SessionHelper.getCurrentUser().getUserId(), countResult.getCounts());
		DocumentDaoHelper.getCounts(countResult.getCounts());
		NotificationDaoHelper.getCounts(countResult.getCounts());
	}
	
	@Override
	public Class<GetAlertCount> getActionType() {

		return GetAlertCount.class;
	}
	
}
