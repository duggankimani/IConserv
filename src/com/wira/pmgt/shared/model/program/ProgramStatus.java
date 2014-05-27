package com.wira.pmgt.shared.model.program;

public enum ProgramStatus {

	CREATED("Created"),
	OPENED("In Progress"),
	COMPLETED("Completed"),
	REOPENED("Re-opened"),
	CLOSED("Closed");

	private String display;
	
	private ProgramStatus(String display){
		this.display = display;
	}
	
	public String getDisplayName() {

		return display;
	}
}
