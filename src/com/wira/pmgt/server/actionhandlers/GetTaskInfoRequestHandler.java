package com.wira.pmgt.server.actionhandlers;

import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.dao.model.DocumentModel;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.requests.GetTaskInfoRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetTaskInfoResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTaskInfoRequestHandler extends
		BaseActionHandler<GetTaskInfoRequest, GetTaskInfoResponse> {

	@Inject
	public GetTaskInfoRequestHandler() {
	}

	@Override
	public void execute(GetTaskInfoRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		long activityId = action.getActivityId();
		ProgramDetail detail = DB.getProgramDaoImpl().getProgramDetail(activityId);
		if(detail.getProcessInstanceId()==null){
			return;
		}
		
		DocumentModel model = DB.getDocumentDao().getDocumentByProcessInstanceId(detail.getProcessInstanceId());
		if(model!=null){
			GetTaskInfoResponse response = (GetTaskInfoResponse)actionResult;
			response.setProcessInstanceId(detail.getProcessInstanceId());
			response.setDocumentId( model.getId());
		}
	}
	
	@Override
	public Class<GetTaskInfoRequest> getActionType() {
		return GetTaskInfoRequest.class;
	}
}
