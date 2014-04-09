package com.wira.pmgt.shared.responses;

public class DeleteDocumentResponse extends BaseResponse {

	private boolean isDelete;

	public DeleteDocumentResponse() {
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

}
