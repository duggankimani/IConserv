package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.requests.GetPermissionsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetPermissionsResponse;

public class GetPermissionsRequestActionHandler extends
		BaseActionHandler<GetPermissionsRequest, GetPermissionsResponse> {

	@Inject
	public GetPermissionsRequestActionHandler() {
	}


	@Override
	public void execute(GetPermissionsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		ProgramDaoHelper.getUserPermissions(action.getUserId(), action.getPeriodId());
	}

	
	@Override
	public Class<GetPermissionsRequest> getActionType() {
		
		return GetPermissionsRequest.class;
	}

}
