package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

/**
 * This class will execute multiple requests/commands/actions
 * whilst ensuring that they all share the same session &
 * transaction 
 * 
 * @author duggan
 *
 */
public class MultiRequestActionHandler extends
		BaseActionHandler<MultiRequestAction, MultiRequestActionResult> {

	@Inject
	public MultiRequestActionHandler() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(MultiRequestAction action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<BaseRequest<BaseResponse>> requests = action.getRequest();
		MultiRequestActionResult result = (MultiRequestActionResult)actionResult;
		
		//execution will follow the order of insertion
		for(BaseRequest request : requests){
			try{
				result.addResponse((BaseResponse)execContext.execute(request));
			}catch(Exception e){
				throw new RuntimeException(e);
			}
			
		}
	}

	@Override
	public Class<MultiRequestAction> getActionType() {
		return MultiRequestAction.class;
	}
}
