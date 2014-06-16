package com.wira.pmgt.shared.model.program;

public enum ProgramStatus {

	CREATED("Created",0),
	OPENED("Assigned",1),
	COMPLETED("Awaiting Approval",2),
	REOPENED("Re-opened",1),
	CLOSED("Completed",3);

	private String display;
	private int weight;
	
	private ProgramStatus(String display, int weight){
		this.display = display;
		this.weight=weight;
	}
	
	public String getDisplayName() {
		return display;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public int getPercCompletion(){
		Double progress = (new Double(this.weight)/(CLOSED.weight)*100);
		return progress.intValue();
	}
}
