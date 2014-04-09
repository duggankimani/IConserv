package com.wira.pmgt.shared.responses;

import com.gwtplatform.dispatch.shared.Result;
import com.wira.pmgt.shared.model.HTUser;

import java.util.List;

public class GetUsersResponse extends BaseResponse {

	private List<HTUser> users;

	public GetUsersResponse() {
	}

	public GetUsersResponse(List<HTUser> users) {
		this.users = users;
	}

	public List<HTUser> getUsers() {
		return users;
	}

	public void setUsers(List<HTUser> users) {
		this.users = users;
	}
}
