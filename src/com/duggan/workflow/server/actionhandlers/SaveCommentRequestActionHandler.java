package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.CommentDaoHelper;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.requests.SaveCommentRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveCommentResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveCommentRequestActionHandler extends
		BaseActionHandler<SaveCommentRequest, SaveCommentResponse> {

	@Inject
	public SaveCommentRequestActionHandler() {
	}

	@Override
	public void execute(SaveCommentRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Comment comment = action.getComment();
		
		comment = CommentDaoHelper.saveComment(comment);
		
		SaveCommentResponse response = (SaveCommentResponse)actionResult;
		response.setComment(comment);
	}

	@Override
	public Class<SaveCommentRequest> getActionType() {
		return SaveCommentRequest.class;
	}
}