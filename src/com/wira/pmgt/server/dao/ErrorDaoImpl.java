package com.wira.pmgt.server.dao;

import javax.persistence.EntityManager;

import com.wira.pmgt.server.dao.model.ErrorLog;

public class ErrorDaoImpl {
	EntityManager em;
	
	public ErrorDaoImpl(EntityManager em){
		this.em = em;
	}
	
	public Long saveError(ErrorLog log){
		em.persist(log);
		
		return log.getId();
	}
	
	public ErrorLog retrieveError(Long logId){
		return em.find(ErrorLog.class, logId);
	}
}
