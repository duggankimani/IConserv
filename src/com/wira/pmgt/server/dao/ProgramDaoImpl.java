package com.wira.pmgt.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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

	public ProgramDaoImpl(EntityManager em) {
		super(em);
	}
	
	public Period getActivePeriod(){
		Query query = em.createNamedQuery("Period.findActive").setParameter("isActive", 1);
		return getSingleResultOrNull(query);
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
	
	
}
