package com.duggan.workflow.shared.model;

import java.util.ArrayList;
import java.util.Collection;

public class GridValue implements Value {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Collection<DocumentLine> value = new ArrayList<DocumentLine>();
	private String key;
	private Long id;
	
	public GridValue(){
			
	}
	
	@Override
	public DataType getDataType() {
		return DataType.GRID;
	}


	public Collection<DocumentLine> getValue() {
		return value;
	}


	public void setValue(Collection<DocumentLine> value) {
		this.value = value;
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setValue(Object value) {
		System.err.println(">>>>>> Grid :: "+value);
	}
}
