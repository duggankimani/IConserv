package com.wira.pmgt.client.ui.newsfeed.components;

import static com.wira.pmgt.client.ui.util.DateUtils.getTimeDifferenceAsString;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.Comment;
import com.wira.pmgt.shared.model.HTUser;

public class CommentActivity extends Composite {

	private static CommentActivityUiBinder uiBinder = GWT
			.create(CommentActivityUiBinder.class);

	interface CommentActivityUiBinder extends UiBinder<Widget, CommentActivity> {
	}
	
	@UiField Image img;
	@UiField SpanElement spnAction;
	@UiField Anchor aDocument;
	//@UiField SpanElement spnSubject;
	@UiField SpanElement spnTime;
	@UiField Element spnUser;
	@UiField SpanElement commentText;
	//@UiField SpanElement spnDescription;
	Long commentid;

	public CommentActivity(Comment comment) {
		initWidget(uiBinder.createAndBindUi(this));
		bind(comment);
		img.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});
	}
	
	void bind(Comment comment) {
		
		setComment(comment.getId(),comment.getDocType(), comment.getSubject(),comment.getDescription(),
				comment.getComment(), comment.getCreatedBy(),
				comment.getCreated(), comment.getUpdatedBy(), comment.getUpdated(),
				comment.getDocumentId(), comment.getParentId()!=null);
	}


	public void setComment(Long commentId, 
			String docType, String subject,String description,
			String comment, HTUser createdBy,
			Date created, String updatedby, Date updated, long documentId, boolean isChild) {
		
		this.commentid=commentId;
		if(createdBy!=null){
			spnTime.setInnerText(getTimeDifferenceAsString(created));
			
			if(AppContext.isCurrentUser(createdBy.getUserId())){
				spnUser.setInnerText("You");
			}else{
				spnUser.setInnerText(createdBy.getSurname());
			}
			
		}
		
		aDocument.setText(docType+" "+subject +" "+description);
		//aDocument.setHref("#home;type=search;did="+documentId);
		aDocument.setHref("#home;did="+ documentId+";type=search");
		
		img.setUrl(AppContext.getUserImageUrl(createdBy));
		
		setComment(comment);
	}

	public void setComment(String comments) {
		commentText.setInnerText(comments);
	}

}
