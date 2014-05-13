package com.wira.pmgt.client.ui.newsfeed.components;

import static com.wira.pmgt.client.ui.util.DateUtils.getTimeDifferenceAsString;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.model.UploadContext;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.ApproverAction;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.model.NotificationType;

public class TaskActivity extends Composite {

	private static TaskActivityUiBinder uiBinder = GWT
			.create(TaskActivityUiBinder.class);

	interface TaskActivityUiBinder extends UiBinder<Widget, TaskActivity> {
	}
	
	@UiField Image img;
	@UiField SpanElement spnAction;
	@UiField SpanElement spnTask;
	//@UiField SpanElement spnSubject;
	@UiField Anchor aFile;
	@UiField SpanElement spnTo;
	@UiField Anchor aDocument;
	
	@UiField SpanElement spnTime;
	@UiField SpanElement spnUser;
	
	String url;

	public TaskActivity(Notification notification) {
		initWidget(uiBinder.createAndBindUi(this));
		
		img.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});

		String text = "";
		
		String subject=notification.getSubject();
		DocumentType documentType=notification.getDocumentType();
		
		NotificationType notificationType=notification.getNotificationType();
		HTUser ownerObj=notification.getOwner();
		HTUser targetUser=notification.getTargetUserId();
		HTUser createdBy=notification.getCreatedBy();
		ApproverAction approverAction=notification.getApproverAction();
		Long processInstanceId=notification.getProcessInstanceId();
		String time=getTimeDifferenceAsString(notification.getCreated());
		
		String prefix = documentType.getDisplayName();
		subject = prefix + " " + subject;

		String action = "";
		
		if(approverAction!=null){
			action = approverAction.getAction();
		}
		
		String owner = ownerObj.getSurname();
		
		if(AppContext.isCurrentUser(ownerObj.getUserId())){
			owner = "You";
		}
		
		String approver = createdBy.getSurname();
		if(AppContext.isCurrentUser(createdBy.getUserId())){
			approver="You";
		}
		
		String target = null;
		
		if(targetUser!=null){
			target =targetUser.getSurname();
			if(AppContext.isCurrentUser(targetUser.getUserId())){
				target="You";
			}
		}
		
		switch (notificationType) {
		case APPROVALREQUEST_APPROVERNOTE:
			spnUser.setInnerText(owner);
			text = "forwarded ";

			setImage(ownerObj);
			
			break;

		case TASKASSIGNMENT_ASSIGNEENOTE:
			spnUser.setInnerText(owner);
			text = "assigned "+target+" ";

			setImage(ownerObj);
			
			break;
		case APPROVALREQUEST_OWNERNOTE:
			spnUser.setInnerText(owner);
			text = "forwarded ";

			setImage(ownerObj);
			
			break;
		case TASKASSIGNMENT_ASSIGNORNOTE:
			spnUser.setInnerText(owner);
			text = "assigned ";
			setImage(ownerObj);
					
			break;
		case TASKCOMPLETED_APPROVERNOTE:
		
			spnUser.setInnerText(owner);
			text =action +" ";

			setImage(notification.getOwner());
			
			break;
		case TASKCOMPLETED_OWNERNOTE:
			spnUser.setInnerText(approver);
			text =action+" ";

			setImage(notification.getCreatedBy());
			
			break;
		case TASKDELEGATED:
			
			spnUser.setInnerText(approver);
			text = "delegated to "+target+" "; 
			setImage(notification.getCreatedBy());
			break;
		case FILE_UPLOADED:
			spnUser.setInnerText(owner);
			setImage(ownerObj);
			spnTask.setInnerText("File");
			spnTask.addClassName("label-purple");
			spnTask.removeClassName("label-blue");
			text = "uploaded file ";
			aFile.removeStyleName("hide");
			spnTo.removeClassName("hide");
			aFile.setText(notification.getFileName());
			UploadContext context = new UploadContext("getreport");
			context.setContext("attachmentId", notification.getFileId()+"");
			context.setContext("ACTION", "GETATTACHMENT");
			url = context.toUrl();
			
			aFile.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
					if(moduleUrl.endsWith("/")){
						moduleUrl = moduleUrl.substring(0, moduleUrl.length()-1);
					}
					url = url.replace("/", "");
					moduleUrl =moduleUrl+"/"+url;
					Window.open(moduleUrl, aFile.getText(), "");
				}
			});
			
			break;
		default:
			break;
		}
		
		aDocument.setText(subject);
		
		if(notification.getDocumentId()!=null){
			aDocument.setHref("#home;type=search;did="+notification.getDocumentId());
		}else if(processInstanceId!=null){
			aDocument.setHref("#home;type=search;pid="+processInstanceId);
		}

		spnAction.setInnerText(text);
		spnTime.setInnerText(time);
	}

	private void setImage(HTUser user) {
		img.setUrl(AppContext.getUserImageUrl(user));
	}

}
