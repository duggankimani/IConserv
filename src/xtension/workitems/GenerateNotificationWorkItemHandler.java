package xtension.workitems;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;

import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.dao.helper.NotificationDaoHelper;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.model.ApproverAction;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.model.NotificationType;

/**
 * This class is responsible for generating
 * <ul>
 * <li>System Notification - Synchronous
 * </ul>
 * 
 * @author duggan
 * 
 */
public class GenerateNotificationWorkItemHandler implements WorkItemHandler {

	private Logger logger = Logger.getLogger(GenerateNotificationWorkItemHandler.class);
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		String subject = (String) workItem.getParameter("Subject");
		String noteType = (String) workItem.getParameter("NotificationType");
		Boolean isTaskAssignmentNode = workItem.getParameter("isTaskAssignmentNode")==null? false:
			Boolean.valueOf(workItem.getParameter("isTaskAssignmentNode").toString());
		
		NotificationType type = NotificationType.valueOf(noteType);
		String documentId = (String) workItem.getParameter("DocumentId");
		String groupId = (String) workItem.getParameter("GroupId");
		String actorId = (String) workItem.getParameter("ActorId");
		String ownerId = (String) workItem.getParameter("OwnerId");
		Object isApproved = workItem.getParameter("isApproved");
		
		logger.debug("Class : "+this.getClass());
		logger.debug("Subject : "+subject);
		logger.debug("NotificationType : "+noteType);
		logger.debug("isTaskAssignmentNode : "+isTaskAssignmentNode);
		logger.debug("DocumentId : "+documentId);
		logger.debug("GroupId : "+groupId);
		logger.debug("ActorId : "+actorId);
		logger.debug("OwnerId : "+ownerId);		


		if(type==NotificationType.APPROVALREQUEST_OWNERNOTE && isTaskAssignmentNode){
			//Change type manually
			type= NotificationType.TASKASSIGNMENT_ASSIGNORNOTE;
		}
		if(type==NotificationType.APPROVALREQUEST_APPROVERNOTE && isTaskAssignmentNode){
			//Change type manually
			type= NotificationType.TASKASSIGNMENT_ASSIGNEENOTE;
		}
		
		Notification notification = new Notification();
		notification.setCreated(new Date());
		notification.setDocumentId(new Long(documentId));
		notification.setNotificationType(type);
		notification.setOwner(LoginHelper.get().getUser(ownerId));
		notification.setRead(false);
		notification.setSubject(subject);
		Document doc = DocumentDaoHelper.getDocument(notification.getDocumentId());
		notification.setDescription(doc.getDescription());
		notification.setDocumentType(doc.getType());
		
		List<HTUser> actors = null;
		List<HTUser> potentialActors = null;
		
		//notification.setTargetUserId(targetUserId);
		if(actorId!=null && !actorId.trim().isEmpty()){
			actors = new ArrayList<>();
			actors.add(LoginHelper.get().getUser(actorId));
		}
		
		//potential users
		if(groupId!=null && !groupId.trim().isEmpty()){
			potentialActors = LoginHelper.get().getUsersForGroup(groupId);
		}
		
		List<HTUser> owner = new ArrayList<>();
		//Testing;;
		
		if(ownerId==null){
			logger.debug("[[[[[###############]]]]]>>>>> OWNERID IS NULL :: "
		+workItem.getName()+" :: WorkItem "+workItem.getId());
			ownerId = "calcacuervo";
			
			//Should return here
		}
		owner.add(LoginHelper.get().getUser(ownerId));
		
		ApproverAction action =isApproved==null? ApproverAction.COMPLETED:
			(Boolean)isApproved? ApproverAction.APPROVED: ApproverAction.REJECTED;	
	
		switch (type) {
		case TASKASSIGNMENT_ASSIGNORNOTE:
		case APPROVALREQUEST_OWNERNOTE:
			generateNotes(owner, notification);
			break;
		case TASKASSIGNMENT_ASSIGNEENOTE:
		case APPROVALREQUEST_APPROVERNOTE:
			if(actors!=null){
				generateNotes(actors, notification);
			}else{
				generateNotes(potentialActors, notification);
			}
			break;
		case TASKCOMPLETED_APPROVERNOTE:	
			notification.setApproverAction(action);
			generateNotes(actors, notification);
			break;
		case TASKCOMPLETED_OWNERNOTE:
			notification.setApproverAction(action);
			generateNotes(owner, notification);
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

		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	private void generateNotes(List<HTUser> users, Notification notification) {
		
		for(HTUser user: users){
			Notification note = notification.clone();
			note.setTargetUserId(user);
			note.setRead(false);
			
			if(note.getTargetUserId()==null)
				throw new IllegalArgumentException("Target Id must not be null");
			NotificationDaoHelper.saveNotification(note);
		}
		
	}
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
