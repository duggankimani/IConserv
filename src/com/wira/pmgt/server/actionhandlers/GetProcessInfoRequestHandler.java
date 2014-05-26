package com.wira.pmgt.server.actionhandlers;

import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.dao.model.DocumentModel;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.requests.GetProcessInfoRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProcessInfoResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetProcessInfoRequestHandler extends
		BaseActionHandler<GetProcessInfoRequest, GetProcessInfoResponse> {

	@Inject
	public GetProcessInfoRequestHandler() {
	}

	@Override
	public void execute(GetProcessInfoRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		long activityId = action.getActivityId();
		ProgramDetail detail = DB.getProgramDaoImpl().getProgramDetail(activityId);
		if(detail.getProcessInstanceId()==null){
			return;
		}
		
		DocumentModel model = DB.getDocumentDao().getDocumentByProcessInstanceId(detail.getProcessInstanceId());
		if(model!=null){
			GetProcessInfoResponse response = (GetProcessInfoResponse)actionResult;
			response.setProcessInstanceId(detail.getProcessInstanceId());
			response.setDocumentId( model.getId());
		}
	}
	
	@Override
	public Class<GetProcessInfoRequest> getActionType() {
		return GetProcessInfoRequest.class;
	}
}
