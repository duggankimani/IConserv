package com.wira.pmgt.shared.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author duggan
 *
 */
public class Permission {

	private HTUser user;
	private UserGroup group;
	List<PermissionPOJO> permissions = new ArrayList<PermissionPOJO>();
	
	public Permission() {
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}

	public List<PermissionPOJO> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionPOJO> permissions) {
		this.permissions = permissions;
	}
	
}
