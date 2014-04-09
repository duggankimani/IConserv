/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.pmgt.server.executor.impl;

import java.util.List;
import javax.persistence.EntityManager;

import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.executor.api.ExecutorQueryService;
import com.wira.pmgt.server.executor.entities.ErrorInfo;
import com.wira.pmgt.server.executor.entities.RequestInfo;

/**
 *
 * @author salaboy
 */
public class ExecutorQueryServiceImpl implements ExecutorQueryService{
    
    private EntityManager em;

    public ExecutorQueryServiceImpl() {
    	em = DB.getEntityManager();
    }
    
    public List<RequestInfo> getQueuedRequests() {
        List resultList = em.createNamedQuery("QueuedRequests").getResultList();
        return resultList;
    }

    public List<RequestInfo> getExecutedRequests() {
        List resultList = em.createNamedQuery("ExecutedRequests").getResultList();
        return resultList;
    }

    public List<RequestInfo> getInErrorRequests() {
        List resultList = em.createNamedQuery("InErrorRequests").getResultList();
        return resultList;
    }

    public List<RequestInfo> getCancelledRequests() {
        List resultList = em.createNamedQuery("CancelledRequests").getResultList();
        return resultList;
    }

    public List<ErrorInfo> getAllErrors() {
        List resultList = em.createNamedQuery("GetAllErrors").getResultList();
        return resultList;
    }
    
    public List<RequestInfo> getAllRequests() {
        List resultList = em.createNamedQuery("GetAllRequests").getResultList();
        return resultList;
    }

}
