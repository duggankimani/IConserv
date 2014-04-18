package com.wira.pmgt.client.ui.component.grid;

import java.util.HashMap;

public class DataModel extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	public DataModel(){}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void set(String key, Object value){
		put(key, value);
	}
}
