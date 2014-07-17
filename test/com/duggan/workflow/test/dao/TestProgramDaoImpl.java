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
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.ProgramAnalysis;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.ProgramTaskForm;

public class TestProgramDaoImpl {

	ProgramDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		dao= DB.getProgramDaoImpl();
	}
	
	@Test
	public void getActivitiesByOutcome(){
		Long programId = 198L;
		Long outcomeId= 199L;
		
		List<IsProgramDetail> outcomeWithActivities = ProgramDaoHelper.getProgramDetailsByOutcome(programId, outcomeId, true);
		Assert.assertSame(1, outcomeWithActivities.size());
		Assert.assertSame(2, outcomeWithActivities.get(0).getChildren().size());
	}
	
	@Ignore
	public void loadProgram(){
		IsProgramDetail detail = ProgramDaoHelper.getProgramById(176L, true);
		Assert.assertEquals(3,detail.getProgramOutcomes().size());
	}
	
	@Ignore
	public void load(){
		List<IsProgramDetail> list = ProgramDaoHelper.getProgramsByType(ProgramDetailType.OBJECTIVE, true);
		System.err.println(">>> Test: "+list.size());		
	}
	
	@Ignore
	public void create(){
		ProgramDetail objective = new ProgramDetail();
		objective.setName("Obj 1");
		objective.setDescription("Objective 1");
		objective.setType(ProgramDetailType.OBJECTIVE);
		dao.save(objective);
		
		ProgramDetail outcome = new ProgramDetail();
		outcome.setName("Outcome 1.1");
		outcome.setDescription("Outcome 1.1");
		outcome.setParent(objective);
		outcome.setType(ProgramDetailType.OUTCOME);
		dao.save(outcome);
		
		ProgramDetail outcome2 = new ProgramDetail();
		outcome2.setName("Outcome 1.2");
		outcome2.setDescription("Outcome 1.2");
		outcome2.setParent(objective);
		outcome2.setType(ProgramDetailType.OUTCOME);
		dao.save(outcome2);
		
		ProgramDetail program = new ProgramDetail();
		program.setName("Wildlife Program");
		program.setDescription("Wildlife Program");
		program.setPeriod(dao.getActivePeriod());
		program.setType(ProgramDetailType.PROGRAM);
		dao.save(program);
		
		ProgramDetail activity = new ProgramDetail();
		activity.setName("Community Meeting");
		activity.setDescription("Community Meeting");
		activity.setParent(program);
		activity.setActivityOutcome(outcome2);
		activity.setPeriod(dao.getActivePeriod());
		activity.setType(ProgramDetailType.ACTIVITY);
		dao.save(activity);
		
		ProgramDetail activity2 = new ProgramDetail();
		activity2.setName("Community Meeting");
		activity2.setDescription("Community Meeting");
		activity2.setParent(program);
		activity2.setActivityOutcome(outcome2);
		activity2.setPeriod(dao.getActivePeriod());
		activity2.setType(ProgramDetailType.ACTIVITY);
		dao.save(activity2);
		
	}
	
	@Ignore
	public void save(){
		IsProgramDetail detail = new ProgramDTO();
		detail.setName("Test");
		detail.setDescription("Test");
		detail.setType(ProgramDetailType.PROGRAM);
		detail.setPeriod(ProgramDaoHelper.getActivePeriod());
		ProgramDaoHelper.save(detail);
		
	}
	
	@Ignore
	public void delete(){
		Long programId=48L;
		
		ProgramDaoHelper.delete(programId);
	}
	
	@Ignore
	public void getGetAnalysisData(){
		List<ProgramAnalysis> data = ProgramDaoHelper.getAnalysisData(null);
		Assert.assertNotSame(0, data.size());
	}
	
	@Ignore
	public void getTaskForms(){
		List<ProgramTaskForm> forms = dao.getTaskFormsForProgram(53L);
		Assert.assertNotSame(0, forms.size());
		
	}
	
	@Ignore
	public void getCalendar(){
		
		List<ProgramSummary> details = dao.getProgramCalendar("Administrator");
		//List<ProgramDetail> details = dao.getProgramCalendar("jodonya");
		Assert.assertNotSame(0, details.size());
		for(ProgramSummary d: details){
			System.out.println(d.getProgramId()+" | "+d.getId()+" | "+d.getParentId()+" | "+d.getStatus()+" | "+d.getName()+" | "+d.getType()+" | "+d.getStartDate()+" | "+d.getEndDate());
		}
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
	
	@Ignore
	public void loadProgramsByType(){
		String userId = "Administrator";
		String groupId = "ADMIN";
		
		List<ProgramDetail> details = dao.getProgramDetails(ProgramDetailType.PROGRAM, dao.getPeriod(1L),
				userId, Arrays.asList(groupId));
		
		Assert.assertNotNull(details);
		Assert.assertSame(details.size(), 9);
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
