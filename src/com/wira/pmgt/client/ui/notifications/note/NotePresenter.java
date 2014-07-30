package com.wira.pmgt.client.ui.notifications.note;

import static com.wira.pmgt.client.ui.util.DateUtils.getTimeDifferenceAsString;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.ApproverAction;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.model.NotificationType;
import com.wira.pmgt.shared.requests.UpdateNotificationRequest;
import com.wira.pmgt.shared.responses.UpdateNotificationRequestResult;

public class NotePresenter extends
		PresenterWidget<NotePresenter.MyView> {

	public interface MyView extends View {
		
		HasClickHandlers getDocumentBtn();

		void setValues(String subject, DocumentType documentType,
				NotificationType notificationType, HTUser owner,
				HTUser targetUserId, String time, boolean isRead, 
				HTUser createdBy,ApproverAction approverAction, Long processInstanceId,
				Long documentId,boolean isNotification);
	}

	private Notification note;

	@Inject PlaceManager placeManager;
	
	@Inject DispatchAsync dispatcher;
		
	boolean isNotification = false;
	
	@Inject
	public NotePresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getDocumentBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//save read		
				if(!note.IsRead()){
					UpdateNotificationRequest request = new UpdateNotificationRequest(note.getId(), true);
					dispatcher.execute(request, new TaskServiceCallback<UpdateNotificationRequestResult>() {
						public void processResult(UpdateNotificationRequestResult result) {
							Notification notification = result.getNotification();
							String time=getTimeDifferenceAsString(notification.getCreated());
							getView().setValues(notification.getSubject(),
									notification.getDocumentType(),
									notification.getNotificationType(),
									notification.getOwner(),
									notification.getTargetUserId(),time,
									notification.IsRead()==null? false: notification.IsRead(),
									notification.getCreatedBy(),
									notification.getApproverAction(),
									notification.getProcessInstanceId(),
									notification.getDocumentId(),
											isNotification);
//							
//							PlaceRequest request = new PlaceRequest("home")
//							.with("type", TaskType.SEARCH.getURL())
//							.with("pid", notification.getProcessInstanceId()+"");
//							
//							placeManager.revealPlace(request);
						};
					});
					
				}
			}
		});
	}
	
	public void setNotification(Notification notification, boolean isNotification){
		this.isNotification=isNotification;
		this.note = notification;
		String time=getTimeDifferenceAsString(notification.getCreated());
		getView().setValues(notification.getSubject(),
				notification.getDocumentType(),
				notification.getNotificationType(),
				notification.getOwner(),
				notification.getTargetUserId(),time,
				notification.IsRead()==null? false: notification.IsRead(),
						notification.getCreatedBy(),
						notification.getApproverAction(),notification.getProcessInstanceId(),
						notification.getDocumentId(),
						isNotification);
	}
	
}
