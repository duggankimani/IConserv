package com.wira.pmgt.server.actionhandlers;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.CommentDaoHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.model.Comment;
import com.wira.pmgt.shared.requests.SaveCommentRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.SaveCommentResponse;

public class SaveCommentRequestActionHandler extends
		BaseActionHandler<SaveCommentRequest, SaveCommentResponse> {

	@Inject
	public SaveCommentRequestActionHandler() {
	}

	@Override
	public void execute(SaveCommentRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Comment comment = action.getComment();
		comment.setCreatedBy(SessionHelper.getCurrentUser());
		comment = CommentDaoHelper.saveComment(comment);
		
		SaveCommentResponse response = (SaveCommentResponse)actionResult;
		response.setComment(comment);
	}

	@Override
	public Class<SaveCommentRequest> getActionType() {
		return SaveCommentRequest.class;
	}
}
