package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.requests.GetUsersRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetUsersResponse;

public class GetUsersRequestActionHandler extends
		BaseActionHandler<GetUsersRequest, GetUsersResponse> {

	@Inject
	public GetUsersRequestActionHandler() {
	}
	
	@Override
	public void execute(GetUsersRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetUsersResponse response = (GetUsersResponse)actionResult;
		
		response.setUsers(LoginHelper.get().getAllUsers());
	}
	
	@Override
	public Class<GetUsersRequest> getActionType() {
		return GetUsersRequest.class;
	}
}
