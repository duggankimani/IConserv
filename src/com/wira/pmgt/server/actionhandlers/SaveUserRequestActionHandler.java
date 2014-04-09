package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.requests.SaveUserRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveUserResponse;

public class SaveUserRequestActionHandler extends
		BaseActionHandler<SaveUserRequest, SaveUserResponse> {

	@Inject
	public SaveUserRequestActionHandler() {
	}

	@Override
	public void execute(SaveUserRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		HTUser user = action.getUser();
		
		if(!action.isDelete()){
			user = LoginHelper.get().createUser(user);
			SaveUserResponse result = (SaveUserResponse)actionResult;
			result.setUser(user);
		}
		
		
		if(action.isDelete()){
			LoginHelper.get().deleteUser(user);
		}
		
	}

	@Override
	public Class<SaveUserRequest> getActionType() {
		return SaveUserRequest.class;
	}
}
