package com.wira.pmgt.server.actionhandlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.shared.requests.LogoutAction;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.LogoutActionResult;

public class LogoutActionHandler extends
		BaseActionHandler<LogoutAction, LogoutActionResult> {

	private final Provider<HttpServletRequest> httpRequest;
	
	@Inject
	public LogoutActionHandler(final Provider<HttpServletRequest> httpRequest) {
		this.httpRequest=httpRequest;
	}

	@Override
	public void execute(LogoutAction action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		HttpSession session =httpRequest.get().getSession(false);
		
		if(session!=null){
			session.invalidate();
		}
		
	}

	@Override
	public void undo(LogoutAction action, LogoutActionResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<LogoutAction> getActionType() {
		return LogoutAction.class;
	}
}
