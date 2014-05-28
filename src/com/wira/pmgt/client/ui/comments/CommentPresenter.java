package com.wira.pmgt.client.ui.comments;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.model.MODE;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.component.CommentBox;
import com.wira.pmgt.client.ui.events.ActivitiesLoadEvent;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.Comment;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.requests.GetActivitiesRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.requests.SaveCommentRequest;
import com.wira.pmgt.shared.responses.GetActivitiesResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class CommentPresenter extends PresenterWidget<CommentPresenter.ICommentView>{

	public interface ICommentView extends View {
		CommentBox getCommentBox();

		void setComment(Long commentId, String comment, HTUser createdBy,
				Date created, String updatedby, Date updated, long documentId, boolean isChild);

		void setMode(MODE mode);

	}

	Comment comment;

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public CommentPresenter(final EventBus eventBus, final ICommentView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getCommentBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String commentText = event.getValue();

				if (commentText == null || commentText.trim().isEmpty()) {
					if (comment.getId() == null) {
						getView().asWidget().removeFromParent();// not previously saved & not mandatory
						return;
					} else {
						// not mandatory/ clear previously saved
						saveComment(commentText);
					}
				} else if (commentText.equals(comment.getComment())
						&& comment.getId() != null) {
					getView().setMode(MODE.VIEW);
					return;// no change
				} else {
					// new values
					saveComment(commentText);
				}
			}
		});

	}

	private void bind(Comment comment) {
		
		getView().setComment(comment.getId(), comment.getComment(), comment.getCreatedBy(),
				comment.getCreated(), comment.getUpdatedBy(), comment.getUpdated(),
				comment.getDocumentId(), comment.getParentId()!=null);
	}

	protected void saveComment(final String commentTxt) {

		if(comment==null || commentTxt.trim().isEmpty())
			return;

		Comment commentToSave = new Comment();
		commentToSave.setComment(commentTxt);
		commentToSave.setDocumentId(comment.getDocumentId());
		commentToSave.setUserId(AppContext.getUserId());
		
		if(comment.getParentId()!=null){
			commentToSave.setParentId(comment.getParentId());
		}else{
			commentToSave.setParentId(comment.getId());
		}
		
		commentToSave.setComment(commentTxt);
		//commentToSave.setCreatedBy(AppContext.getUserId());

		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new SaveCommentRequest(commentToSave));
		action.addRequest(new GetActivitiesRequest(comment.getDocumentId()));
		
		requestHelper.execute(action,
				 new TaskServiceCallback<MultiRequestActionResult>(){
			@Override
			public void processResult(MultiRequestActionResult result) {
				result.get(0);
				
				GetActivitiesResponse response = (GetActivitiesResponse)result.get(1);
				
				fireEvent(new ActivitiesLoadEvent(response.getActivityMap()));
			}
		});
		
	}

	public void setComment(Comment comment) {
		this.comment = comment;
		bind(comment);
	}

}
