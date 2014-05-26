package com.duggan.workflow.test.dao;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.wira.pmgt.server.dao.ProgramDaoImpl;
import com.wira.pmgt.server.dao.biz.model.Period;
import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.ParticipantType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.UserGroup;

public class TestProgramDaoImpl {

	ProgramDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		dao= DB.getProgramDaoImpl();
	}
	
	@Ignore
	public void assign(){
		long activityId = 1L;
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
		info.addParticipant(new UserGroup("CEO"), ParticipantType.STAKEHOLDER);
		info.addParticipant(new UserGroup("HOD_DEV"), ParticipantType.BUSINESSADMIN);
		
		ProgramDaoHelper.saveTaskInfo(info);
	}
	
	@Test
	public void loadProgramsByType(){
		List<ProgramDetail> details = dao.getProgramDetails(ProgramDetailType.PROGRAM, dao.getPeriod(1L),
				"calcacuervo", Arrays.asList("USERS"));
		Assert.assertNotNull(details);
		//Assert.assertNotSame(details.size(), 0);
		System.out.println("Load All Size: "+details.size());
	}
	
	@Ignore
	public void loadPrograms(){
		
		List<ProgramDetail> details = dao.getProgramDetails(dao.getActivePeriod(),
				"Administrator", Arrays.asList("ADMIN"));
		
		Assert.assertNotNull(details);
		//Assert.assertSame(details.size(), 2);
		System.out.println("Load All Size: "+details.size());
	}
	
	@Ignore
	public void getActivePeriod(){
		Period active = dao.getActivePeriod();
		Assert.assertNotNull(active);
		System.out.println(active.getDescription());
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}

}
