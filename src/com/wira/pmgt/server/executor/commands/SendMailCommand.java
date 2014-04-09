package com.wira.pmgt.server.executor.commands;

import java.util.Map;


import com.wira.pmgt.server.executor.api.Command;
import com.wira.pmgt.server.executor.api.CommandContext;
import com.wira.pmgt.server.executor.api.ExecutionResults;
import com.wira.pmgt.server.helper.email.EmailServiceHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;

public class SendMailCommand implements Command{

	@Override
	public ExecutionResults execute(CommandContext ctx) throws Exception {
		
		Map<String, Object> data = ctx.getData(); 
		String subject = data.get("Subject")== null ? "": data.get("Subject").toString();
		String body = data.get("Body")==null? "": data.get("Body").toString();
		String recipients = data.get("To")==null? "": data.get("To").toString();
		
		EmailServiceHelper.sendEmail(body, subject, recipients);
		
		ExecutionResults result = new ExecutionResults();
		
		result.setData(ctx.getData());
		
		return result;
	}
}
