package com.duggan.workflow.test.process;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.server.helper.jbpm.ProcessMigrationHelper;
import com.wira.pmgt.server.helper.jbpm.TaskApiHelper;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.ParticipantType;
import com.wira.pmgt.shared.model.TaskInfo;

public class TestProgramActivityTask {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(1L);
		ProcessMigrationHelper.start(2L);
	}
	
	@Test
	public void createTask(){
		long activityId = 39L;
		ProgramDetail detail = DB.getProgramDaoImpl().getProgramDetail(activityId);
		
		String taskName = "Program-"+activityId;
		String approvalTaskName = taskName;
		
		TaskInfo info = new TaskInfo();
		
		info.setActivityId(activityId);
		info.setTaskName(taskName);
		info.setApprovalTaskName(approvalTaskName);
		info.setMessage("Kindly Take Care of This Task for me");
		info.setDescription(detail.getDescription());
		
		//Assignees
		info.addParticipant(new HTUser("Administrator"), ParticipantType.INITIATOR);
		info.addParticipant(new HTUser("calcacuervo"), ParticipantType.ASSIGNEE);
		
		TaskApiHelper.createTask(info);
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.commitTransaction();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
