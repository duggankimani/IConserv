package com.wira.pmgt.server.actionhandlers;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.requests.CreateDonorRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateDonorResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class CreateDonorRequestHandler extends
		BaseActionHandler<CreateDonorRequest, CreateDonorResponse> {

	@Inject
	public CreateDonorRequestHandler() {
	}

	@Override
	public void execute(CreateDonorRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		FundDTO fundDTO = ProgramDaoHelper.save(action.getFund());
		
		CreateDonorResponse response = (CreateDonorResponse)actionResult;
		response.setFund(fundDTO);
	}
	
	@Override
	public Class<CreateDonorRequest> getActionType() {
		return CreateDonorRequest.class;
	}
}
