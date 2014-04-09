package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.Comment;

public class SaveCommentResponse extends BaseResponse {

	private Comment comment;

	public SaveCommentResponse() {
	}

	public SaveCommentResponse(Comment comment) {
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
