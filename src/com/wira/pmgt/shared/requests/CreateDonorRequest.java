package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.CreateDonorResponse;
import com.wira.pmgt.shared.model.program.FundDTO;

public class CreateDonorRequest extends BaseRequest<CreateDonorResponse> {

	private FundDTO fund;

	@SuppressWarnings("unused")
	private CreateDonorRequest() {
		// For serialization only
	}

	public CreateDonorRequest(FundDTO fund) {
		this.fund = fund;
	}

	public FundDTO getFund() {
		return fund;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new CreateDonorResponse();
	}
}
