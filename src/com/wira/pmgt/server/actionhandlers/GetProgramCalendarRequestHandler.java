package com.wira.pmgt.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.requests.GetProgramCalendarRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramCalendarResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetProgramCalendarRequestHandler
		extends
		BaseActionHandler<GetProgramCalendarRequest, GetProgramCalendarResponse> {

	static Logger log = Logger.getLogger(GetProgramCalendarResponse.class);
	@Inject
	public GetProgramCalendarRequestHandler() {
	}

	@Override
	public void execute(GetProgramCalendarRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		List<ProgramSummary> summary = new ArrayList<>();
		if(SessionHelper.getCurrentUser()!=null){
			summary = ProgramDaoHelper.getProgramCalendar(SessionHelper.getCurrentUser().getUserId());
		}else{
			log.fatal("Current User cannot be found in session: Return empty list");
		}
		
		((GetProgramCalendarResponse)actionResult).setSummary(summary);
	}
	
	@Override
	public Class<GetProgramCalendarRequest> getActionType() {
		return GetProgramCalendarRequest.class;
	}
}
