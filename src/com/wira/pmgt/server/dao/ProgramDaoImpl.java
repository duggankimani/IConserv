package com.wira.pmgt.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.wira.pmgt.server.dao.biz.model.Fund;
import com.wira.pmgt.server.dao.biz.model.Period;
import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.program.ProgramStatus;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.ProgramTaskForm;

/**
 * Dao Implementation Class for managing
 * Fund, ProgramFund, Program, ProgramDetail 
 * <p><p>
 * @author duggan
 *
 */
public class ProgramDaoImpl extends BaseDaoImpl{

	private Logger log = Logger.getLogger(ProgramDaoImpl.class);
	
	public ProgramDaoImpl(EntityManager em) {
		super(em);
	}
	
	public Period getActivePeriod(){
		Query query = em.createNamedQuery("Period.findActive")
				.setParameter("isActive", 1)
				.setParameter("now", new Date());
		
		List<Period> periods = getResultList(query);
		
		if(periods!=null && !periods.isEmpty()){
			if(periods.size()==1){
				return periods.get(0);
			}				
			if(periods.size()>1){
				Period period = periods.get(0);
				log.error("Period.findActive returns more than 1 active periods: using first result ["+period+"]");
				return period;
			}
		}
		
		return null;
	}
	
	public List<Period> getPeriods(){
		Query query = em.createNamedQuery("Period.findAll");
		return getResultList(query);
	}
	
	public List<Fund> getFunds(){
		Query query = em.createNamedQuery("Fund.findAll").setParameter("isActive", 1);
		return getResultList(query);
	}
	
	public List<ProgramDetail> getProgramDetails(ProgramDetailType type, Period period){
		return getProgramDetails(type, period, getCurrentUserId(), getCurrentUserGroups());
	}
	
	private String getCurrentUserId() {
		HTUser user = SessionHelper.getCurrentUser();
		assert user!=null;
		
		return user.getUserId();
	}

	private List<String> getCurrentUserGroups() {
		String userId = getCurrentUserId();
		return getCurrentUserGroups(userId);
	}
	
	private List<String> getCurrentUserGroups(String userId) {
		
		List<UserGroup> groups = LoginHelper.get().getGroupsForUser(userId);
		if(groups==null){
			log.warn("Get Program Details- No Results Due To: User '"+userId+"'has no groups");
			return new ArrayList<>();
		}
		
		List<String> groupIds = new ArrayList<>();
		for(UserGroup group: groups){
			groupIds.add(group.getEntityId());
		}
		
		return groupIds;
	}

	public List<ProgramDetail> getProgramDetails(ProgramDetailType type, Period period,String user, List<String> groups){
		if(type==null){
			return getProgramDetails(period);
		}
		
		Query query = em.createNamedQuery("ProgramDetail.findByType")
				.setParameter("isCurrentUserAdmin", groups.contains("ADMIN"))
				.setParameter("userId", user)
				.setParameter("groupIds", groups)
				.setParameter("type", type)
				.setParameter("period", period)
				.setParameter("isActive", 1);
		
		return getResultList(query);
	}

	public List<ProgramDetail> getProgramDetails(Period period) {		
		return getProgramDetails(period,getCurrentUserId() , getCurrentUserGroups());
	}
	
	public List<ProgramDetail> getProgramDetails(Period period, String user, List<String> groups) {
		if(groups==null || groups.isEmpty()){
			log.warn("Get Program Details- No Results Due To: User '"+user+"'has no groups");
			return new ArrayList<>();
		}
		
		Query query = em.createNamedQuery("ProgramDetail.findAll")
		.setParameter("isCurrentUserAdmin", groups.contains("ADMIN"))
		.setParameter("userId", user)
		.setParameter("groupIds", groups)
		.setParameter("isActive", 1)
		.setParameter("period", period);
		
		return getResultList(query);
	}

	public Period getPeriod(Long id) {
		Query query = em.createNamedQuery("Period.findById").setParameter("id", id);
		return getSingleResultOrNull(query);
	}

