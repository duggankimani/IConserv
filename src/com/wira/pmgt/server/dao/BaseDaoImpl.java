package com.wira.pmgt.server.dao;

import javax.persistence.EntityManager;

import com.wira.pmgt.server.dao.model.PO;

public class BaseDaoImpl {

	protected EntityManager em;
	
	public BaseDaoImpl(EntityManager em){
		this.em = em;
	}
		
	public void save(PO po){
		em.persist(po);
	}
	
	public void delete(PO po){
		em.remove(po);
	}
}
