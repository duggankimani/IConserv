package com.wira.pmgt.client.ui.programs;

import com.wira.pmgt.shared.model.Listable;

public enum AnalysisType implements Listable{

	ACTUAL("Actual", "Show remaining amount after actual expenditure"),
	BUDGET("Budget", "Show remaining unbudgetted amount");
	
	private String displayName;
	private String description;
	
	private AnalysisType(String displayName, String description){
		this.displayName = displayName;
		this.description = description;
	}
	
	@Override
	public String getName() {
		return name();
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}
}
