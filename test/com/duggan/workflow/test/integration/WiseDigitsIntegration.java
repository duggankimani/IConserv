package com.duggan.workflow.test.integration;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.DocumentLine;
import com.wira.pmgt.shared.model.Value;

public class WiseDigitsIntegration {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	
	@Test
	public void sendDocument() {

		//String serviceUrl = "http://localhost:8080/ebusiness/rest/request/approval";
		String serviceUrl = "http://localhost:8888/rest/request/approval";
		String ownerId="Administrator";
		
		Document document = DocumentDaoHelper.getDocument(171L);
		
		String urlEncoded = getUrlEncoding(serviceUrl+serviceUrl, document);
		
		System.err.println(urlEncoded);
	}


	private String getUrlEncoding(String serviceUrl, Document document) {
		
		Map<String, Value> values= document.getValues();
		
		StringBuffer buffer = new StringBuffer("?action=Save");
		for(Value value: values.values()){
			String key = value.getKey();
			String valueAsString = value.getValue()==null? "": value.getValue().toString();
			
			if(value.getKey().equals("subject")){
				key = "documentNo";
			}
			
			buffer.append("&"+key+"="+valueAsString);			
		}
		

		Map<String, List<DocumentLine>> linesMap = document.getDetails();
	
		buffer.append("&array(");
		if(linesMap.values()!=null && !linesMap.values().isEmpty()){

			Set<String> keyset = linesMap.keySet();
			
			for(String key: keyset){
				List<DocumentLine> lst = linesMap.get(key);
				
				for(DocumentLine line: lst){
					buffer.append("array(");
					for(Value value: line.getValues().values()){
						String valueAsString = value.getValue()==null? "": value.getValue().toString();
												
						buffer.append("'"+value.getKey()+"'=>\""+valueAsString+"\",");
					}
					buffer.replace(buffer.length()-1, buffer.length(), "");
					buffer.append("),");
				}
				
				buffer.replace(buffer.length()-1, buffer.length(), "");
				
			}
			
		}
		buffer.append(")");
		
		return buffer.toString();
	}


}
