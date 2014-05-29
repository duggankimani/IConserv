package com.wira.pmgt.shared.model.form;

import java.util.HashMap;
import java.util.Map;

/***
 * 
 * @author duggan
 * Variable Parameter Mappings : Inputs & Outputs
 */
public class ProcessMappings {

	Map<String, String> inputMappings = new HashMap<String, String>();
	
	Map<String, String> outMappings = new HashMap<String,String>();
	
	public ProcessMappings() {
		
	}
	
	/**
	 * set Input Variable -> Parameter Mapping
	 * @param inMappings
	 */
	public void setInputMappings(Map<String, String> inMappings){
		this.inputMappings = inMappings;
	}
	
	/**
	 * set Output Parameter --> Variable Mapping
	 * @param outMappings
	 */
	public void setOutMappings(Map<String, String> outMappings){
		this.outMappings = outMappings;
	}
	
	/**
	 * get Output Variable Name by Parameter Name  
	 * @param oldName
	 * @return
	 */
	public String getOutputName(String parameterName){
		String varName = outMappings.get(parameterName);
		if(varName==null){
			varName = parameterName;
		}
		return varName;
	}
	
}
