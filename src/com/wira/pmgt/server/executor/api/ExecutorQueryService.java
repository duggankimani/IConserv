/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.pmgt.server.executor.api;

import java.util.List;

import com.wira.pmgt.server.executor.entities.ErrorInfo;
import com.wira.pmgt.server.executor.entities.RequestInfo;

/**
 *
 * @author salaboy
 */
public interface ExecutorQueryService {
    List<RequestInfo> getQueuedRequests();
    List<RequestInfo> getExecutedRequests();
    List<RequestInfo> getInErrorRequests();
    List<RequestInfo> getCancelledRequests();
    List<ErrorInfo> getAllErrors(); 
    List<RequestInfo> getAllRequests(); 
}
