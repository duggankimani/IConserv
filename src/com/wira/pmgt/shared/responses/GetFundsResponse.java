package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.program.FundDTO;

public class GetFundsResponse extends BaseResponse {

	private List<FundDTO> funds;

	public GetFundsResponse() {
	}

	public List<FundDTO> getFunds() {
		return funds;
	}

	public void setFunds(List<FundDTO> funds) {
		this.funds = funds;
	}
}
