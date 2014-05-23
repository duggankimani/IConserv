package com.wira.pmgt.shared.model;

public enum PermissionType {

	CAN_VIEW_PROGRAMS("Can view programs"),
	CAN_CREATE_PROGRAMS("Can create programs"),//Create-Update programs, outcomes, activities, tasks....  
	CAN_ASSIGN_PROGRAMS("Can assign programs"),
	CAN_VIEW_BUDGETS("Can view budgets");
	
	String description;
	
	private PermissionType(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public enum PermissionLevel{
		READ,WRITE,EXECUTE
	}
	
}
