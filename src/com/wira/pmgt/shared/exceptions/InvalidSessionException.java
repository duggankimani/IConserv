package com.wira.pmgt.shared.exceptions;

import java.io.Serializable;

import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.actionvalidator.SessionValidator;

/**
 * Thrown by the {@link SessionValidator} whenever a valid session is found
 * 
 * @author duggan
 *
 */
public class InvalidSessionException extends ActionException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InvalidSessionException(){
	}
	
	public InvalidSessionException(String message) {
		super(message);
	}
}
