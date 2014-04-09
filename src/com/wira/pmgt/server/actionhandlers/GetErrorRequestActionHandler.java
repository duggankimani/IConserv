package com.wira.pmgt.server.actionhandlers;

import java.util.Date;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.model.ErrorLog;
import com.wira.pmgt.server.helper.error.ErrorLogDaoHelper;
import com.wira.pmgt.shared.requests.GetErrorRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetErrorRequestResult;

public class GetErrorRequestActionHandler extends
		BaseActionHandler<GetErrorRequest, GetErrorRequestResult> {

	@Inject
	public GetErrorRequestActionHandler() {
	}

	@Override
	public void execute(GetErrorRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		ErrorLog log = ErrorLogDaoHelper.retrieveError(action.getErrorId());
		
		GetErrorRequestResult result = (GetErrorRequestResult)actionResult;
		
		if(log!=null){
			result.setMessage(log.getMsg());
			result.setStack(log.getStackTrace());
			result.setErrorDate(log.getCreated());
			result.setAgent(log.getAgent());
			result.setRemoteAddress(log.getRemoteAddress());
		}else{
			result.setMessage("No Error Log for Error no: "+action.getErrorId());
			result.setStack("");
			result.setErrorDate(new Date());
		}
		result.setErrorCode(0);
	}
	
	@Override
	public Class<GetErrorRequest> getActionType() {
		return GetErrorRequest.class;
	}
}
