package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.requests.UpdatePasswordRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveUserResponse;
import com.wira.pmgt.shared.responses.UpdatePasswordResponse;

public class UpdatePasswordRequestActionHandler extends 
		BaseActionHandler<UpdatePasswordRequest, UpdatePasswordResponse> {

	@Inject
	public UpdatePasswordRequestActionHandler() {
	}


	@Override
	public void execute(UpdatePasswordRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		String username = action.getUsername();
		String password = action.getPassword();
		boolean success= LoginHelper.get().updatePassword(username, password);
		
		UpdatePasswordResponse response = (UpdatePasswordResponse)actionResult;
	}

	@Override
	public Class<UpdatePasswordRequest> getActionType() {
		return UpdatePasswordRequest.class;
	}
}
