package com.wira.pmgt.client.ui.activities;

public class TableHeader{
	String titleName;
	Double width;
	
	public TableHeader(String titleName, Double width) {
		this.titleName = titleName;
		this.width = width;
	}
	
	public String getTitleName() {
		return titleName;
	}
	
	
	public Double getWidth() {
		return width;
	}
}