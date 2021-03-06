package com.wira.pmgt.server.rest.service;

import com.wira.pmgt.server.rest.model.Request;
import com.wira.pmgt.server.rest.model.Response;

/**
 * EBUSINESS TO ERP SYSTEM Service 
 * 
 * @author duggan
 *
 */
public interface OutgoingRequestService {

	/**
	 * Mechanism to call external resource and retrieve
	 * responses
	 * 
	 * e.g GetBudgetRequest would call and ERP with document details
	 * and the ERP would respond with budget details 
	 * 
	 * @param request
	 * @return
	 */
	public Response executeCall(Request request);

	public Response executeCall(Request request, String serviceURI);
	
}
