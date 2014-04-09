package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.CommentDaoHelper;
import com.wira.pmgt.shared.model.Comment;
import com.wira.pmgt.shared.requests.GetCommentsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetCommentsResponse;

public class GetCommentsRequestActionHandler extends
		BaseActionHandler<GetCommentsRequest, GetCommentsResponse> {

	@Inject
	public GetCommentsRequestActionHandler() {
	}

	@Override
	public void execute(GetCommentsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<Comment> comments = CommentDaoHelper.getAllCommentsByDocumentId(action.getDocumentId());
		
		((GetCommentsResponse)actionResult).setComments(comments);
	}

	@Override
	public Class<GetCommentsRequest> getActionType() {
		return GetCommentsRequest.class;
	}
}
