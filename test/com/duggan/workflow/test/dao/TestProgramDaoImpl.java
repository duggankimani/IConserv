package com.duggan.workflow.test.dao;

import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;
import javax.transaction.SystemException;

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
import com.wira.pmgt.shared.model.PermissionType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.ProgramAnalysis;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramStatus;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.ProgramTaskForm;
import com.wira.pmgt.shared.model.program.ProgramTreeModel;

public class TestProgramDaoImpl {

	ProgramDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		dao= DB.getProgramDaoImpl();
	}
	
	@Test
	public void test(){
		Long id=209L;
		List<FundDTO> funds = ProgramDaoHelper.getFunds(id);
		
		System.err.println(funds);
		
	}
	
	@Ignore
	public void changeStatus(){
		IsProgramDetail detail = ProgramDaoHelper.getProgramById(41L, false);
		detail.setStatus(ProgramStatus.CREATED);
		ProgramDaoHelper.save(detail);
	}
	
	@Ignore
	public void getSum(){
		System.err.println(dao.getOutcome("noOfExchangeVisits", 200L));
	}
	
	@Ignore
	public void getStatus(){
		System.out.println(dao.getStatus(200L));
	}
	
	@Ignore
	public void getProgramTree(){
		Long periodId= 1L;
		Long programId = null;
		List<ProgramTreeModel> lst=ProgramDaoHelper.getProgramTree(periodId,programId);
		recursiveLoop(lst);
		System.err.println(count);
		
	}
	int count;
	private void recursiveLoop(List<ProgramTreeModel> lst) {
		if(lst==null){
			return;
		}
		for(ProgramTreeModel m: lst){
			++count;
			System.err.println(m.getProgramId()+" : "+m.getOutcomeId()+" : "+m.getId()+" : "+m.getParentId()+" : "+m.getName());
			recursiveLoop(m.getChildren());
		}
	}

	@Ignore
	public void saveProgram() throws SystemException, NamingException{
		IsProgramDetail detail = ProgramDaoHelper.getProgramById(1L, false);
		List<ProgramFundDTO> dtos = detail.getFunding();
		Assert.assertTrue(DB.hasActiveTrx());
		boolean zero=false;
		for(ProgramFundDTO fund: dtos){
			System.err.println(fund.getFund().getName()+" :: "+fund.getAmount());
			long fundid = fund.getFund().getId().longValue();
			if(fundid==1L){
				fund.getFund().setId(3L);
			}
			if(fundid==3L){
				fund.getFund().setId(1L);
			}
//			
			if(fundid==2L){
				fund.getFund().setId(4L);
			}
			if(fundid==4L){
				fund.getFund().setId(2L);
			}
			
			if(fund.getAmount()>0){
				fund.setAmount(0.0);
				zero=true;
				
			}else{
				fund.setAmount(1000.0);
				zero=false;
			}
		}
		detail = ProgramDaoHelper.save(detail);
		
//		Long id = detail.getId();
//		Double[] values = dao.getComputedAmounts(id);
//		System.err.println(values[0]+" | "+values[1]+" | "+values[2]);
		
		if(zero){
			Assert.assertEquals(new Double(0.0), detail.getBudgetAmount());
		}else{
			Assert.assertEquals(new Double(2000.0), detail.getBudgetAmount());
		}
		
	}
	
	@Ignore
	public void resetAmounts(){
		IsProgramDetail detail = ProgramDaoHelper.getProgramById(127L, false);
		ProgramDaoHelper.save(detail);
	}
	@Ignore
	public void getAmounts(){
		//Test against this :  select sum(allocatedamount),sum(commitedamount) commited,sum(actualamount) actual from programfund where programid in(select id from programdetail where parentid=1) and fundid=4; 
		
		Double[] amounts = dao.getAmounts(1L, 4L);
		System.err.println(amounts[0]+", "+amounts[1]);
	}
	
	@Ignore
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
		info.addParticipant(new HTUser("Administrator"), PermissionType.CAN_EDIT);
		info.addParticipant(new UserGroup("CEO"), PermissionType.CAN_VIEW);
		info.addParticipant(new UserGroup("HOD_DEV"), PermissionType.CAN_EDIT);
		
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
		DBTrxProvider.close();
	}

}
