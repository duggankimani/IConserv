package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.UserGroup;

public class SaveGroupResponse extends BaseResponse {

	private UserGroup group;

	public SaveGroupResponse() {
		// For serialization only
	}

	public SaveGroupResponse(UserGroup group) {
		this.group = group;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}
}
