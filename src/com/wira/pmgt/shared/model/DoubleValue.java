package com.wira.pmgt.shared.model;

public class DoubleValue implements Value {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Double value;
	private String key;
	private Long id;
	
	public DoubleValue(){
	}

	public DoubleValue(Number val){
		setValue(val);
	}
	
	public DoubleValue(Long id, String key, Number value){
		this.id=id;
		this.key=key;
		setValue(value);
	}
	
	
	public Double getValue() {
		return value;
	}
	public void setValue(Object value) {
		if(value!=null){
			this.value = ((Number)value).doubleValue();
		}else{
			this.value=null;
		}
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
	public DataType getDataType() {
		
		return DataType.DOUBLE;
	}
	
	public DoubleValue clone(boolean fullClone){
		
		Long identify=null;
		if(fullClone){
			identify = id;
		}
		DoubleValue dvalue = new DoubleValue(identify, key, value);
		return dvalue;
	}
	
	@Override
	public String toString() {
		return value==null? "NULL": value.toString();
	}
}
