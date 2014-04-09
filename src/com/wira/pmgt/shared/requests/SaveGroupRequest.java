package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveGroupResponse;

public class SaveGroupRequest extends BaseRequest<SaveGroupResponse> {

	private UserGroup group;
	private boolean isDelete=false;

	@SuppressWarnings("unused")
	private SaveGroupRequest() {
		// For serialization only
	}

	public SaveGroupRequest(UserGroup group) {
		this.group = group;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new SaveGroupResponse();
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
