package com.wira.pmgt.server.rest.service;

import com.wira.pmgt.server.rest.model.Request;
import com.wira.pmgt.server.rest.model.Response;

/**
 * Execute Incoming client requests<br/>
 * i.e ERP => EBusiness Request - e.g APPROVALREQUEST 
 * 
 * @author duggan
 *
 */
public interface IncomingRequestService {

	public void executeClientRequest(Request request, Response response);
}
