package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.requests.SaveGroupRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveGroupResponse;

public class SaveGroupRequestActionHandler extends
		BaseActionHandler<SaveGroupRequest, SaveGroupResponse> {

	@Inject
	public SaveGroupRequestActionHandler() {
	}
	
	@Override
	public void execute(SaveGroupRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		UserGroup group = action.getGroup();
		
		if(!action.isDelete()){	
			
			group = LoginHelper.get().createGroup(group);
			
			//save
			SaveGroupResponse response = (SaveGroupResponse)actionResult;
			response.setGroup(group);
			
		}
		
		if(action.isDelete()){
			LoginHelper.get().deleteGroup(group);
		}
	}
	
	@Override
	public Class<SaveGroupRequest> getActionType() {
		return SaveGroupRequest.class;
	}
}
