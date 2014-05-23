package com.wira.pmgt.shared.model;

import java.util.List;

import com.wira.pmgt.shared.model.PermissionType.PermissionLevel;

public class PermissionPOJO {

	PermissionType type;
	List<PermissionLevel> levels;
		
	public PermissionPOJO() {
	}
	
	public boolean isPermission(PermissionType aPermissionType, PermissionLevel level){
		return type==aPermissionType && levels.contains(level);
	}
}
