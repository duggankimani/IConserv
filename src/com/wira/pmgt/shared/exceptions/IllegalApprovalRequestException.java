package com.wira.pmgt.shared.exceptions;

import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.shared.model.Document;

/**
 * 
 * @author duggan
 *
 */
public class IllegalApprovalRequestException extends ActionException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalApprovalRequestException(String message){
		super(message);
	}
	
	public IllegalApprovalRequestException(Document document){
		this("Cannot execute Approval Request - Document ["+document+"] is already attached to another process "+
				JBPMHelper.getProcessDetails(document.getProcessInstanceId()));
		
	}
}
