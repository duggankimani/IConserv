package com.wira.pmgt.server.actionhandlers;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.requests.GetUserRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetUserRequestResult;

public class GetUserRequestActionHandler extends
		BaseActionHandler<GetUserRequest, GetUserRequestResult> {

	@Inject
	public GetUserRequestActionHandler() {
	}
	
	@Override
	public void execute(GetUserRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		HTUser user = LoginHelper.getHelper().getUser(action.getUserId(), true);
		GetUserRequestResult result = (GetUserRequestResult)actionResult;
	
		result.setUser(user);
	}
	
	
	@Override
	public Class<GetUserRequest> getActionType() {
		return GetUserRequest.class;
	}
}
