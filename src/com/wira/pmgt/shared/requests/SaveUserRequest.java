package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveUserResponse;

/**
 * 
 * @author duggan
 *
 */
public class SaveUserRequest extends BaseRequest<SaveUserResponse> {

	private HTUser user;
	private boolean isDelete=false;

	@SuppressWarnings("unused")
	private SaveUserRequest() {
	}

	public SaveUserRequest(HTUser user) {
		this.user = user;
	}

	public HTUser getUser() {
		return user;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new SaveUserResponse();
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
}
