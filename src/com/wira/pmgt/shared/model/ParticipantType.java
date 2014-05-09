package com.wira.pmgt.shared.model;

public enum ParticipantType implements Listable{

	ASSIGNEE("Assignee"),
	INITIATOR("Initiator"),
	BUSINESSADMIN("Business administrator"),
	STAKEHOLDER("Stakeholder");
	
	public String displayName;
	private ParticipantType(String displayName){
		this.displayName = displayName;
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
