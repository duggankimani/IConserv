package com.wira.pmgt.server.dao.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import xtension.workitems.UpdateActivityStatus;

import com.wira.pmgt.client.ui.programs.ProgramsPresenter;
import com.wira.pmgt.client.ui.util.StringUtils;
import com.wira.pmgt.server.dao.ProgramDaoImpl;
import com.wira.pmgt.server.dao.biz.model.Fund;
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
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.Field;
import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.model.form.Property;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramAnalysis;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.ProgramTaskForm;
import com.wira.pmgt.shared.model.program.TargetAndOutcomeDTO;

public class ProgramDaoHelper {

	static Logger log = Logger.getLogger(ProgramDaoHelper.class);
	
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
		boolean hasFunds=false;
		//Parent must have a preset set of funds before child source of funds are generated
		if(parent!=null){
			
			Set<ProgramFund> parentSourceOfFunds = parent.getSourceOfFunds();
			Set<Fund> fundSources = new HashSet<>(); 
			for(ProgramFund programFund: parentSourceOfFunds){
				Fund fund = programFund.getFund();
				fundSources.add(fund);				
			}
						
			Set<ProgramFund> childFunds =  program.getSourceOfFunds();
			hasFunds = childFunds!=null && !childFunds.isEmpty();
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
		
		//Database triggers update fund amounts & we'd like to get the committed values from the database
		//in the get method below
		
		return get(program,false);
	}
	
	private static ProgramDTO get(ProgramDetail program, boolean loadChildren) {
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
		dto.setProgress(program.getProgress());
		dto.setRating(program.getRating());
		//dto.setTargetsAndOutcomes(List<TargetAndOutcomeDTO>);
		dto.setType(program.getType());
		dto.setBudgetLine(program.getBudgetLine());
		dto.setActualAmount(program.getActualAmount());
				
		if(program.getActivityOutcome()!=null){
			dto.setActivityOutcomeId(program.getActivityOutcome().getId());
		}
		
		//Program Outcomes
		if(program.getProgramOutcomes()!=null){
			for(ProgramDetail outcome: program.getProgramOutcomes()){
				dto.addProgramOutcomes(get(outcome,false));
			}
		}
		
		if(loadChildren){
			dto.setChildren(getActivity(program.getChildren(),loadChildren));
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
			dto.setKey(target.getKey());
			dto.setActualOutcome(target.getActualOutcome());
			
			targetsDTO.add(dto);
		}
		
		return targetsDTO;
	}

//	private static List<IsProgramActivity> getActivity(Collection<ProgramDetail> children, boolean loadChildren){
//		return getActivity(children, loadChildren, false);
//	}
	
	private static List<IsProgramDetail> getActivity(Collection<ProgramDetail> children, boolean loadChildren) {
		List<IsProgramDetail> activity = new ArrayList<>();
		if(children!=null)
			for(ProgramDetail detail: children){
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
		programFundDTO.setAllocation(programFund.getAllocatedAmount());
		programFundDTO.setActual(programFund.getActualAmount());
		
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
			ProgramDetail parent = dao.getProgramDetail(programDTO.getParentId());
			detail.setParent(parent);
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
		detail.setBudgetLine(programDTO.getBudgetLine());
		//detail.setActualAmount(programDTO.getActualAmount());
		//detail.setTarget(String);
		//detail.setTargets(Set<TargetAndOutcome>);
		detail.setType(programDTO.getType());
		List<TargetAndOutcomeDTO> targets = programDTO.getTargetsAndOutcomes();
		detail.setTargets(getTargets(targets));
		
		if(programDTO.getActivityOutcomeId()!=null){
			detail.setActivityOutcome(dao.getProgramDetail(programDTO.getActivityOutcomeId()));
		}
		
		if(programDTO.getProgramOutcomes()!=null){
			List<ProgramDetail> programOutcomes = new ArrayList<ProgramDetail>();
			for(IsProgramDetail outcome: programDTO.getProgramOutcomes()){
				programOutcomes.add(dao.getProgramDetail(outcome.getId()));
			}
			detail.setProgramOutcomes(programOutcomes);
		}
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
			target.setKey(dto.getKey());
			//target.setActualOutcome(dto.getActualOutcome());
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
		programFund.setActualAmount(dto.getActual());
		
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
		fund.setIsActive(dto.isActive()?1: 0);
		
		return fund;
	}

	public static IsProgramDetail getProgramById(Long id, boolean loadChildren) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = dao.getById(ProgramDetail.class, id);
		if(detail==null){
			return null;
		}
		
		IsProgramDetail activity = get(detail, loadChildren);
		activity.setProgramSummary(getProgramSummary(detail));
		
		return activity;
	}
	
