package com.wira.pmgt.shared.model;

import java.util.Arrays;
import java.util.List;

public enum PermissionType implements Listable{

	CAN_EDIT("Can Edit"),
	CAN_VIEW("Can View"),
	CAN_EXECUTE("Assignee");
	
	public String displayName;
	private PermissionType(String displayName){
		this.displayName = displayName;
	}
	
	public static List<PermissionType> getTypes(ProgramDetailType type){
		if(type==ProgramDetailType.PROGRAM)
			return Arrays.asList(CAN_EDIT, CAN_VIEW);
		return Arrays.asList(CAN_EDIT, CAN_EXECUTE);
	}
	@Override
	public String getName() {
		return name();
	}
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
}
