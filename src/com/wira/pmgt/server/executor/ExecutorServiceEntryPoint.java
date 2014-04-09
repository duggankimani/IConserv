/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.pmgt.server.executor;

import java.util.List;

import com.wira.pmgt.server.executor.api.CommandCodes;
import com.wira.pmgt.server.executor.api.CommandContext;
import com.wira.pmgt.server.executor.entities.ErrorInfo;
import com.wira.pmgt.server.executor.entities.RequestInfo;

/**
 *
 * @author salaboy
 */
public interface ExecutorServiceEntryPoint {

    public List<RequestInfo> getQueuedRequests();

    public List<RequestInfo> getExecutedRequests();

    public List<RequestInfo> getInErrorRequests();

    public List<RequestInfo> getCancelledRequests();

    public List<ErrorInfo> getAllErrors();

    public List<RequestInfo> getAllRequests();

    public int clearAllRequests();

    public int clearAllErrors();

    public Long scheduleRequest(CommandCodes commandName, CommandContext ctx);

    public void cancelRequest(Long requestId);

    public void init();

    public void destroy();

    public int getInterval();

    public void setInterval(int waitTime);

    public int getRetries();

    public void setRetries(int defaultNroOfRetries);

    public int getThreadPoolSize();

    public void setThreadPoolSize(int nroOfThreads);
}