	public static IsProgramDetail getProgramByCode(String code, Long periodId, 
			boolean loadChildren){
		
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = dao.getByCodeAndPeriod(code,periodId);
		if(detail==null){
			return null;
		}
		
		IsProgramDetail activity = get(detail, loadChildren);
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

	public static List<IsProgramDetail> getPrograms(boolean loadChildren) {
		
		return getProgramsByPeriod(null,loadChildren);
	}
	
	public static List<IsProgramDetail> getProgramsByPeriod(Long periodId,boolean loadChildren) {
		
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
			activities.add(get(detail, loadChildren));
		}	
		
		return activities;
	}

	public static List<IsProgramDetail> getProgramsByType(ProgramDetailType type,
			boolean loadChildren) {
		return getProgramByTypeAndPeriod(type,null, loadChildren);
	}
	
	public static List<IsProgramDetail> getProgramByTypeAndPeriod(ProgramDetailType type,
			Long periodId, boolean loadChildren) {
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
		
		return getActivity(details, loadChildren);
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

		Long formId = DB.getFormDao().getFormByName(taskName);
		
		if(formId!=null){
			return formId;
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
		
		Collection<TargetAndOutcome> targets = detail.getTargets();
		if(targets!=null)
			for(TargetAndOutcome target: targets){
				addToForm(model,target.getKey(), target.getMeasure(),DataType.DOUBLE);
			}
		
		Collection<ProgramFund> funds = detail.getSourceOfFunds();
		if(funds!=null){
			for(ProgramFund programFund: funds){
				Fund fund = programFund.getFund();
				String key = "cost_"+fund.getName();
				key = StringUtils.camelCase(key);
				addToForm(model, key, "Cost "+fund.getName(), DataType.DOUBLE);
			}
		}
		
		//addToForm(model,"rating", "Rating", DataType.RATING);
		//addToForm(model,"cost", "Total Expenditure", DataType.DOUBLE);
		
		model = FormDaoHelper.createForm(model, Boolean.FALSE);
		
		return model.getId();
	}

	/**
	 * Add fields to form
	 * 
	 * @param model
	 * @param key
	 * @param measure
	 */
	private static void addToForm(Form model, String key, String measure, DataType type) {
		if(key==null){
			return;
		}
		List<Field> fields = model.getFields();
		
		for(Field fld: fields){
			if(fld.getName().equals(key)){
				return;
			}
		}
		
		Field field = new Field();
		field.setProperties(Arrays.asList(
				new Property("NAME", "Name", DataType.STRING, new StringValue(key)),
				new Property("CAPTION", "Caption", DataType.STRING,new StringValue(measure)),
				new Property("HELP", "Help", DataType.STRING,new StringValue(measure))));
		
		field.setCaption(measure);
		field.setName(key);
		
		field.setType(type);		
		fields.add(field);
		model.setFields(fields);
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

	public static List<ProgramSummary> getProgramCalendar(String userId) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		return dao.getProgramCalendar(userId);
	}

	/**
	 * <p>
	 * This method maps inputs from a Task Form to Program Outcomes
	 * <p>
	 * 
	 * This method is called after Task Approval in the WorkItem Handler {@link UpdateActivityStatus#executeWorkItem}<br>
	 * Some common form values expected include:
	 * Rating (Task Rating)
	 * Cost (Total cost of performing the task)
	 * <p>
	 * We generate keys for each Target&Outcome indicator
	 * e.g. No of Women Participants - generates - noOfWomenParticipants
	 * <br>
	 * These keys are used to map Form values to Program Targets & Outcomes   
	 * 
	 * <p>
	 * Since Both the form engine and the Targets & Outcomes UI use the same API for generating
	 * keys, ensuring field captions match the indicators is key to ensuring data will be mapped correctly 
	 * <p>
	 * @param detail ProgramDetail to be updated
	 * @param values Map of values from the User Task Form
	 */
	public static void updateTargetAndOutcome(ProgramDetail detail,
			Map<String, Value> values) {
		Collection<TargetAndOutcome> targetsAndOutcomes = detail.getTargets();
//		if(targetsAndOutcomes==null || targetsAndOutcomes.isEmpty()){			
//			//copy parent targets and outcomes
//			log.debug("ProgramDaoHelper#updateTargetAndOutcome Copying targets and outcomes from parent of ["+detail.getName()+"]");
//			copyParentTargetsAndOutcomes(detail, detail.getParent());
//			targetsAndOutcomes = detail.getTargets();
//		}
		
		if(targetsAndOutcomes==null || targetsAndOutcomes.isEmpty()){
			log.debug("ProgramDaoHelper#updateTargetAndOutcome No Targets or Outcomes found for ["+detail.getName()+"]");
			return;
		}
		
		for(TargetAndOutcome tao: targetsAndOutcomes){
			String key = tao.getKey();
			assert key!=null;
			
			Value value = values.get(key);
			if(value==null){
				log.warn("ProgramDaoHelper#updateTargetAndOutcome No form data for target key "+key);
				continue;
			}
			
			if(values.get(key)!=null){
				Object val = value.getValue();
				
				if(val==null){
					log.debug("ProgramDaoHelper#updateTargetAndOutcome Reading form values: NULL Value found target key "+key);
					continue;
				}
				
				if(!(val instanceof Number)){
					if(val instanceof String){
						try{
							//Quantitative data expected only
							val = Double.parseDouble(val.toString());
						}catch(Exception e){}
					}
					
					if(!(val instanceof Number)){
						log.warn("ProgramDaoHelper#updateTargetAndOutcome " +
								"Only quantitative values expected "+"[key=" +value.getKey()+", value="+
									value.getValue()+", type="+value.getDataType()+" ] ");
						continue;
					}
				}
				
				Number num = (Number)val;					
				tao.setActualOutcome(num.doubleValue());
			}
		}
		
		Collection<ProgramFund> funds = detail.getSourceOfFunds();
		if(funds!=null){
			for(ProgramFund programFund: funds){
				Fund fund = programFund.getFund();
				String key = "cost_"+fund.getName();
				key = StringUtils.camelCase(key);
				
				Value value = values.get(key);
				if(value==null){
					log.warn("ProgramDaoHelper#updateTargetAndOutcome No form data for target key "+key);
					continue;
				}
				
				Object val= value.getValue();
				if(!(val instanceof Number)){
					
					if(val instanceof String){
						try{
							//Quantitative data expected only
							val = Double.parseDouble(val.toString());
						}catch(Exception e){}
					}
					
					if(!(val instanceof Number)){
						log.warn("ProgramDaoHelper#updateTargetAndOutcome " +
								"Only quantitative values expected "+"[key=" +value.getKey()+", value="+
									value.getValue()+", type="+value.getDataType()+" ] ");
						continue;
					}
				}
				
				Number num = (Number)val;//Actual thus far
				programFund.setActualAmount(num.doubleValue());
				programFund.resetCommited();
			}
		}
		
		DB.getProgramDaoImpl().save(detail);
	}

	/**
	 * Recursively copy parent targets to child node<br/>
	 * --How do we determine which targets are relevant to a child?<br/>
	 * --Option 1: Use the form fields for determination (If a form field has the same key as an indicator,
	 * add the target to the child)<br/>
	 * --Option 2: Let the end users Key in the targets during the planning/setup stage. This increases the amount
	 * of initial data entry the end user has to do, but also provides a better/more reliable set of indicators
	 * <p>
	 * @param child
	 * @param parent
	 */
	private static void copyParentTargetsAndOutcomes(ProgramDetail child,
			ProgramDetail parent) {
		if(parent==null){
			return;
		}
		
		Collection<TargetAndOutcome> targetsAndOutcomes = parent.getTargets();
		if(targetsAndOutcomes==null || targetsAndOutcomes.isEmpty()){			
			//copy parent targets and outcomes
			log.debug("ProgramDaoHelper#updateTargetAndOutcome Copying targets and outcomes from parent of ["+parent.getName()+"]");
			copyParentTargetsAndOutcomes(parent, parent.getParent());
			targetsAndOutcomes = parent.getTargets();
		}
		
		if(targetsAndOutcomes==null || targetsAndOutcomes.isEmpty()){
			log.debug("ProgramDaoHelper#updateTargetAndOutcome No Targets or Outcomes found for ["+parent.getName()+"]");
			return;
		}
		
		//Copy Parent targets to child
		//if()
		
	}

	public static List<ProgramTaskForm> getTaskForms(Long programId) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		List<ProgramTaskForm> taskForms = dao.getTaskFormsForProgram(programId);
		
		return taskForms;
	}

