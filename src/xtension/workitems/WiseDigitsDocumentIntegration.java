package xtension.workitems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.wira.pmgt.server.rest.model.Request;
import com.wira.pmgt.server.rest.service.impl.OutgoingRequestImpl;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.DocumentLine;
import com.wira.pmgt.shared.model.Value;

public class WiseDigitsDocumentIntegration implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Object targetUrl= workItem.getParameter("url");
		Object document = workItem.getParameter("document");
		//document full
		
		Request request = new Request();
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("url", targetUrl);
		context.put("document", document);
		request.setContext(context);
		
		
		new OutgoingRequestImpl().executePostCall(getUrlEncoding((Document)document));
	}
	
	private String getUrlEncoding(Document document) {
		
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


	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
