package com.wira.jbpm.humantask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jbpm.task.AccessType;
import org.jbpm.task.Deadline;
import org.jbpm.task.Deadlines;
import org.jbpm.task.Group;
import org.jbpm.task.I18NText;
import org.jbpm.task.OnParentAbortAllSubTasksEndStrategy;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.PeopleAssignments;
import org.jbpm.task.SubTasksStrategy;
import org.jbpm.task.SubTasksStrategyFactory;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;
import org.jbpm.task.User;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.service.local.LocalTaskService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;

public class TestJbpmHumanTask {

	@Before
	public void init(){
		DBTrxProvider.init();
		DB.beginTransaction();	
	}
	
	@Test
	public void createTask(){
		LocalTaskService service =  JBPMHelper.get().getTaskClient();
		HashMap<String, Object> data = new HashMap<>();
		byte[] content = SerializationUtils.serialize(data);

		String language = "en-UK";
		String subject="Conduct Community Meetings";
		String description="Conservation Meetings for community empowerment";
		int priority = 0;
		
		User businessAdmins = new User("Administrator");  
		User createdBy = new User("Administrator");
		User potentialOwner = new User("mariano"); 
		
		PeopleAssignments peopleAssignments = new PeopleAssignments();
		peopleAssignments.setTaskInitiator(createdBy);
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
		 * see org.jbpm.task.SubTasksStrategyFactory - OnParentAbortAllSubTasksEnd; OnAllSubTasksEndParentEnd
		 */
		//task.setSubTaskStrategies(subTaskStrategies);
		
		TaskData taskData = new TaskData();
		taskData.setActivationTime(new Date());
		//taskData.setActualOwner(actualOwner);
		//taskData.setAttachments(attachments);
		//taskData.setComments(comments);
		//taskData.setCompletedOn(completedOn);
		taskData.setCreatedBy(createdBy);
		taskData.setCreatedOn(new Date());
		//taskData.setDocument(documentID, documentContentData);
		//taskData.setDocumentAccessType(accessType);
		
		//taskData.setParentId(parentId); TODO: Take Note of this
		
		taskData.setSkipable(false);
		taskData.setProcessId(null);
		taskData.setProcessInstanceId(0);
		taskData.setProcessSessionId(0);
		task.setTaskData(taskData);

		
		ContentData contentData = new ContentData();
		contentData.setContent(content);
		contentData.setType("java.util.HashMap");
		contentData.setAccessType(AccessType.Inline);
		service.addTask(task, contentData);
	}
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
	}

}
