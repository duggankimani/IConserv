package com.wira.pmgt.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
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

	/**
	 * Programs can be loaded by Ids or program codes;
	 * Program Codes are important when loading programs based on periods
	 * <p>
	 * Wildlife program 2013 and Wildlife Program in 2014 are two different 
	 * programs associated by code
	 * 
	 */
	@Override
	public void execute(GetProgramsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetProgramsResponse response = (GetProgramsResponse)actionResult;
		List<IsProgramDetail> activities = new ArrayList<>();
		
		if(action.getCode()==null && action.getPeriodId()==null){
			loadById(action,activities);
		}else{
			loadByCode(action, activities);
		}
		
		response.setPrograms(activities);
		
	}

	private void loadByCode(GetProgramsRequest action,
			List<IsProgramDetail> activities) {
		if(action.getCode()!=null){
			IsProgramDetail activity = ProgramDaoHelper.getProgramByCode(action.getCode(), action.getPeriodId(),
					action.isLoadChildren());
			
			if(activity!=null){
				activities.add(activity);
			}
		}else if(action.getType()!=null){
			activities.addAll(ProgramDaoHelper.getProgramByTypeAndPeriod(action.getType(), action.getPeriodId(),
					action.isLoadChildren()));
		}else{
			activities.addAll(ProgramDaoHelper.getProgramsByPeriod(action.getPeriodId(),
					action.isLoadChildren()));
		}
	}

	private void loadById(GetProgramsRequest action,
			List<IsProgramDetail> activities) {

		if(action.getId()!=null){
			IsProgramDetail activity = ProgramDaoHelper.getProgramById(action.getId(), action.isLoadChildren());
			if(activity!=null){
				activities.add(activity);
			}
		}else if(action.getType()!=null){
			activities.addAll(ProgramDaoHelper.getProgramsByType(action.getType(), action.isLoadChildren()));
		}else{
			activities.addAll(ProgramDaoHelper.getPrograms(action.isLoadChildren()));
		}
	}

	@Override
	public Class<GetProgramsRequest> getActionType() {
		return GetProgramsRequest.class;
	}
}
