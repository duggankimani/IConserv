package com.wira.pmgt.server.actionhandlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.requests.MoveItemRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.MoveItemResponse;

public class MoveItemRequestHandler extends BaseActionHandler<MoveItemRequest, MoveItemResponse> {

	@Override
	public void execute(MoveItemRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		if(action.getOutcomeId()!=null){
			ProgramDaoHelper.moveToOutcome(action.getItemToMoveId(), action.getOutcomeId());
		}else{
			ProgramDaoHelper.moveParent(action.getItemToMoveId(), action.getParentId());
		}
	}
	
	@Override
	public Class<MoveItemRequest> getActionType() {
		return MoveItemRequest.class;
	}
}
