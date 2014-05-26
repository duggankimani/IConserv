package com.wira.pmgt.server.dao;

import java.util.ArrayList;
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
	
}
