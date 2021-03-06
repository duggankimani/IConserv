package com.wira.pmgt.client.ui.comments;

import static com.wira.pmgt.client.ui.util.DateUtils.getTimeDifferenceAsString;

import java.util.Date;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.model.MODE;
import com.wira.pmgt.client.ui.component.CommentBox;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.HTUser;

public class CommentView extends ViewImpl implements CommentPresenter.ICommentView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CommentView> {
	}
	
	@UiField HTMLPanel root;
	
	@UiField Image img;
	
	@UiField SpanElement spnCreatedBy;
	@UiField SpanElement spnCreated;
	@UiField SpanElement spnMessage;
	
	@UiField Anchor aReply;
	@UiField CommentBox txtCommentBox;
	@UiField DivElement divSave;
	
	@UiField SpanElement spnTime;

	static String HIDDEN="hidden";
	static String DISPLAYED="block";

	static String CANCELIMAGE = "images/mm/close.gif";
	static String EDITIMAGE="images/mm/pen_icon12.png";
	
	Long commentid=null;
	MODE mode;
	

	@Inject
	public CommentView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		 txtCommentBox.getElement().setAttribute("placeholder","write your comment...");
		 txtCommentBox.setHeight("15px");
		 
		aReply.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if(mode==MODE.VIEW){
					setMode(MODE.EDIT);		
				}else{
					setMode(MODE.VIEW);	
				}
			}
		});
		
		img.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});
		
		img.getElement().getStyle().setWidth(40.0, Unit.PX);
		img.getElement().getStyle().setHeight(70.0, Unit.PX);

	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setComment(Long commentId, String comment, HTUser createdBy,
			Date created, String updatedby, Date updated, long documentId, boolean isChild) {
		
		this.commentid=commentId;
		if(createdBy!=null){
			spnCreated.setInnerText(getTimeDifferenceAsString(created));
			
			if(AppContext.isCurrentUser(createdBy.getUserId())){
				spnCreatedBy.setInnerText("You");
			}else{
				spnCreatedBy.setInnerText(createdBy.getSurname());
			}
			
			setImage(createdBy);
			
		}
		
		if(isChild){
			root.addStyleName("well");
			aReply.addStyleName("hidden");
		}
						
		setComment(comment);
	}

	public void setComment(String comments) {
		spnMessage.setInnerText(comments);
		if(commentid!=null){
			setMode(MODE.VIEW);
		}else{
			setMode(MODE.EDIT);
		}
	}
	
	/**
	 * 1 - View
	 * 2 - Edit
	 * @param mode
	 */
	public void setMode(MODE mode){
		this.mode=mode;
		
		if(mode==MODE.EDIT){			
			((Widget)aReply).setTitle("Edit");
			divSave.removeClassName(HIDDEN);
			txtCommentBox.removeStyleName(HIDDEN);
			aReply.setText("Cancel");
			//((Widget)aReply).setTitle("Cancel");
			divSave.addClassName("reply");
			
			txtCommentBox.getSpnArrow().addClassName("reply");
			//aReply.addStyleName(HIDDEN);
		}else{
			//aReply.removeStyleName(HIDDEN);
			//((Widget)aReply).setTitle("Reply to this Comment");
			aReply.setText("Reply");
			divSave.addClassName(HIDDEN);
			txtCommentBox.addStyleName(HIDDEN);
		}
	}
	
	public CommentBox getCommentBox(){
		return txtCommentBox;
	}
	
	private void setImage(HTUser user) {
		img.setUrl(AppContext.getUserImageUrl(user));
	}
	
}
