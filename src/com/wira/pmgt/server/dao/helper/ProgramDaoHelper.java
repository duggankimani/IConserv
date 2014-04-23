package com.wira.pmgt.server.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wira.pmgt.server.dao.ProgramDaoImpl;
import com.wira.pmgt.server.dao.biz.model.Fund;
import com.wira.pmgt.server.dao.biz.model.Period;
import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.dao.biz.model.ProgramFund;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;

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

	public static ProgramDTO save(IsProgramActivity programDTO) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail program = get(programDTO);
		dao.save(program);
				
		return get(program,false);
	}

	private static ProgramDTO get(ProgramDetail program, boolean loadChildren) {
		
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
		//dto.setTargetsAndOutcomes(List<TargetAndOutcomeDTO>);
		dto.setType(program.getType());
		
		if(loadChildren){
			dto.setChildren(getActivity(program.getChildren(),loadChildren));
		}
		
		return dto;
	}

	private static List<IsProgramActivity> getActivity(Collection<ProgramDetail> children, boolean loadChildren) {
		List<IsProgramActivity> activity = new ArrayList<>();
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
		
		return programFundDTO;
	}

	private static FundDTO get(Fund fund) {
		FundDTO dto = new FundDTO();
		dto.setDescription(fund.getDescription());
		dto.setId(fund.getId());
		dto.setName(fund.getName());
		
		return dto;
	}

	private static ProgramDetail get(IsProgramActivity programDTO) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = new ProgramDetail();
		if(programDTO.getId()!=null){
			detail = dao.getProgramDetail(programDTO.getId());
		}
		
		if(programDTO.getParentId()!=null){
			detail.setParent(dao.getProgramDetail(programDTO.getParentId()));
		}
		
		//detail.setActual(String);
		detail.setActualAmount(programDTO.getActualAmount());
		detail.setBudgetAmount(programDTO.getBudgetAmount());
		detail.setDescription(programDTO.getDescription());
		detail.setEndDate(programDTO.getEndDate());
		//detail.setIndicator(String);
		detail.setName(programDTO.getName());
		detail.setPeriod(get(programDTO.getPeriod()));
		detail.setSourceOfFunds(get(programDTO.getFunding()));
		detail.setStartDate(programDTO.getStartDate());
		//detail.setTarget(String);
		//detail.setTargets(Set<TargetAndOutcome>);
		detail.setType(programDTO.getType());
		
		return detail;
	}

	private static Set<ProgramFund> get(List<ProgramFundDTO> fundingDtos) {
		
		Set<ProgramFund> funding = new HashSet<>();
		
		if(fundingDtos!=null)
		for(ProgramFundDTO dto: fundingDtos){
			ProgramFund fund = get(dto);
			funding.add(fund);
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

	public static IsProgramActivity getProgramById(Long id, boolean loadChildren) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		ProgramDetail detail = dao.getById(ProgramDetail.class, id);
		IsProgramActivity activity = get(detail, loadChildren);
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

	public static List<IsProgramActivity> getPrograms(boolean loadChildren) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		List<IsProgramActivity> activities = new ArrayList<>();
		
		List<ProgramDetail> details = dao.getProgramDetails();
		
		for(ProgramDetail detail: details){
			activities.add(get(detail, loadChildren));
		}	
		
		return activities;
	}

	public static List<IsProgramActivity> getPrograms(ProgramDetailType type,
			boolean loadChildren) {
		ProgramDaoImpl dao = DB.getProgramDaoImpl();
		
		List<ProgramDetail> details = dao.getProgramDetails(type);
		
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
}
