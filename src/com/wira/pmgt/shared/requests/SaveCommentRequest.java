package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.Comment;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveCommentResponse;

public class SaveCommentRequest extends BaseRequest<SaveCommentResponse> {

	private Comment comment;

	@SuppressWarnings("unused")
	private SaveCommentRequest() {
		// For serialization only
	}

	public SaveCommentRequest(Comment comment) {
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveCommentResponse();
	}
}
