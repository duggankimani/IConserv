package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.requests.CheckPasswordRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CheckPasswordRequestResult;

public class CheckPasswordRequestActionHandler extends
		BaseActionHandler<CheckPasswordRequest, CheckPasswordRequestResult> {

	@Inject
	public CheckPasswordRequestActionHandler() {
	}

	@Override
	public void execute(CheckPasswordRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		String userId = action.getUserId();
		String password = action.getPassword();
		
		boolean isValid = LoginHelper.get().login(userId, password);
		CheckPasswordRequestResult result = (CheckPasswordRequestResult)actionResult;

		result.setIsValid(isValid);
		
	}

	@Override
	public Class<CheckPasswordRequest> getActionType() {
		return CheckPasswordRequest.class;
	}
}
