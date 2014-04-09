package com.wira.pmgt.server.actionvalidator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.ActionException;
import com.gwtplatform.dispatch.shared.Result;
import com.wira.pmgt.server.ServerConstants;
import com.wira.pmgt.shared.exceptions.InvalidSessionException;

/**
 * Validate current Session
 * @author duggan
 *
 */
public class SessionValidator implements ActionValidator {

	@Inject Provider<HttpServletRequest> request;
	
	@Override
	public boolean isValid(Action<? extends Result> action)
			throws ActionException {
		
		if(!action.isSecured()){
			return true;
		}
		
		HttpSession session = request.get().getSession(false);
		
		if(session==null){
			throw new InvalidSessionException("No valid session found[1]");
		}
		
		Object sessionCookie = session.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
			
		if(sessionCookie==null){
			throw new InvalidSessionException("No valid session found[2]");
			
		}
		
		Cookie[] cookies = request.get().getCookies();
		for(Cookie c: cookies){
			if(c.getName().equals(ServerConstants.AUTHENTICATIONCOOKIE)){
				if(sessionCookie.equals(c.getValue())){
					//Authentication cookie provided by the front end== authentication cookie on the server session
					return true;
				}
			}
		}
		
		session.invalidate();
		throw new InvalidSessionException("No valid session found[3]");
	}

}
