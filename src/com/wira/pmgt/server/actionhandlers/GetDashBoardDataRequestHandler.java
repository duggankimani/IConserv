package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.DashboardDaoImpl;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.model.DocStatus;
import com.wira.pmgt.shared.requests.GetDashBoardDataRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDashBoardDataResponse;

public class GetDashBoardDataRequestHandler extends
		BaseActionHandler<GetDashBoardDataRequest, GetDashBoardDataResponse> {

	@Inject
	public GetDashBoardDataRequestHandler() {
	}

	@Override
	public void execute(GetDashBoardDataRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		DashboardDaoImpl dao = DB.getDashboardDao();
		GetDashBoardDataResponse response = (GetDashBoardDataResponse)actionResult;
		response.setActiveCount(dao.getRequestCount(DocStatus.INPROGRESS));
		response.setRequestCount(dao.getRequestCount(false,DocStatus.DRAFTED));
		response.setFailureCount(dao.getRequestCount(DocStatus.FAILED));
		response.setDocumentCounts(dao.getDocumentCounts());
		response.setRequestAging(dao.getRequestAging());
	}

	@Override
	public Class<GetDashBoardDataRequest> getActionType() {
		return GetDashBoardDataRequest.class;
	}
}
