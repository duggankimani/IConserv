package com.wira.pmgt.server.helper.jbpm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jbpm.ruleflow.core.RuleFlowProcessFactory;
import org.jbpm.task.AccessType;
import org.jbpm.task.Deadline;
import org.jbpm.task.Deadlines;
import org.jbpm.task.Group;
import org.jbpm.task.I18NText;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.PeopleAssignments;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;
import org.jbpm.task.User;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.service.local.LocalTaskService;

import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.LongValue;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.ParticipantType;
import com.wira.pmgt.shared.model.StringValue;
import com.wira.pmgt.shared.model.TaskInfo;

public class TaskApiHelper {

	/**
	 * Generates a task for a ProgramActivity
	 * 
	 * @param activity
	 */
	public void createTask(TaskInfo info){		
		Document document = createDocument(info);
		DocumentDaoHelper.save(document);
		
		//initiator
		User initiator = (User)get(info.getParticipant(ParticipantType.INITIATOR));		
				
		//Assignees
		List<OrgEntity> assignees = info.getParticipants(ParticipantType.ASSIGNEE);
		List<OrganizationalEntity> potentialOwners = new ArrayList<OrganizationalEntity>();
		potentialOwners.addAll(getEntities(assignees));
		
		assert initiator!=null;
		
		createTask(initiator,potentialOwners, document);
	}
	

	private void createTask(User initiator,
			List<OrganizationalEntity> potentialOwners, Document document) {
		LocalTaskService service =  JBPMHelper.get().getTaskClient();
		HashMap<String, Object> data = new HashMap<>();
		data.put("document", document);
		//Create Document
		
		byte[] content = SerializationUtils.serialize(data);

		String language = "en-UK";
		String subject=document.getSubject();
		String description=document.getDescription();
		int priority = document.getPriority();
		
		User businessAdmins = new User("Administrator");  
		User potentialOwner = new User("mariano"); 
		
		PeopleAssignments peopleAssignments = new PeopleAssignments();
		peopleAssignments.setTaskInitiator(initiator);
		//peopleAssignments.setTaskStakeholders(taskStakeholders);
		peopleAssignments.setPotentialOwners(Arrays.<OrganizationalEntity>asList(potentialOwner));
		peopleAssignments.setBusinessAdministrators(Arrays.<OrganizationalEntity>asList(businessAdmins));
		//peopleAssignments.setRecipients(recipients);
		//peopleAssignments.setExcludedOwners(excludedOwners);
		
		Task task = new Task();
		task.setArchived(false);
		
		Deadlines deadlines = new Deadlines();
		
		Deadline startDeadline = new Deadline();
		startDeadline.setDate(DateUtils.addHours(new Date(), 1));
		//deadline.setDocumentation(documentation);
		deadlines.setStartDeadlines(Arrays.asList(startDeadline));
		
		Deadline endDeadline = new Deadline();
		endDeadline.setDate(DateUtils.addHours(new Date(), 24));
		//endDeadline.setDocumentation(documentation);
		deadlines.setEndDeadlines(Arrays.asList(endDeadline));
		
		
		task.setDeadlines(deadlines);
		//task.setDelegation(delegation);
		task.setDescriptions(Arrays.asList(new I18NText(language, description)));
		//task.setId(id);
		task.setNames(Arrays.asList(new I18NText(language, description)));
		task.setPeopleAssignments(peopleAssignments);
		task.setPriority(priority);
		task.setSubjects(Arrays.asList(new I18NText(language, subject)));
		
		/**
		 * @see org.jbpm.task.SubTasksStrategyFactory
		 * @see org.jbpm.task.OnParentAbortAllSubTasksEnd
		 * @see org.jbpm.task.OnAllSubTasksEndParentEnd
		 */
		//task.setSubTaskStrategies(subTaskStrategies);
		
		TaskData taskData = new TaskData();
		
		taskData.setActivationTime(new Date());
		//taskData.setActualOwner(actualOwner);
		//taskData.setAttachments(attachments);
		//taskData.setComments(comments);
		//taskData.setCompletedOn(completedOn);
		taskData.setCreatedBy(initiator);
		taskData.setCreatedOn(new Date());
		//taskData.setDocument(documentID, documentContentData);
		//taskData.setDocumentAccessType(accessType);
		
		//taskData.setParentId(parentId); TODO: Take Note of this
		
		taskData.setSkipable(false);
//		taskData.setProcessId(null);
//		taskData.setProcessInstanceId(0);
//		taskData.setProcessSessionId(0);
		task.setTaskData(taskData);

		ContentData contentData = new ContentData();
		contentData.setContent(content);
		contentData.setType("java.util.HashMap");
		contentData.setAccessType(AccessType.Inline);
		service.addTask(task, contentData);

	}


	/**
	 * Check if a document already exists 
	 * <br/>
	 * @param activity
	 * @return
	 */
	private Document createDocument(TaskInfo info) {
		
		Document document = new Document();
		document.setDocumentDate(new Date());
		//document.setSubject();//Task#200
		document.setDescription(info.getDescription());
		document.setValue("message", new StringValue(info.getMessage()));
				
		document.setPriority(0);
		document.setType(DocumentDaoHelper.getDocumentType("TASK"));//Document Type - Task
		document.setValue("activityId", new LongValue(info.getRefId()));
		document.setValue("activityType", new StringValue(info.getType().name()));
		
		return document;
	}

	private List<OrganizationalEntity> getEntities(List<OrgEntity> assignees) {
		List<OrganizationalEntity> entities = new ArrayList<>();
		if(assignees!=null){
			for(OrgEntity entity:assignees){
				entities.add(get(entity));
			}
		}
		return entities;
	}


	private OrganizationalEntity get(OrgEntity entity) {
		OrganizationalEntity e=null;
		if(entity instanceof HTUser){
			e= new User(entity.getEntityId());
		}else{
			e = new Group(entity.getEntityId());
		}
		
		return e;
	}
	
	public void createDynamicProcess(){
		String version="1.0";
		String packageName="org.lwf.process";
		String processId=packageName+".TaskAssignment";
		
		RuleFlowProcessFactory factory = RuleFlowProcessFactory.createProcess(processId);
		
		//create process dynamically
		factory = factory.name("TaskAssignment").version(version).packageName(packageName);
		
		//adding start node
		factory.startNode(1).name("Start").done();
		
		//Adding Human Task
	}
}
