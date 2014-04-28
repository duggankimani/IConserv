package com.wira.pmgt.server.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.wira.pmgt.server.dao.biz.model.Fund;
import com.wira.pmgt.server.dao.biz.model.Period;
import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.shared.model.ProgramDetailType;

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
	
	public List<ProgramDetail> getProgramDetails(ProgramDetailType type){
		Query query = em.createNamedQuery("ProgramDetail.findByType")
				.setParameter("type", type)
				.setParameter("period", getActivePeriod())
				.setParameter("isActive", 1);
		
		if(type==null){
			return getProgramDetails();
		}
		
		return getResultList(query);
	}

	public List<ProgramDetail> getProgramDetails() {
		Query query = em.createNamedQuery("ProgramDetail.findAll")
				.setParameter("isActive", 1);
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
	
	
}
