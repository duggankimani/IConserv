package com.wira.pmgt.client.ui.programs;

public class TableHeader{
	String name;
	String title;
	String thStyleName;
	Double width;
	
	public TableHeader(String name, Double width) {
		this(name,width,null);
	}
	
	public TableHeader(String name, String title, Double width) {
		this(name,width,null);
		this.title = title;
	}
	
	public TableHeader(String name, Double width, String styleName) {
		this.name = name;
		this.width = width;
		this.thStyleName = styleName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getStyleName() {
		return thStyleName;
	}

	public Double getWidth() {
		return width;
	}

	public String getTitle() {
		return title;
	}
}