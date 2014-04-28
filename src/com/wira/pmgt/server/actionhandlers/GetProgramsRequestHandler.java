package com.wira.pmgt.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetProgramsRequestHandler extends
		BaseActionHandler<GetProgramsRequest, GetProgramsResponse> {

	@Inject
	public GetProgramsRequestHandler() {
	}

	@Override
	public void execute(GetProgramsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetProgramsResponse response = (GetProgramsResponse)actionResult;
		List<IsProgramActivity> activities = new ArrayList<>();
		if(action.getId()!=null){
			IsProgramActivity activity = ProgramDaoHelper.getProgramById(action.getId(), action.isLoadChildren(),action.isLoadObjectives());
			if(activity!=null){
				activities.add(activity);
			}
		}else if(action.getType()!=null){
			activities = ProgramDaoHelper.getPrograms(action.getType(), action.isLoadChildren(),action.isLoadObjectives());
		}else{
			activities = ProgramDaoHelper.getPrograms(action.isLoadChildren(),action.isLoadObjectives());
		}
		
		response.setPrograms(activities);
		
	}

	@Override
	public Class<GetProgramsRequest> getActionType() {
		return GetProgramsRequest.class;
	}
}
