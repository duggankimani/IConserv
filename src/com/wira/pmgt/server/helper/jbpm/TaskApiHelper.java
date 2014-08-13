package com.wira.pmgt.server.helper.jbpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jbpm.task.Group;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.User;

import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.exceptions.IllegalApprovalRequestException;
import com.wira.pmgt.shared.model.DocStatus;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.LongValue;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.ParticipantType;
import com.wira.pmgt.shared.model.StringValue;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.Value;

public class TaskApiHelper {

	/**
	 * Generates a task for a ProgramActivity
	 * 
	 * @param activity
	 */
	public static void createTask(TaskInfo info){	

		//Save permissions
		ProgramDaoHelper.saveTaskInfo(info);
		if(!info.contains(ParticipantType.ASSIGNEE)){
			return;
		}
		
		Document document = createDocument(info);
		if(document.getId()==null){
			//new request
			document.setValue("taskName", new StringValue(info.getTaskName()));
			document.setValue("approvalTaskName", new StringValue(info.getApprovalTaskName()));
			document.setDescription(info.getDescription());
			document.setValue("description", new StringValue(info.getDescription()));
			document.setValue("programId",new LongValue(info.getActivityId()));
		}
		document = DocumentDaoHelper.save(document);
				
		//initiator
		OrgEntity initiator = info.getParticipant(ParticipantType.INITIATOR);		
		document.setOwner((HTUser)initiator);
				
		//Assignees
		List<OrgEntity> assignees = info.getParticipants(ParticipantType.ASSIGNEE);
//		List<OrganizationalEntity> potentialOwners = new ArrayList<OrganizationalEntity>();
//		potentialOwners.addAll(getEntities(assignees));
		
		assert !assignees.isEmpty();
		
		if(document.getProcessInstanceId()==null){
			document.setValue("actorIds", getActorIds(assignees));
			document.setValue("groupIds", getGroups(assignees));
			
			assert initiator!=null;
			
			try{
				
				startWorkflow(document, initiator.getEntityId());
				Long processInstanceId = DB.getDocumentDao().getById(document.getId()).getProcessInstanceId();
				//Associate Program Detail with the process 
				ProgramDetail program = DB.getProgramDaoImpl().getById(ProgramDetail.class, info.getActivityId());
				program.setProcessInstanceId(processInstanceId);
				DB.getProgramDaoImpl().save(program);
			
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			
		}
		
	}
	
	public static void startWorkflow(Document doc, String initiatorId) throws IllegalApprovalRequestException{
		doc.setStatus(DocStatus.INPROGRESS);
		doc = DocumentDaoHelper.save(doc);
		
		if(doc.getProcessInstanceId()!=null){
			throw new IllegalApprovalRequestException(doc);
		}
		String userId = initiatorId;
		if(userId==null)
			userId = com.wira.pmgt.server.helper.session.SessionHelper.getCurrentUser().getUserId();
		
		JBPMHelper.get().createApprovalRequest(userId, doc);
	}

	private static Value getGroups(List<OrgEntity> assignees) {

		StringValue groupIds = new StringValue();
		StringBuffer ids = new StringBuffer();
		for(OrgEntity entity: assignees){
			if(entity instanceof UserGroup){
				ids.append(entity.getEntityId()+",");
			}
		}
		
		if(ids.length()>0){
			groupIds.setValue(ids.substring(0, ids.length()-1));
		}
		
		return groupIds;
	}

	private static Value getActorIds(List<OrgEntity> assignees) {

		StringValue actorIds = new StringValue();
		StringBuffer ids = new StringBuffer();
		for(OrgEntity entity: assignees){
			if(entity instanceof HTUser){
				ids.append(entity.getEntityId()+",");
			}
		}
		
		if(ids.length()>0){
			actorIds.setValue(ids.substring(0, ids.length()-1));
		}
		
		return actorIds;
		
	}

	/**
	 * Check if a document already exists -- Request Document (All Request Variables Saved here) 
	 * <br/>
	 * @param activity
	 * @return
	 */
	private static Document createDocument(TaskInfo info) {
		
		Document document = new Document();
		if(info.getProcessInstanceId()!=null){
			document = DocumentDaoHelper.getDocumentByProcessInstance(info.getProcessInstanceId());
		}else{
			document.setDocumentDate(new Date());
			//document.setSubject();//Task#200
			document.setDescription(info.getDescription());
			document.setPriority(0);
			document.setType(DocumentDaoHelper.getDocumentType("TASK"));//Document Type - Task
		}		
		
		document.setValue("message", new StringValue(info.getMessage()));
		
		return document;
	}

	private static List<OrganizationalEntity> getEntities(List<OrgEntity> assignees) {
		List<OrganizationalEntity> entities = new ArrayList<>();
		if(assignees!=null){
			for(OrgEntity entity:assignees){
				entities.add(get(entity));
			}
		}
		return entities;
	}


	private static OrganizationalEntity get(OrgEntity entity) {
		OrganizationalEntity e=null;
		if(entity instanceof HTUser){
			e= new User(entity.getEntityId());
		}else if(entity instanceof UserGroup){
			e = new Group(entity.getEntityId());
		}
		
		return e;
	}
	
}
