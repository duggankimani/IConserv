package com.wira.pmgt.server.dao.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wira.pmgt.client.ui.programs.ProgramsPresenter;
import com.wira.pmgt.server.dao.ProgramDaoImpl;
import com.wira.pmgt.server.dao.biz.model.Fund;
import com.wira.pmgt.server.dao.biz.model.FundAllocation;
import com.wira.pmgt.server.dao.biz.model.Period;
import com.wira.pmgt.server.dao.biz.model.ProgramAccess;
import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.dao.biz.model.ProgramFund;
import com.wira.pmgt.server.dao.biz.model.TargetAndOutcome;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.ParticipantType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.StringValue;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.model.form.FormModel;
import com.wira.pmgt.shared.model.form.Property;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramStatus;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.TargetAndOutcomeDTO;

public class ProgramDaoHelper {

	public static PeriodDTO save(PeriodDTO periodDTO) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		Period period = get(periodDTO);
		dao.save(period);
		periodDTO = get(period);
		return periodDTO;
	}

	private static PeriodDTO get(Period period) {
		if(period==null){
			return null;
		}
		
		PeriodDTO periodDTO = new PeriodDTO();
		periodDTO.setDescription(period.getDescription());
		periodDTO.setEndDate(period.getEndDate());
		periodDTO.setStartDate(period.getStartDate());
		periodDTO.setId(period.getId());
		
		return periodDTO;
	}

	private static Period get(PeriodDTO periodDTO) {
		if(periodDTO==null){
			return null;
		}
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		Period period = new Period();
		if(periodDTO.getId()!=null){
			period = dao.getPeriod(periodDTO.getId());
		}
		
		period.setDescription(periodDTO.getDescription());
		period.setEndDate(periodDTO.getEndDate());
		period.setStartDate(periodDTO.getStartDate());
		return period;
	}

	public static List<PeriodDTO> getPeriods() {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		
		List<Period> periods = dao.getPeriods();
		
		List<PeriodDTO> dtos = new ArrayList<>();
		
		for(Period p: periods){
			dtos.add(get(p));
		}
		
		return dtos;
	}

	public static ProgramDTO save(IsProgramDetail programDTO) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail program = get(programDTO);
		ProgramDetail parent = program.getParent();
		
		//Parent must have a preset set of funds before child source of funds are generated
		if(parent!=null){
			
			Set<ProgramFund> parentSourceOfFunds = parent.getSourceOfFunds();
			Set<Fund> fundSources = new HashSet<>(); 
			for(ProgramFund programFund: parentSourceOfFunds){
				Fund fund = programFund.getFund();
				fundSources.add(fund);				
			}
			
			Set<ProgramFund> childFunds =  program.getSourceOfFunds();
			for(ProgramFund childFund: childFunds){
				if(fundSources.contains(childFund.getFund())){
					continue;
				}else{
					//Auto create program fund for parent if it did not exist (with zero for budget amount)
					ProgramFund programFund= new ProgramFund();
					programFund.setAmount(0.0);
					programFund.setFund(childFund.getFund());
					programFund.setProgramDetail(parent);
					dao.save(programFund);
				}				
			}
			
		}
		
		dao.save(program);
				
		return get(program,false);
	}

	private static ProgramDTO get(ProgramDetail program, boolean loadChildren){
		return get(program, loadChildren, true);
	}
	
	private static ProgramDTO get(ProgramDetail program, boolean loadChildren, boolean loadObjectives) {
		if(program==null){
			return null;
		}
		ProgramDTO dto = new ProgramDTO();
		dto.setActualAmount(program.getActualAmount());
		dto.setBudgetAmount(program.getBudgetAmount());
		dto.setDescription(program.getDescription());
		dto.setEndDate(program.getEndDate());
		dto.setFunding(get(program.getSourceOfFunds()));
		dto.setId(program.getId());
		dto.setName(program.getName());
		dto.setParentId(program.getParent()==null? null: program.getParent().getId());
		dto.setPeriod(get(program.getPeriod()));
		dto.setStartDate(program.getStartDate());
		dto.setCode(program.getCode());
		dto.setStatus(program.getStatus());
		//dto.setTargetsAndOutcomes(List<TargetAndOutcomeDTO>);
		dto.setType(program.getType());
		
		if(loadChildren){
			dto.setChildren(getActivity(program.getChildren(),loadChildren,loadObjectives));
		}
		
		if(program.getType()==ProgramDetailType.PROGRAM){
			//Objectives are saved a children of program
			//Load program objectives here
			List<IsProgramDetail> objectives = new ArrayList<>();
			for(ProgramDetail obj: program.getChildren()){					
				if(obj.getType()==ProgramDetailType.OBJECTIVE){
					objectives.add(get(obj, false));
				}					
			}
			dto.setObjectives(objectives);
		}else{
			//Objectives for Outcomes/Activities (many to many relationship)
			//if(loadObjectives){
			dto.setObjectives(getActivity(program.getObjectives(), false,true));
			//}
		}
		
		if(program.getTargets()!=null)
			dto.setTargetsAndOutcomes(getTargets(program.getTargets()));
		return dto;
	}

	private static List<TargetAndOutcomeDTO> getTargets(
			Set<TargetAndOutcome> targets) {

		List<TargetAndOutcomeDTO> targetsDTO = new ArrayList<>();
		for(TargetAndOutcome target: targets){
			TargetAndOutcomeDTO dto = new TargetAndOutcomeDTO();
			dto.setId(target.getId());
			dto.setActualOutcome(target.getActualOutcome());
			dto.setMeasure(target.getMeasure());
			dto.setOutcomeRemarks(target.getOutcomeRemarks());
			dto.setTarget(target.getTarget());
			targetsDTO.add(dto);
		}
		
		return targetsDTO;
	}

