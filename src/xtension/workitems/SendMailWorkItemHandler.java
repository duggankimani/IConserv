package xtension.workitems;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.executor.ExecutorModule;
import com.wira.pmgt.server.executor.api.CommandCodes;
import com.wira.pmgt.server.executor.api.CommandContext;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.NotificationType;
import com.wira.pmgt.shared.model.UserGroup;

/**
 * Send Asynchronous Email
 * 
 * @author duggan
 *
 */
public class SendMailWorkItemHandler implements WorkItemHandler {


	private static Logger log = Logger.getLogger(SendMailWorkItemHandler.class);
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		String subject = (String) workItem.getParameter("Subject");
		String noteType = (String) workItem.getParameter("NotificationType");
		NotificationType type = NotificationType.valueOf(noteType);
		String documentId = (String) workItem.getParameter("DocumentId");
		String groupId = (String) workItem.getParameter("GroupId");
		String actorId = (String) workItem.getParameter("ActorId");
		String ownerId = (String) workItem.getParameter("OwnerId");
		Object isApproved = workItem.getParameter("isApproved");

		String[]groups = null;
		if(groupId!=null){
			groups = groupId.split(",");
		}
		
		Document doc = null;
		try{
			doc = DocumentDaoHelper.getDocument(Long.parseLong(documentId));
		}catch(Exception e){}
		
		log.debug("Class : "+this.getClass());
		log.debug("Subject : "+subject);
		log.debug("NotificationType : "+noteType);
		log.debug("DocumentId : "+documentId);
		log.debug("GroupId : "+groupId);
		log.debug("ActorId : "+actorId);
		log.debug("OwnerId : "+ownerId);	

		Map<String, Object> params = workItem.getParameters();
		
		List<HTUser> users = null;
		//notification.setTargetUserId(targetUserId);
		if(actorId!=null && !actorId.trim().isEmpty()){
			users = new ArrayList<>();
			users.add(LoginHelper.get().getUser(actorId));
		}else if(groups!=null){
			users = LoginHelper.get().getUsersForGroups(groups);
		}
		
		List<HTUser> owner = new ArrayList<>();
		owner.add(LoginHelper.get().getUser(ownerId));
		
		String body = "";
		String approver = "";
		if(groups!=null && groups.length>0){
			List<UserGroup> groupList = LoginHelper.get().getGroupsByIds(groups);
			for(UserGroup group:groupList){
				approver = approver.concat(group.getFullName()+", ");
			}
			
			if(!approver.isEmpty() && approver.endsWith(", ")){
				approver = approver.substring(0, approver.length()-2);
			}
		}
		
		if(approver==null && actorId!=null){
			approver = actorId;
			HTUser user = LoginHelper.get().getUser(actorId);
			if(user!=null)
				approver = user.getFullName();
			
		}
		
		if(approver==null){
			approver="";
		}
		
		params.put("Approver", approver);
		
		String noteaction = isApproved==null?"Completed": 
			(Boolean)isApproved? "Approved": "Denied";
		
		switch (type) {
		case APPROVALREQUEST_OWNERNOTE:
			subject = subject+" Approval Request Submitted";
			body = "Your document #"+subject+" was submitted to "+approver;
			break;
		case APPROVALREQUEST_APPROVERNOTE:
			subject = subject+" Approval Request from "+getOwner(ownerId);
			body =  "The following document requires your review/ approval.";
			owner = users;
			break;
		case TASKCOMPLETED_APPROVERNOTE:
			subject = subject+" - "+noteaction;
			body =  "You "+noteaction.toLowerCase()+" Document #"+subject;
			owner= users;
			break;
		case TASKCOMPLETED_OWNERNOTE:						
			subject = subject+" - "+approver+" "+noteaction;
			body =  "The following document "+noteaction.toLowerCase()+" by "+approver;
			break;
		case PROCESS_COMPLETED:
			
			break;

		case TASK_REMINDER:

			break;
		case COMMENT:
			break;
		default:
			break;
		}
		
		params.put("Body", body);
		params.put("Subject", subject);
		sendMail(doc,owner, params);
		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	private void sendMail(Document doc,List<HTUser> users, Map<String, Object> params) {
		
		StringBuffer recipient=new StringBuffer();
		
		Iterator<HTUser> iter = users.iterator();
		
		List<String> usersList = new ArrayList<>();
		while(iter.hasNext()){
			String email = iter.next().getEmail();
			if(email==null){
				continue;
			}
			if(usersList.contains(email)){
				continue;
			}
			usersList.add(email);
			
			recipient.append(email);
			
			if(iter.hasNext())
				recipient.append(",");
		}
		
		if(recipient.toString().isEmpty()){
			return;
		}
		
		/**
		 * Sending mail
		 * 
		 * Schedule async
		 */
		CommandContext context = new CommandContext();
		String initiatorId = doc.getOwner()==null? null: doc.getOwner().getUserId();
		if(initiatorId==null){
			log.warn("Request initiator ID is null; Email will not contain user image");
		}
		params.put("ownerId", initiatorId);
		
		
		params.put("callbacks", CommandCodes.SendEmailCallback.name());
		params.put("To", recipient.toString());
		params.put("From", params.get("From")==null? "ebpm.mgr@gmail.com": params.get("From"));
		
		try{
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("email.html");			
			String html = "";			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			IOUtils.copy(is, bout);
			html = new String(bout.toByteArray());
			
			String ownerId = params.get("OwnerId")==null? "Unknown":params.get("OwnerId").toString(); 
			String body = params.get("Body")==null?"":params.get("Body").toString();
			
			html = html.replace("${Request}",body);
			html = html.replace("${OwnerId}", getOwner(ownerId));
			html = html.replace("${Office}", params.get("Approver").toString());
			html = html.replace("${Description}",doc.getDescription());
			html = html.replace("${DocSubject}", doc.getSubject());
			html = html.replace("${DocumentDate}", SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(
					doc.getDocumentDate()==null? doc.getCreated(): doc.getDocumentDate()));
			DocumentType type = doc.getType();
			html = html.replace("${DocType}",type.getDisplayName());
			
			HttpServletRequest request = SessionHelper.getHttpRequest();
			if(request!=null){
				String requestURL = request.getRequestURL().toString();
				String servletPath = request.getServletPath();
				String pathInfo = request.getPathInfo();
				
				log.debug("# RequestURL = "+requestURL);
				log.debug("# ServletPath = "+servletPath);
				log.debug("# Path Info = "+pathInfo);
				if(pathInfo!=null){
					requestURL = requestURL.replace(pathInfo, "");
				}
				log.debug("# Remove Path Info = "+requestURL);				
				requestURL = requestURL.replace(servletPath, "?#home;type=search;did="+doc.getId());
				log.debug("# Replace ServletPath = "+requestURL);
				
				html = html.replace("${DocumentURL}", requestURL);
			}else{
				html = html.replace("${DocumentURL}", "#");
			}
			
			params.put("Body", html);
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		params.put("businessKey", UUID.randomUUID().toString());
		context.setData(params);

		ExecutorModule.getInstance().getExecutorServiceEntryPoint()		
				.scheduleRequest(CommandCodes.SendEmailCommand, context);
		
	}


	private CharSequence getOwner(String ownerId) {
		
		String owner = ownerId;
		if(ownerId!=null){
			HTUser user = LoginHelper.get().getUser(ownerId);
			if(user!=null){
				owner = user.getFullName();
			}
		}
		return owner;
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
