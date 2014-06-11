package com.wira.pmgt.server.executor.commands;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wira.pmgt.server.executor.api.Command;
import com.wira.pmgt.server.executor.api.CommandContext;
import com.wira.pmgt.server.executor.api.ExecutionResults;
import com.wira.pmgt.server.helper.email.EmailServiceHelper;
import com.wira.pmgt.shared.model.HTUser;

public class SendMailCommand implements Command{


	static Logger log = Logger.getLogger(SendMailCommand.class);
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public ExecutionResults execute(CommandContext ctx) throws Exception {
		
		Map<String, Object> data = ctx.getData(); 
		String subject = data.get("Subject")== null ? "": data.get("Subject").toString();
		String body = data.get("Body")==null? "": data.get("Body").toString();
		Object recipients = data.get("To")==null? "": data.get("To");
		
		if(recipients instanceof List){
			log.debug("## Recipients List<HTUser> found !!!!! ");
			EmailServiceHelper.sendEmail(body, subject, (List<HTUser>)recipients,(HTUser)data.get("ownerId"));
			
		}else if(recipients instanceof String){
			log.warn("Recipients assumed to be a comma separated String of emails !!!!! >>"+recipients);
			EmailServiceHelper.sendEmail(body, subject, recipients.toString(),
					data.get("ownerId")==null? null :data.get("ownerId").toString());
			
		}else{
			log.warn("Recipients Type cannot be resolved!!!!! >>"+recipients);
		}
		
		ExecutionResults result = new ExecutionResults();
		
		result.setData(ctx.getData());
		
		return result;

	}
}
