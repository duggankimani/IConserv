package com.wira.pmgt.server.dao.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.PermissionType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.StringValue;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.Field;
import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.model.form.FormModel;
import com.wira.pmgt.shared.model.form.Property;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.Metric;
import com.wira.pmgt.shared.model.program.PerformanceModel;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramAnalysis;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramStatus;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.ProgramTaskForm;
import com.wira.pmgt.shared.model.program.ProgramTreeModel;
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
		boolean isNew = programDTO.getId()==null;
		
		ProgramStatus previousStatus = null;
		if(programDTO.getId()!=null){
			previousStatus = dao.getStatus(programDTO.getId()); 
		}
		
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
		if(isNew && program.getType()==ProgramDetailType.PROGRAM){
			createWriteAccess(program);
		}
		dao.flush();
		//dao.refresh(program);
		
		saveTargetsAndOutcomes(programDTO, program, previousStatus);
		saveFunding(programDTO, program, previousStatus);
		dao.flush();
		//dao.refresh(managedPO);
		
		//Database triggers update fund amounts & we'd like to get the committed values from the database
		//in the get method below
	
		//reload program
		dao.refresh(program);
		
		return get(program,false);
	}
	
	private static void createWriteAccess(ProgramDetail program) {
		TaskInfo info = new TaskInfo();
		info.setActivityId(program.getId());
		info.addParticipant(SessionHelper.getCurrentUser(), PermissionType.CAN_EDIT);
		
		saveTaskInfo(info);
	}

	private static void saveTargetsAndOutcomes(IsProgramDetail programDTO, ProgramDetail detail, ProgramStatus previousStatus) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		
		Collection<TargetAndOutcome> dbTargets =  getTargets(programDTO.getTargetsAndOutcomes());
		
		boolean isSave = true;
		//Targets And Outcomes
		if(previousStatus!=null){
			if(previousStatus!=null && previousStatus.equals(ProgramStatus.CLOSED)){
				if(programDTO.getStatus()==null || !programDTO.getStatus().equals(ProgramStatus.CLOSED)){
					//reset program targets and outcomes
					for(TargetAndOutcome target: dbTargets){
						resetTarget(target);
					}
					
					isSave=false;
				}
			}
		}
		
		if(isSave){
			List<Long> ids = new ArrayList<>();
			for(TargetAndOutcome target: dbTargets){
				target.setProgramDetail(detail);
				dao.save(target);
				ids.add(target.getId());				
			}
			
			dao.deleteTargetsNotIn(detail, ids);
		}
		
	}

	private static void resetTarget(TargetAndOutcome target) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		double outcome= dao.getOutcome(target.getKey(), target.getProgramDetail().getId());
		target.setActualOutcome(outcome);
	}

	private static void saveFunding(IsProgramDetail programDTO, ProgramDetail managedPO, ProgramStatus previousStatus) {
		List<ProgramFundDTO> funding = programDTO.getFunding();
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		
		List<Long> idsToDelete= new ArrayList<>();
		if(programDTO.getId()!=null){
			idsToDelete = dao.getPreviousFundIds(programDTO.getId()); //Ids to be deleted
		}
		
		if(funding!=null)
			for(ProgramFundDTO dto: funding){
				if(dto.getId()!=null && dto.getFund()!=null){
					//find previous fund
					idsToDelete.remove(dto.getId());
					long donorId = dto.getFund().getId();
					long previousDonorId = dao.getSourceOfFundId(dto.getId());
					
					//Check if donor/ source of funds has changed
					if(previousDonorId!=donorId){
						ProgramFund pfund = dao.getById(ProgramFund.class, dto.getId());
						assert pfund!=null;
						log.debug("Changing Donor for PFid "+pfund+" ["+donorId+" >> "+previousDonorId+"]");
						dao.delete(pfund);
						//funds changed
						dto.setId(null); //generate new programfund entry
					}
				}
				
				ProgramFund fund = get(programDTO.getType(),dto);
				log.debug("Saving "+fund);
				if(previousStatus!=null && previousStatus.equals(ProgramStatus.CLOSED)){
					if(programDTO.getStatus()==null || !programDTO.getStatus().equals(ProgramStatus.CLOSED)){
						resetProgramFund(fund);
					}
				}
				fund.setProgramDetail(managedPO);
				dao.save(fund);
			}
		
		if(!idsToDelete.isEmpty()){
			dao.deleteProgramFunds(idsToDelete);
		}
		
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
		dto.setRemarks(program.getRemarks());
		//dto.setActualAmount(program.getActualAmount());
		dto.setCommitedAmount(program.getCommitedAmount());
		
		dto.setAssigned(DB.getProgramDaoImpl().isProgramAssigned(dto.getId()));
		
		if(program.getActivityOutcome()!=null){
			dto.setActivityOutcomeId(program.getActivityOutcome().getId());
		}
		
		//Program Outcomes
		if(program.getProgramOutcomes()!=null){
			for(ProgramDetail outcome: program.getProgramOutcomes()){
				IsProgramDetail outcomeDto = get(outcome,false);
				outcomeDto.setPeriod(dto.getPeriod());
				dto.addProgramOutcomes(outcomeDto);
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
		
//		Set<ProgramFund> funds = get(programDTO.getFunding());
//		detail.setSourceOfFunds(funds);		
//		if(detail.getStatus()!=null && detail.getStatus().equals(ProgramStatus.CLOSED)){
//			if(programDTO.getStatus()==null || !programDTO.getStatus().equals(ProgramStatus.CLOSED)){
//				resetProgramFunds(funds);
//			}
//		}
		
		
		
		//detail.setActual(String);
		/**
		 * This generates a conflict between value keyed in directly and value computed by programfund trigger#procedure
		 */
		//detail.setActualAmount(programDTO.getActualAmount()==null? 0.0: programDTO.getActualAmount());
		detail.setDescription(programDTO.getDescription());
		detail.setEndDate(programDTO.getEndDate());
		detail.setStatus(programDTO.getStatus());
		
		detail.setName(programDTO.getName());
		
		if(programDTO.getPeriod()!=null && programDTO.getPeriod().getId()!=null){
			detail.setPeriod(dao.getPeriod(programDTO.getPeriod().getId()));
		}
		
		detail.setStartDate(programDTO.getStartDate());
		detail.setBudgetLine(programDTO.getBudgetLine());
		detail.setType(programDTO.getType());
		
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

	private static void resetProgramFunds(Collection<ProgramFund> funds) {
		if(funds==null){
			return;
		}
		
		for(ProgramFund programFund: funds){
			resetProgramFund(programFund);
		}
	}

	private static void resetProgramFund(ProgramFund programFund) {
		log.debug("Resetting fund "+programFund);
		ProgramDaoImpl dao = DB.getProgramDaoImpl();	
		Double[] amounts = dao.getAmounts(programFund.getProgramDetail().getId(), programFund.getFund().getId());
		programFund.setCommitedAmount(amounts[0]);
		programFund.setActualAmount(amounts[1]);
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

	/**
	 * This method transforms ProgramFundDTO's into ProgramFund entities
	 * <br>
	 * If fund(donor) has changed, this is treated like a new source of fund entry
	 * 
	 * @param fundingDtos
	 * @return
	 */
	private static Set<ProgramFund> get(ProgramDetailType type,List<ProgramFundDTO> fundingDtos) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		Set<ProgramFund> funding = new HashSet<>();
		
		if(fundingDtos!=null)
		for(ProgramFundDTO dto: fundingDtos){
			if(dto.getId()!=null && dto.getFund()!=null){
				//find previous fund
				
				long fundid = dto.getFund().getId();
				long previousFundId = dao.getSourceOfFundId(dto.getId());			
				
				if(previousFundId!=fundid){
					//funds changed
					dto.setId(null); //generate new programfund entry
				}
			}

			
			ProgramFund programFund = get(type,dto);
			funding.add(programFund);
		}
		return funding;
	}

	private static ProgramFund get(ProgramDetailType type,ProgramFundDTO dto) {
		
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramFund programFund = new ProgramFund();
		if(dto.getId()!=null){
			programFund = dao.getById(ProgramFund.class,dto.getId());
		}
		//log.debug("READRING ProgramFund For Save: Id="+dto.getId()+", BudgetAmount= "+);
		programFund.setAmount(dto.getAmount());
		Long fundId = dto.getFund().getId();
		if(fundId!=null){
			programFund.setFund(dao.getById(Fund.class, fundId));
		}
		
		//programFund.setFund(get(dto.getFund()));
		/**
		 * Programs Actuals are never keyed in, they are calculated; therefor cannot be set here.
		 */
		if(!(type==ProgramDetailType.PROGRAM)){
			programFund.setActualAmount(dto.getActual());
		}
		
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
			Long periodId,boolean loadChildren) {
		return getProgramByTypeAndPeriod(type,periodId, loadChildren);
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
		
		details = dao.getProgramDetails(type, period);
		
		return getActivity(details, loadChildren);
	}

	public static List<FundDTO> getFunds(Long parentId) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		List<Fund> funds = new ArrayList<>();
		List<FundDTO> fundsDto = new ArrayList<>();
		
		if(parentId==null){
			funds = dao.getFunds();
		}else{
			//find all funds available to this program/activity's parent
			ProgramDetail parent = dao.getProgramDetail(parentId);
			assert parent!=null;

			if(parent!=null){
				Collection<ProgramFund> pfunds = parent.getSourceOfFunds();
				if(pfunds!=null)
				for(ProgramFund fund: pfunds){
					funds.add(fund.getFund());
				}
			}else{
				//no parent
				funds = dao.getFunds();
			}
		}
		
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
		FormModel frm = FormDaoHelper.getFormByName("TASK_TEMPLATE_FORM");
		Form model =null;
		if(frm!=null){
			model = (Form)frm;
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
		if(key==null || model==null){
			return;
		}
		List<Field> fields = model.getFields();
		if(fields==null){
			fields = new ArrayList<>();
		}
		
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
		
		Map<PermissionType, List<OrgEntity>> accessAllocations = taskInfo.getParticipants();
		
		List<ProgramAccess> permissions = new ArrayList<>();
		
		if(accessAllocations!=null)
		for(PermissionType type: accessAllocations.keySet()){
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
			PermissionType type = permission.getType();
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
		
		log.debug("##Approved Form Values to Map::");
		for(String k: values.keySet()){
			Value v = values.get(k);
			Object o = v==null? null: v.getValue();
			log.debug("{"+k+":"+o+"}");
		}
		log.debug("##Done Printing Form Values");
		
		Value remarks = values.get("remarks");
		if(remarks!=null && remarks.getValue()!=null){
			detail.setRemarks(remarks.getValue().toString());
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
					log.debug("ProgramDaoHelper#updateTargetAndOutcome No form data for target key "+key);
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
						log.debug("ProgramDaoHelper#updateTargetAndOutcome " +
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

	public static List<IsProgramDetail> loadById(Long programId, Long outcomeId, 
			ProgramDetailType programType, Long periodId,boolean isLoadChildren) {
		List<IsProgramDetail> activities = new ArrayList<>();
		if(programId!=null && outcomeId!=null){
			activities.addAll(
					getProgramDetailsByOutcome(programId, outcomeId,isLoadChildren));
		}else if(programId!=null){
			IsProgramDetail activity = getProgramById(programId, isLoadChildren);
			if(activity!=null){
				activities.add(activity);
			}
		}else if(programType!=null){
			activities.addAll(getProgramsByType(programType,periodId, isLoadChildren));
		}else{
			activities.addAll(getPrograms(isLoadChildren));
		}
		
		return activities;
	}

	public static List<IsProgramDetail> loadByCode(String code, Long periodId, ProgramDetailType programType, boolean isLoadChildren) {
		List<IsProgramDetail> activities = new ArrayList<>();
		if(code!=null){
			IsProgramDetail activity = ProgramDaoHelper.getProgramByCode(code, periodId,
					isLoadChildren);
			
			if(activity!=null){
				activities.add(activity);
			}
		}else if(programType!=null){
			activities.addAll(ProgramDaoHelper.getProgramByTypeAndPeriod(programType, periodId,
					isLoadChildren));
		}else{
			activities.addAll(ProgramDaoHelper.getProgramsByPeriod(periodId,
					isLoadChildren));
		}
		
		return activities;
	}

	public static List<PerformanceModel> getPerformanceData(Metric metric, Long periodId) {
		if(periodId==null){
			Period period = DB.getProgramDaoImpl().getActivePeriod();
			if(period!=null)
				periodId = period.getId();
		}
		
		return DB.getProgramDaoImpl().getBudgetPerformanceData(metric, periodId);
	}

	public static List<ProgramTreeModel> getProgramTree(Long periodId,Long programId) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		List<ProgramTreeModel> list = null;
		
		if(periodId==null){
			Period p = dao.getActivePeriod();
			if (p!=null) {
				periodId = p.getId();
			}
		}
		
		if(programId!=null){
			list= dao.getProgramTree(periodId, Arrays.asList(programId));
		}else{
			list = dao.getProgramTree(periodId, dao.getProgramIds(SessionHelper.getCurrentUser().getUserId()));
		}
		
		List<ProgramTreeModel> rootList = new ArrayList<>();
		List<ProgramTreeModel> outcomes=new ArrayList<>();
		ProgramTreeModel outcome=null;
		ProgramTreeModel program=null;
		Map<Long, ProgramTreeModel> idActivityMap = new HashMap<Long, ProgramTreeModel>();
		
		for(ProgramTreeModel model: list){
			if(model.getType()==ProgramDetailType.PROGRAM){
				program = model;
				model.setChildren(outcomes);
				rootList.add(program);
				outcomes = new ArrayList<>();
			}
			
			if(model.getType()==ProgramDetailType.ACTIVITY){
				
				if(outcome==null || !model.getOutcomeId().equals(outcome.getId())){
					outcome = new ProgramTreeModel();
					outcome.setName(model.getOutcomeName());
					outcome.setId(model.getOutcomeId());
					outcome.setType(ProgramDetailType.OUTCOME);
					outcomes.add(outcome);
				}
				
				model.setParentNodeId(outcome.getId());
				outcome.addChild(model);
			
				//Check if placeholder already exists
				//happens if children are loaded before the parent
				
				{
					//Check if placeholder for this task already exists
					//happens if the children of a task/activity are loaded before the parent
					ProgramTreeModel placeHolder = idActivityMap.get(model.getId());
					if(placeHolder!=null){
						//copy children from placeholder
						model.setChildren(placeHolder.getChildren());
					}
					idActivityMap.put(model.getId(), model);
				}
			}
			
			if(model.getType()==ProgramDetailType.TASK){
				
				ProgramTreeModel parent=idActivityMap.get(model.getParentId());
				
				if(parent==null){
					//Create a place holder
					ProgramTreeModel placeHolder = new ProgramTreeModel();
					placeHolder.setId(model.getParentId());
					placeHolder.setName("PlaceHolder for Item: "+model.getParentId());
					parent = placeHolder;
					idActivityMap.put(model.getParentId(), placeHolder);
				}
				
				model.setParentNodeId(parent.getId());
				parent.addChild(model);
				
				{
					//Check if placeholder for this task already exists
					//happens if the children of a task/activity are loaded before the parent
					ProgramTreeModel placeHolder = idActivityMap.get(model.getId());
					if(placeHolder!=null){
						//copy children from placeholder
						model.setChildren(placeHolder.getChildren());
					}
					idActivityMap.put(model.getId(), model);
				}
			}
			
		}		
		
		return rootList;
	}

	public static void moveParent(Long itemToMoveId, Long parentId) {
		DB.getProgramDaoImpl().moveToParent(itemToMoveId,parentId);
	}

	public static void moveToOutcome(Long itemToMoveId, Long outcomeId) {
		DB.getProgramDaoImpl().moveToOutcome(itemToMoveId,outcomeId);
	}
	
	public static HashMap<Long, PermissionType> getUserPermissions(String userId, Long periodId){
		if(userId==null){
			userId = SessionHelper.getCurrentUser().getUserId();
		}
		
		if(periodId==null){
			Period period = DB.getProgramDaoImpl().getActivePeriod();
			if(period!=null){
				periodId = period.getId();
			}
		}
		
		List<UserGroup> groups  = LoginHelper.get().getGroupsForUser(userId); 
		List<String> groupIds = new ArrayList<>();
		for(UserGroup g: groups){
			groupIds.add(g.getEntityId());
		}
		
		return DB.getProgramDaoImpl().getPermissions(userId, groupIds, periodId);
	}

	public static PeriodDTO getPeriod(Long periodId) {
		if(periodId==null){
			return getActivePeriod();
		}
		
		return get(DB.getProgramDaoImpl().getPeriod(periodId));
	}

}