//	private static List<IsProgramActivity> getActivity(Collection<ProgramDetail> children, boolean loadChildren){
//		return getActivity(children, loadChildren, false);
//	}
	
	private static List<IsProgramDetail> getActivity(Collection<ProgramDetail> children, boolean loadChildren, boolean loadObjectives) {
		List<IsProgramDetail> activity = new ArrayList<>();
		if(children!=null)
			for(ProgramDetail detail: children){
				if(!loadObjectives && detail.getType()==ProgramDetailType.OBJECTIVE){
					continue;
				}
				activity.add(get(detail,loadChildren));
			}
		return activity;
	}

	private static List<ProgramFundDTO> get(Set<ProgramFund> sourceOfFunds) {
		List<ProgramFundDTO> programFundsDTOs = new ArrayList<>();
		
		for(ProgramFund programFund: sourceOfFunds){
			programFundsDTOs.add(get(programFund));
		}
		
		return programFundsDTOs;
	}

	private static ProgramFundDTO get(ProgramFund programFund) {
		ProgramFundDTO programFundDTO = new ProgramFundDTO();
		programFundDTO.setAmount(programFund.getAmount());
		programFundDTO.setFund(get(programFund.getFund()));
		programFundDTO.setId(programFund.getId());
		programFundDTO.setProgramId(programFund.getProgramDetail().getId());
		FundAllocation allocation = programFund.getAllocation();
		if(allocation!=null){
			programFundDTO.setAllocation(allocation.getAllocation());
		}
		
		return programFundDTO;
	}

	private static FundDTO get(Fund fund) {
		
		FundDTO dto = new FundDTO();
		dto.setDescription(fund.getDescription());
		dto.setId(fund.getId());
		dto.setName(fund.getName());
		
		return dto;
	}
		
	private static ProgramDetail get(IsProgramDetail programDTO) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = new ProgramDetail();
		if(programDTO.getId()!=null){
			detail = dao.getProgramDetail(programDTO.getId());
		}
		
		if(programDTO.getParentId()!=null){
			detail.setParent(dao.getProgramDetail(programDTO.getParentId()));
		}
		
		//detail.setActual(String);
		detail.setActualAmount(programDTO.getActualAmount()==null? 0.0: programDTO.getActualAmount());
		detail.setDescription(programDTO.getDescription());
		detail.setEndDate(programDTO.getEndDate());
		//detail.setIndicator(String);
		detail.setName(programDTO.getName());
		detail.setPeriod(get(programDTO.getPeriod()));
		detail.setSourceOfFunds(get(programDTO.getFunding()));
		detail.setStartDate(programDTO.getStartDate());
		
		if(programDTO.getObjectives()!=null)
			detail.setObjectives(getProgramChildren(programDTO.getObjectives()));
		
		//detail.setTarget(String);
		//detail.setTargets(Set<TargetAndOutcome>);
		detail.setType(programDTO.getType());
		List<TargetAndOutcomeDTO> targets = programDTO.getTargetsAndOutcomes();
		detail.setTargets(getTargets(targets));
		
		return detail;
	}

	private static Collection<TargetAndOutcome> getTargets(List<TargetAndOutcomeDTO> targets) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		
		List<TargetAndOutcome> targetsAndOutcomes = new ArrayList<>();
		if(targets!=null)
		for(TargetAndOutcomeDTO dto: targets){
			TargetAndOutcome target = new TargetAndOutcome();
			if(dto.getId()!=null){
				target = dao.getById(TargetAndOutcome.class, dto.getId());
			}
			target.setMeasure(dto.getMeasure());
			target.setTarget(dto.getTarget());
			target.setActualOutcome(dto.getActualOutcome());
			target.setOutcomeRemarks(dto.getOutcomeRemarks());
			
			targetsAndOutcomes.add(target);
		}
		return targetsAndOutcomes;
	}

	private static Set<ProgramDetail> getProgramChildren(
			List<IsProgramDetail> objectives) {
		
		if(objectives==null){
			return new HashSet<>();
		}
		
		Set<ProgramDetail> details = new HashSet<>();
		for(IsProgramDetail activity: objectives){
			ProgramDetail detail = get(activity);
			details.add(detail);
		}
		
		return details;
	}

	private static Set<ProgramFund> get(List<ProgramFundDTO> fundingDtos) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		Set<ProgramFund> funding = new HashSet<>();
		
		if(fundingDtos!=null)
		for(ProgramFundDTO dto: fundingDtos){
			if(dto.getId()!=null && dto.getFund()!=null){
				//find previous fund
				
				long fundid = dto.getFund().getId();
				long previousFundId = dao.getPreviousFundId(dto.getId());			
				
				if(previousFundId!=fundid){
					//funds changed
					dto.setId(null); //generate new entry
				}
			}
			
			ProgramFund programFund = get(dto);
			
			funding.add(programFund);
		}
		return funding;
	}

	private static ProgramFund get(ProgramFundDTO dto) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramFund programFund = new ProgramFund();
		if(dto.getId()!=null){
			programFund = dao.getById(ProgramFund.class,dto.getId());
		}
		programFund.setAmount(dto.getAmount());
		programFund.setFund(get(dto.getFund()));
		
		return programFund;
	}

	private static Fund get(FundDTO dto) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		Fund fund = new Fund();
		
		if(dto.getId()!=null){
			fund = dao.getById(Fund.class, dto.getId());
		}
		
		fund.setDescription(dto.getDescription());
		fund.setName(dto.getName());
		
		return fund;
	}

	public static IsProgramDetail getProgramById(Long id, boolean loadChildren,boolean loadObjectives) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = dao.getById(ProgramDetail.class, id);
		if(detail==null){
			return null;
		}
		
		IsProgramDetail activity = get(detail, loadChildren,loadObjectives);
		activity.setProgramSummary(getProgramSummary(detail));
		
		return activity;
	}
	
	public static IsProgramDetail getProgramByCode(String code, Long periodId, 
			boolean loadChildren, boolean loadObjectives){
		
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = dao.getByCodeAndPeriod(code,periodId);
		if(detail==null){
			return null;
		}
		
		IsProgramDetail activity = get(detail, loadChildren,loadObjectives);
		activity.setProgramSummary(getProgramSummary(detail));
		
		return activity;
	}

	/**
	 * List of parent details to be used generate the breadcrumb display 
	 * 
	 * @param detail
	 * @return
	 */
	private static List<ProgramSummary> getProgramSummary(ProgramDetail detail) {
		List<ProgramSummary> summaries = new ArrayList<>();
		getSummary(summaries, detail);
		return summaries;
	}

	private static void getSummary(List<ProgramSummary> summaries,ProgramDetail detail) {
		ProgramSummary summary = new ProgramSummary();
		summary.setId(detail.getId());
		summary.setName(detail.getName());
		summary.setDescription(detail.getDescription());
		summaries.add(summary);
		
		if(detail.getParent()!=null){
			getSummary(summaries,detail.getParent());
		}
	}

	public static List<IsProgramDetail> getPrograms(boolean loadChildren, boolean loadObjectives) {
		
		return getProgramsByPeriod(null,loadChildren,loadObjectives);
	}
	
	public static List<IsProgramDetail> getProgramsByPeriod(Long periodId,boolean loadChildren, boolean loadObjectives) {
		
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		
		List<IsProgramDetail> activities = new ArrayList<>();
		Period period = null;
		
		if(periodId==null){
			 period= dao.getActivePeriod();
		}else{
			period = dao.getPeriod(periodId);
		}
		
		List<ProgramDetail> details = new ArrayList<>();
		
		if(period!=null){
			details = dao.getProgramDetails(period);
		}
		
		for(ProgramDetail detail: details){
			activities.add(get(detail, loadChildren,loadObjectives));
		}	
		
		return activities;
	}

	public static List<IsProgramDetail> getProgramsByType(ProgramDetailType type,
			boolean loadChildren, boolean loadObjectives) {
		return getProgramByTypeAndPeriod(type,null, loadChildren, loadObjectives);
	}
	
	public static List<IsProgramDetail> getProgramByTypeAndPeriod(ProgramDetailType type,
			Long periodId, boolean loadChildren, boolean loadObjectives) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		Period period = null;
		
		if(periodId==null){
			 period= dao.getActivePeriod();
		}else{
			period = dao.getPeriod(periodId);
		}
		
		List<ProgramDetail> details = new ArrayList<>();
		
		if(period!=null){
			details = dao.getProgramDetails(type, period);
		}		
		
		return getActivity(details, loadChildren,loadObjectives|| type==ProgramDetailType.OBJECTIVE);
	}

	public static List<FundDTO> getFunds() {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		List<Fund> funds = dao.getFunds();
		
		List<FundDTO> fundsDto = new ArrayList<>();
		if(funds!=null)
			for(Fund fund: funds){
				fundsDto.add(get(fund));
			}
		return fundsDto;
	}

	public static FundDTO save(FundDTO donor) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		Fund fund = get(donor);
		dao.save(fund);
		
		return get(fund);
	}

	public static PeriodDTO getActivePeriod() {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		Period period = dao.getActivePeriod();
		
		return get(period);
	}

	/**
	 * This method generates taskName for the Task to be performed
	 * and approvalTaskName for the Task Approvers Form
	 * {@link ProgramsPresenter#assignTask} and see how this is created {@link TaskInfo}
	 * 
	 * @param activityId
	 * @return
	 */
	public static Long generateFormFor(Long activityId) {

		if(activityId==null){
			return null;
		}
		
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = dao.getProgramDetail(activityId);
		
		String taskName = "Program-"+ detail.getId();		
		String taskFormCaption ="Task Form - "+detail.getName();

		FormModel form = FormDaoHelper.getFormByName(taskName);
		if(form!=null){
			return form.getId();
		}
				
		//Approval Form
//		String approvalTaskName = taskName+"-Approval";
//		String approvalTaskForm = "Task Approval Form - "+detail.getName(); 
		
		//Clone Default Form
		Form model = (Form)FormDaoHelper.getFormByName("TASK_TEMPLATE_FORM");
		if(model!=null){
			model = model.clone();
			model.setCaption(taskFormCaption);
			model.setName(taskName);
			for(Property prop: model.getProperties()){
				if(prop.getName().equals("NAME")){
					prop.setValue(new StringValue(taskName));
				}
				
				if(prop.getName().equals("CAPTION")){
					prop.setValue(new StringValue(taskFormCaption));
				}
				
				if(prop.getName().equals("HELP")){
					prop.setValue(new StringValue(detail.getDescription()));
				}
			}
		}else{
			model = generateForm(taskName,taskFormCaption, detail.getDescription());
		}
		
		model = FormDaoHelper.createForm(model, Boolean.FALSE);
		
		return model.getId();
	}

	private static Form generateForm(String taskName, String taskFormCaption, String description) {

		Form form = new Form();
		form.setName(taskName);
		form.setCaption(taskFormCaption);
		
		form.setProperties(Arrays.asList(
				new Property("NAME", "Name", DataType.STRING, new StringValue(taskName)),
				new Property("CAPTION", "Caption", DataType.STRING,new StringValue(taskFormCaption)),
				new Property("HELP", "Help", DataType.STRING,new StringValue(description))));
		
		return form;
	}
	
	/**
	 * 
	 * @param taskInfo
	 */
	public static void saveTaskInfo(TaskInfo taskInfo){
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = dao.getProgramDetail(taskInfo.getActivityId());
		
		Map<ParticipantType, List<OrgEntity>> accessAllocations = taskInfo.getParticipants();
		
		List<ProgramAccess> permissions = new ArrayList<>();
		
		if(accessAllocations!=null)
		for(ParticipantType type: accessAllocations.keySet()){
			List<OrgEntity> entities = accessAllocations.get(type);
			for(OrgEntity entity: entities){
				ProgramAccess access = new ProgramAccess();
				access.setType(type);
				if(entity instanceof HTUser){
					access.setUserId(entity.getEntityId());
				}else{
					access.setGroupId(entity.getEntityId());
				}
				permissions.add(access);
			}
		}
		
		detail.setAccess(permissions);
		dao.save(detail);
	}
	
	public static TaskInfo getTaskInfo(Long programId){
		TaskInfo info = new TaskInfo();
		info.setActivityId(programId);
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = dao.getProgramDetail(programId);
		Collection<ProgramAccess> permissions = detail.getProgramAccess();
		if(permissions!=null)
		for(ProgramAccess permission: permissions){
			ParticipantType type = permission.getType();
			OrgEntity entity = permission.getUserId()==null? 
					LoginHelper.get().getGroupById(permission.getGroupId()):
				LoginHelper.get().getUser(permission.getUserId());
					
			info.addParticipant(entity, type);
		}		
		
		return info;
	}
}