	public ProgramDetail getProgramDetail(Long id) {
		Query query = em.createNamedQuery("ProgramDetail.findById").setParameter("id", id);
		return getSingleResultOrNull(query);
	}

	public long getPreviousFundId(Long programFundId) {
		Query query = em.createNativeQuery("select fundid from ProgramFund where id=?")
				.setParameter(1, programFundId);
		return ((Number) query.getSingleResult()).longValue();
	}

	public ProgramDetail getByCodeAndPeriod(String code, Long periodId) {

		Period period = null;
		
		if(periodId==null){
			 period= getActivePeriod();
		}else{
			period = getPeriod(periodId);
		}
		
		if(period==null)
			return null;
		
		Query query = em.createNamedQuery("ProgramDetail.findByCodeAndPeriod")
				.setParameter("code", code)
				.setParameter("period", period);
		
		return getSingleResultOrNull(query);
	}
	
	public List<ProgramSummary> getProgramCalendar(String userId){
		List<Long> ids = getProgramIds(userId);
		if(ids.isEmpty()){
			log.warn("No ids found......... Cannot load calendar for current user");
			return new ArrayList<>();
		}
		
		log.debug("Ids found >> "+ids);
		
		Query query = em.createNamedQuery("ProgramDetail.getCalendar")
				.setParameter("parentIds", ids)
				.setParameter("statusCreated", ProgramStatus.CREATED.name())
				.setParameter("currentDate", new Date())
				.setParameter("statusClosed", ProgramStatus.CLOSED.name());	
		
		List<Object[]> rows = getResultList(query); 
		
		List<ProgramSummary> summaries = new ArrayList<>();
		for(Object[] row: rows){
			//programId,id,parentid,type,startdate,enddate,status
			int i=0;
			Object value=null;
			Long programId= (value=row[i++])==null? null: new Long(value.toString());
			Long id=(value=row[i++])==null? null: new Long(value.toString());
			Long parentid=(value=row[i++])==null? null: new Long(value.toString());
			String type=(value=row[i++])==null? null: value.toString();
			Date startdate=(value=row[i++])==null? null: (Date)value;
			Date enddate=(value=row[i++])==null? null: (Date)value;
			String status=(value=row[i++])==null? ProgramStatus.CREATED.name(): value.toString();
			String name = (value=row[i++])==null? null: value.toString();
			String description=(value=row[i++])==null? null: value.toString();
			
			ProgramSummary summary = new ProgramSummary(name,description,
					programId,id,parentid,ProgramDetailType.valueOf(type),startdate,enddate,
					ProgramStatus.valueOf(status));
			
			summaries.add(summary);
		}
		return summaries;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	private List<Long> getProgramIds(String userId) {
		List<String> groups = getCurrentUserGroups(userId);
		if(groups==null || groups.isEmpty()){
			return new ArrayList<>();
		}
		
		Query query = em.createNamedQuery("ProgramDetail.findAllIds")
		.setParameter("isCurrentUserAdmin", groups.contains("ADMIN"))
		.setParameter("type", ProgramDetailType.PROGRAM)
		.setParameter("userId", userId)
		.setParameter("groupIds", groups)
		.setParameter("isActive", 1)
		.setParameter("period", getActivePeriod());
				
		return getResultList(query);
	}

	public List<ProgramTaskForm> getTaskFormsForProgram(Long programId) {
		List<String> formsNames = Arrays.asList("Program-"+programId,"Program-"+programId+"-Approval" );
		Query query = em.createNamedQuery("ProgramDetail.getTaskForms")
				.setParameter("formNames", formsNames);	
		List<Object[]> rows = getResultList(query); 
		
		List<ProgramTaskForm> forms = new ArrayList<>();
		for(Object[] row: rows){
			int i=0;
			Object value=null;
			Long formId= (value=row[i++])==null? null: new Long(value.toString());
			String caption = (value=row[i++])==null? null: value.toString();
			
			ProgramTaskForm form = new ProgramTaskForm(formId,caption);
			
			forms.add(form);
		}
		
		return forms;
	}
}