	public static List<ProgramAnalysis> getAnalysisData(Long periodId) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		if(periodId==null){
			periodId = dao.getActivePeriod()==null? null: dao.getActivePeriod().getId();
		}
		
		return dao.getAnalysisData(periodId);
	}

	public static void delete(Long programId) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = dao.getProgramDetail(programId);
		dao.delete(detail);
	}

	public static List<IsProgramDetail> getProgramDetailsByOutcome(
			Long programId, Long outcomeId, boolean loadChildren) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		
		ProgramDetail detail = dao.getProgramDetail(outcomeId);
		IsProgramDetail outcomeDTO = get(detail, false);
		
		List<ProgramDetail> list = dao.getProgramActivitiesByOutcome(programId, outcomeId);
		List<IsProgramDetail> children = getActivity(list, loadChildren);
		outcomeDTO.setChildren(children);
		outcomeDTO.setProgramId(programId);
		return Arrays.asList(outcomeDTO);
	}

//	private static ProgramSummary getSummary(ProgramDetail detail) {
//		ProgramSummary summary = new ProgramSummary();
//		summary.setId(detail.getId());
//		summary.setName(detail.getName());
//		summary.setDescription(detail.getDescription());
//		summary.setStartDate(detail.getStartDate());
//		summary.setEndDate(detail.getEndDate());
//		summary.setStatus(detail.getStatus());
//		if(detail.getParent()!=null){
//			summary.setParentId(detail.getParent().getId());
//		}
//		
//		summary.setType(detail.getType());
//		return summary;
//	}
}
