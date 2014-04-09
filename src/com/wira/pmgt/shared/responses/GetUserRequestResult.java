package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.HTUser;

public class GetUserRequestResult extends BaseResponse {

	private HTUser user;

	public GetUserRequestResult() {
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}
}
