package com.wira.pmgt.shared.model;

public enum ParticipantType {

	ASSIGNEE("Assignee"),
	INITIATOR("Initiator"),
	BUSINESSADMIN("Business administrator"),
	STAKEHOLDER("Stakeholder");
	
	public String displayName;
	private ParticipantType(String displayName){
		this.displayName = displayName;
	}
	
}
