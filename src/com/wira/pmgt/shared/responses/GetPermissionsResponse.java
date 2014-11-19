package com.wira.pmgt.shared.responses;

import java.util.HashMap;

import com.wira.pmgt.shared.model.PermissionType;

public class GetPermissionsResponse extends BaseResponse {

	private HashMap<Long, PermissionType> permissions = new HashMap<Long, PermissionType>();
	
	public GetPermissionsResponse() {
		// For serialization only
	}

	public HashMap<Long, PermissionType> getPermissions() {
		return permissions;
	}

	public void setPermissions(HashMap<Long, PermissionType> permissions) {
		this.permissions = permissions;
	}

}
