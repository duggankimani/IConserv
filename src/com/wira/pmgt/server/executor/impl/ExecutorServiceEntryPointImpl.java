/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.pmgt.server.executor.impl;

import java.util.List;
import javax.inject.Inject;

import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.executor.ExecutorServiceEntryPoint;
import com.wira.pmgt.server.executor.api.CommandCodes;
import com.wira.pmgt.server.executor.api.CommandContext;
import com.wira.pmgt.server.executor.api.Executor;
import com.wira.pmgt.server.executor.api.ExecutorQueryService;
import com.wira.pmgt.server.executor.api.ExecutorRequestAdminService;
import com.wira.pmgt.server.executor.entities.ErrorInfo;
import com.wira.pmgt.server.executor.entities.RequestInfo;

/**
 *
 * @author salaboy
 */
public class ExecutorServiceEntryPointImpl implements ExecutorServiceEntryPoint {
    
    private Executor executor;
    
    private ExecutorQueryService queryService;
    
    private ExecutorRequestAdminService adminService;

    public ExecutorServiceEntryPointImpl() {
    	
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public ExecutorQueryService getQueryService() {
        return queryService;
    }

    public void setQueryService(ExecutorQueryService queryService) {
        this.queryService = queryService;
    }

    public ExecutorRequestAdminService getAdminService() {
        return adminService;
    }

    public void setAdminService(ExecutorRequestAdminService adminService) {
        this.adminService = adminService;
    }
    
    

    public List<RequestInfo> getQueuedRequests() {
        return queryService.getQueuedRequests();
    }

    public List<RequestInfo> getExecutedRequests() {
        return queryService.getExecutedRequests();
    }

    public List<RequestInfo> getInErrorRequests() {
        return queryService.getInErrorRequests();
    }

    public List<RequestInfo> getCancelledRequests() {
        return queryService.getCancelledRequests();
    }

    public List<ErrorInfo> getAllErrors() {
        return queryService.getAllErrors();
    }

    public List<RequestInfo> getAllRequests() {
        return queryService.getAllRequests();
    }

    public int clearAllRequests() {
        return adminService.clearAllRequests();
    }

    public int clearAllErrors() {
        return adminService.clearAllErrors();
    }

    public synchronized Long scheduleRequest(CommandCodes commandName, CommandContext ctx) {
    	assert executor!=null;
    	assert commandName!=null;
    	assert ctx!=null;
    	
        return executor.scheduleRequest(commandName, ctx);
    }

    public void cancelRequest(Long requestId) {
        executor.cancelRequest(requestId);
    }

    public void init() {
    	executor = ExecutorFactory.getExecutor();
    	queryService = ExecutorFactory.getExecutorQueryService();
    	adminService = ExecutorFactory.getExecutorRequestAdminService();
    	       
    }

    public void destroy() {
        executor.destroy();
    }

    public int getInterval() {
        return executor.getInterval();
    }

    public void setInterval(int waitTime) {
        executor.setInterval(waitTime);
    }

    public int getRetries() {
        return executor.getRetries();
    }

    public void setRetries(int defaultNroOfRetries) {
        executor.setRetries(defaultNroOfRetries);
    }

    public int getThreadPoolSize() {
        return executor.getThreadPoolSize();
    }

    public void setThreadPoolSize(int nroOfThreads) {
        executor.setThreadPoolSize(nroOfThreads);
    }
    
    
    
}
