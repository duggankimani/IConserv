package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.program.FundDTO;

public class CreateDonorResponse extends BaseResponse {

	private FundDTO fund;

	public CreateDonorResponse() {
	}

	public CreateDonorResponse(FundDTO fund) {
		this.fund = fund;
	}

	public FundDTO getFund() {
		return fund;
	}

	public void setFund(FundDTO fund) {
		this.fund = fund;
	}
	
}
