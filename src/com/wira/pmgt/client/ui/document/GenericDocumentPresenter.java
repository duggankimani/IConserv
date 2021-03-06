package com.wira.pmgt.client.ui.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.wira.pmgt.client.model.MODE;
import com.wira.pmgt.client.model.TaskType;
import com.wira.pmgt.client.model.UploadContext;
import com.wira.pmgt.client.model.UploadContext.UPLOADACTION;
import com.wira.pmgt.client.service.ServiceCallback;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.OnOptionSelected;
import com.wira.pmgt.client.ui.admin.formbuilder.HasProperties;
import com.wira.pmgt.client.ui.admin.formbuilder.component.SingleButton;
import com.wira.pmgt.client.ui.comments.CommentPresenter;
import com.wira.pmgt.client.ui.delegate.DelegateTaskView;
import com.wira.pmgt.client.ui.delegate.msg.DelegationMessageView;
import com.wira.pmgt.client.ui.document.activityview.ActivityDetailPresenter;
import com.wira.pmgt.client.ui.events.ActivitiesLoadEvent;
import com.wira.pmgt.client.ui.events.ActivitiesLoadEvent.ActivitiesLoadHandler;
import com.wira.pmgt.client.ui.events.AfterAttachmentReloadedEvent;
import com.wira.pmgt.client.ui.events.AfterDocumentLoadEvent;
import com.wira.pmgt.client.ui.events.AfterSaveEvent;
import com.wira.pmgt.client.ui.events.ButtonClickEvent;
import com.wira.pmgt.client.ui.events.ButtonClickEvent.ButtonClickHandler;
import com.wira.pmgt.client.ui.events.CompleteDocumentEvent;
import com.wira.pmgt.client.ui.events.DeleteLineEvent;
import com.wira.pmgt.client.ui.events.DeleteLineEvent.DeleteLineHandler;
import com.wira.pmgt.client.ui.events.ExecTaskEvent;
import com.wira.pmgt.client.ui.events.FileLoadEvent;
import com.wira.pmgt.client.ui.events.LoadActivitiesEvent;
import com.wira.pmgt.client.ui.events.LoadActivitiesEvent.LoadActivitiesHandler;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.events.ReloadAttachmentsEvent;
import com.wira.pmgt.client.ui.events.ReloadAttachmentsEvent.ReloadAttachmentsHandler;
import com.wira.pmgt.client.ui.events.ReloadDocumentEvent;
import com.wira.pmgt.client.ui.events.ReloadDocumentEvent.ReloadDocumentHandler;
import com.wira.pmgt.client.ui.events.ReloadEvent;
import com.wira.pmgt.client.ui.events.WorkflowProcessEvent;
import com.wira.pmgt.client.ui.notifications.note.NotePresenter;
import com.wira.pmgt.client.ui.popup.GenericPopupPresenter;
import com.wira.pmgt.client.ui.program.save.CreateProgramPresenter;
import com.wira.pmgt.client.ui.upload.UploadDocumentPresenter;
import com.wira.pmgt.client.ui.upload.attachment.AttachmentPresenter;
import com.wira.pmgt.client.ui.upload.custom.Uploader;
import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.Actions;
import com.wira.pmgt.shared.model.Activity;
import com.wira.pmgt.shared.model.Attachment;
import com.wira.pmgt.shared.model.BooleanValue;
import com.wira.pmgt.shared.model.Comment;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.DateValue;
import com.wira.pmgt.shared.model.Delegate;
import com.wira.pmgt.shared.model.Doc;
import com.wira.pmgt.shared.model.DocStatus;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.DocumentLine;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.GridValue;
import com.wira.pmgt.shared.model.HTStatus;
import com.wira.pmgt.shared.model.HTSummary;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.HTask;
import com.wira.pmgt.shared.model.NodeDetail;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.model.NotificationType;
import com.wira.pmgt.shared.model.StringValue;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.Field;
import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.model.form.FormModel;
import com.wira.pmgt.shared.model.form.Property;
import com.wira.pmgt.shared.requests.ApprovalRequest;
import com.wira.pmgt.shared.requests.CreateDocumentRequest;
import com.wira.pmgt.shared.requests.DeleteDocumentRequest;
import com.wira.pmgt.shared.requests.DeleteLineRequest;
import com.wira.pmgt.shared.requests.GetActivitiesRequest;
import com.wira.pmgt.shared.requests.GetAttachmentsRequest;
import com.wira.pmgt.shared.requests.GetCommentsRequest;
import com.wira.pmgt.shared.requests.GetDocumentRequest;
import com.wira.pmgt.shared.requests.GetFormModelRequest;
import com.wira.pmgt.shared.requests.GetProcessStatusRequest;
import com.wira.pmgt.shared.requests.GetUsersRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.requests.SaveCommentRequest;
import com.wira.pmgt.shared.responses.ApprovalRequestResult;
import com.wira.pmgt.shared.responses.CreateDocumentResult;
import com.wira.pmgt.shared.responses.DeleteDocumentResponse;
import com.wira.pmgt.shared.responses.DeleteLineResponse;
import com.wira.pmgt.shared.responses.GetActivitiesResponse;
import com.wira.pmgt.shared.responses.GetAttachmentsResponse;
import com.wira.pmgt.shared.responses.GetCommentsResponse;
import com.wira.pmgt.shared.responses.GetDocumentResult;
import com.wira.pmgt.shared.responses.GetFormModelResponse;
import com.wira.pmgt.shared.responses.GetProcessStatusRequestResult;
import com.wira.pmgt.shared.responses.GetUsersResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class GenericDocumentPresenter extends
		PresenterWidget<GenericDocumentPresenter.MyView> 
		implements ReloadDocumentHandler, ActivitiesLoadHandler,
		ReloadAttachmentsHandler,DeleteLineHandler,ButtonClickHandler,LoadActivitiesHandler{

	public interface MyView extends View {
		void setValues(HTUser createdBy, Date created, String type, String subject,
				Date docDate, String value, String partner, String description, 
				Integer priority,DocStatus status, Long id, String taskDisplayName);
		
		void showForward(boolean show);
		void setValidTaskActions(List<Actions> actions);
		void show(boolean IsShowapprovalLink, boolean IsShowRejectLink);
		void showEdit(boolean displayed);
		void setStates(List<NodeDetail> states);
		HasClickHandlers getSimulationBtn();
		HasClickHandlers getSaveButton();
		HasClickHandlers getDeleteButton();
		HasClickHandlers getForwardForApproval();		
		HasClickHandlers getClaimLink();
		HasClickHandlers getStartLink();
		HasClickHandlers getSuspendLink();
		HasClickHandlers getResumeLink();
		HasClickHandlers getCompleteLink();
		HasClickHandlers getDelegateLink();
		HasClickHandlers getRevokeLink();
		HasClickHandlers getStopLink();
		HasClickHandlers getApproveLink();
		HasClickHandlers getRejectLink();
		HasValueChangeHandlers<String> getCommentBox();
		Uploader getUploader();
		void setComment(String string);
		SpanElement getSpnAttachmentNo();
		SpanElement getSpnActivityNo();
		DivElement getDivAttachment();
		void setForm(Form form);
		boolean isValid();
		Map<String,Value> getValues(); //Task Data

		void showDefaultFields(boolean b);

		void setDelegate(Delegate delegate);

		HasClickHandlers getUploadLink2();

		void setDeadline(Date endDateDue);
		
		void overrideDefaultCompleteProcess();

		void overrideDefaultStartProcess();

		void hideTopHeader(boolean show);

		void showCommentsPanel(boolean b);

		void showAttachmentPanel(boolean show);
	}
	
	Long taskId;
	
	Long documentId;
	
	Doc doc;
	
	Form form;
	
	private Integer activities=0;
	
	@Inject DispatchAsync requestHelper;
	
	@Inject PlaceManager placeManager;
	
	private IndirectProvider<CreateProgramPresenter> createDocProvider;
	private IndirectProvider<CommentPresenter> commentPresenterFactory;
	private IndirectProvider<AttachmentPresenter> attachmentPresenterFactory;
	private IndirectProvider<NotePresenter> notePresenterFactory;
	private IndirectProvider<UploadDocumentPresenter> uploaderFactory;
	private IndirectProvider<ActivityDetailPresenter> activityDetailFactory;

	private boolean isDetailedView;
	
	
	//@Inject static MainPagePresenter mainPagePresenter;
	@Inject static GenericPopupPresenter popupPresenter;
	
	public static final Object ACTIVITY_SLOT = new Object();
	public static final Object ATTACHMENTS_SLOT = new Object();
	public static final Object BODY_SLOT = new Object();
	
	@Inject
	public GenericDocumentPresenter(final EventBus eventBus, final MyView view,
			Provider<CreateProgramPresenter> docProvider,
			Provider<CommentPresenter> commentProvider,
			Provider<AttachmentPresenter> attachmentProvider,
			Provider<NotePresenter> noteProvider,
			Provider<ActivityDetailPresenter> activityProvider,
			Provider<UploadDocumentPresenter> uploaderProvider) {
		super(eventBus, view);		
		createDocProvider = new StandardProvider<CreateProgramPresenter>(docProvider);
		commentPresenterFactory = new StandardProvider<CommentPresenter>(commentProvider);
		attachmentPresenterFactory = new StandardProvider<AttachmentPresenter>(attachmentProvider);
		notePresenterFactory = new StandardProvider<NotePresenter>(noteProvider);
		uploaderFactory = new StandardProvider<UploadDocumentPresenter>(uploaderProvider);
		activityDetailFactory  = new StandardProvider<ActivityDetailPresenter>(activityProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		addRegisteredHandler(ReloadDocumentEvent.TYPE, this);
		addRegisteredHandler(ActivitiesLoadEvent.TYPE, this);
		addRegisteredHandler(ReloadAttachmentsEvent.TYPE, this);
		addRegisteredHandler(DeleteLineEvent.TYPE, this);
		addRegisteredHandler(LoadActivitiesEvent.TYPE, this);
		addRegisteredHandler(ButtonClickEvent.TYPE, this);
		
		getView().getUploadLink2().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showUpload();
			  }
		});
		
		getView().getForwardForApproval().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent());
				requestHelper.execute(new ApprovalRequest(AppContext.getUserId(), (Document)doc), new TaskServiceCallback<ApprovalRequestResult>(){
					@Override
					public void processResult(ApprovalRequestResult result) {
						GenericDocumentPresenter.this.getView().asWidget().removeFromParent();
						fireEvent(new ProcessingCompletedEvent());
						//clear selected document
						fireEvent(new AfterSaveEvent());
						fireEvent(new WorkflowProcessEvent(doc.getSubject(), "You have forwarded for Approval",doc));
					}
				});
				
			}
		});
		
		getView().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(doc instanceof Document)
				if(((Document)doc).getStatus()==DocStatus.DRAFTED){
					//showEditForm(MODE.EDIT);
					save((Document)doc);
				}
			}
		});
		
		getView().getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(doc instanceof Document)
					if(((Document)doc).getStatus()==DocStatus.DRAFTED){
						//showEditForm(MODE.EDIT);
						delete((Document)doc);
					}
			}
		});
		
		getView().getCommentBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				save(event.getValue());
			}
		});
		
		//testing code
		getView().getSimulationBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				requestHelper.execute(new ApprovalRequest(AppContext.getUserId(), (Document)doc), new TaskServiceCallback<ApprovalRequestResult>(){
					@Override
					public void processResult(ApprovalRequestResult result) {						
						PlaceRequest request = new PlaceRequest("home").
								with("type", TaskType.DRAFT.getURL());
						
						placeManager.revealPlace(request);
						
					}
				});
			}
		});

		getView().getClaimLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.CLAIM);
				fireEvent(new ExecTaskEvent(taskId, Actions.CLAIM));
			}
		});
		
		getView().getStartLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.START);
				assert taskId!=null;
				fireEvent(new ExecTaskEvent(taskId, Actions.START));
			}
		});
		
		getView().getSuspendLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.SUSPEND);
				fireEvent(new ExecTaskEvent(taskId, Actions.SUSPEND));
			}
		});
		
		getView().getResumeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.RESUME);
				fireEvent(new ExecTaskEvent(taskId, Actions.RESUME));
			}
		});
		
		getView().getCompleteLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					
					Map<String, Value> values = getView().getValues();
					if(values==null){
						values = new HashMap<String, Value>();
					}
					fireEvent(new CompleteDocumentEvent(taskId, values));
				}
				fireEvent(new ExecTaskEvent(taskId, Actions.COMPLETE));
			}
		});
		
		getView().getDelegateLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//submitRequest(Actions.DELEGATE);
				//fireEvent(new ExecTaskEvent(taskId, Actions.DELEGATE));
				requestHelper.execute(new GetUsersRequest(), new TaskServiceCallback<GetUsersResponse>() {
					@Override
					public void processResult(GetUsersResponse result) {
						showDelegatePopup(result.getUsers());
					}
				});
				
				//mainPagePresenter.addToPopupSlot(popupPresenter, false);
			}
		});
		
		getView().getRevokeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//submitRequest(Actions.REVOKE);
				fireEvent(new ExecTaskEvent(taskId, Actions.REVOKE));
			}
		});
		
		getView().getStopLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ExecTaskEvent(taskId, Actions.STOP));				
			}
		});
		
		getView().getApproveLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final ConfirmAction confirm = new ConfirmAction("Do you want to approve this request?",
						"Approval comments");
				
				AppManager.showPopUp("Approval Comments", confirm, new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Approve")){
							//create comment
							Map<String, Value> values= new HashMap<String, Value>();
							values.put("isApproved", new BooleanValue(true));
							complete(values, true);	
							if(confirm.getComment()!=null && !confirm.getComment().isEmpty()){
								save(confirm.getComment());
							}
						}						
					}
				}, "Approve", "Cancel");		
				
			}
		});
		
		getView().getRejectLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {							
				final ConfirmAction confirm = new ConfirmAction("Do you want to reject this request?",
						"Rejection reason or comments");
				
				AppManager.showPopUp("Rejection Comments", confirm, new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Reject")){
							//create comment
							Map<String, Value> values = new HashMap<String, Value>();
							values.put("isApproved", new BooleanValue(false));
							complete(values, false);		
							
							if(confirm.getComment()!=null && !confirm.getComment().isEmpty()){
								save(confirm.getComment());
							}
						}						
					}
				}, "Reject", "Cancel");		
				
			}
		});
		
		
	}
	
	public void complete(Map<String, Value> withValues, boolean validateForm){
		if(validateForm){
			if(getView().isValid()){
				completeIt(withValues);
			}
		}else{
			completeIt(withValues);
		}
	}


	private void completeIt(Map<String, Value> withValues) {
		Map<String, Value> values = getView().getValues();
		if(values==null){
				values = new HashMap<String, Value>();
		}
		
		if(withValues!=null)
			values.putAll(withValues);
		
		assert !values.isEmpty();
		values.remove(null);
		
		fireEvent(new CompleteDocumentEvent(taskId, values));
	}

	protected void forwardForApproval() {
		if(getView().isValid()){
			fireEvent(new ProcessingEvent());
			requestHelper.execute(new ApprovalRequest(AppContext.getUserId(), (Document)doc), new TaskServiceCallback<ApprovalRequestResult>(){
				@Override
				public void processResult(ApprovalRequestResult result) {
					GenericDocumentPresenter.this.getView().asWidget().removeFromParent();
					fireEvent(new ProcessingCompletedEvent());
					//clear selected document
					fireEvent(new AfterSaveEvent());
					fireEvent(new WorkflowProcessEvent(doc.getSubject(), "You have forwarded for Approval",doc));
				}
			});
		}
	}



	protected void showUpload() {
		uploaderFactory.get(new ServiceCallback<UploadDocumentPresenter>() {
			@Override
			public void processResult(UploadDocumentPresenter result) {
				
				UploadContext context = new UploadContext();
				context.setAction(UPLOADACTION.ATTACHDOCUMENT);
				context.setContext("documentId", documentId+"");
				context.setContext("userid", AppContext.getUserId());
				result.setContext(context);
				addToPopupSlot(result,false);
			}
		});
		
	}

	protected void delete(Document document) {
		AppManager.showPopUp("Confirm Delete", 
				new InlineLabel("Do you want to delete document '"+document.getSubject()+"'"),
				new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Yes")){
							requestHelper.execute(new DeleteDocumentRequest(documentId),
									new TaskServiceCallback<DeleteDocumentResponse>() {
										@Override
										public void processResult(
												DeleteDocumentResponse aResponse) {
											if(aResponse.isDelete()){
												fireEvent(new ReloadEvent());
											}
											
										}
									});
						}
					}
				}, "Yes","Cancel");
	}

	private void showDelegatePopup(final List<HTUser> users) {
		final DelegateTaskView view = new DelegateTaskView(users);
		showDelegatePopup(view);
		
	}
	
	private void showDelegatePopup(final DelegateTaskView view) {
		AppManager.showPopUp("Delegate Task", view,
				new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Ok")){
							final HTUser user = view.getSelectedUser();
							if(user!=null && user.getUserId()!=null){
								
								DelegationMessageView msgView = new DelegationMessageView(user, doc.getSubject());
								
								AppManager.showPopUp("Delegation Message",
										msgView,
										new OnOptionSelected() {
											
											@Override
											public void onSelect(String name) {
												
												if(name.equals("Back")){
													showDelegatePopup(view);
												}else{
													ExecTaskEvent event = new ExecTaskEvent(taskId, Actions.DELEGATE);
													
													Map<String, Value> values = new HashMap<String, Value>();
													
													StringValue userValue = new StringValue(null, "targetUserId",user.getUserId());
													values.put(userValue.getKey(), userValue);
													event.setValues(values);
													fireEvent(event);
												}
												
											}
										}, "Back", "Done");
								
							}
						}
					}
				}, "Ok", "Cancel");
	}

	protected void save(Document document) {
		document.setValues(getView().getValues());
		document.getDetails().clear();
		for(Value val: document.getValues().values()){
			if(val instanceof GridValue){
				GridValue gridVal = (GridValue)val;
				Collection<DocumentLine> lines = gridVal.getValue();
				for(DocumentLine line: lines){
					line.setName(gridVal.getKey());
					document.addDetail(line);
				}				
			}
		}
		
//		List<DocumentLine> lnes=document.getDetails().get("lines");
//		for(DocumentLine l: lnes){
//			System.err.println(l);
//		}
		
		if (getView().isValid()) {
			fireEvent(new ProcessingEvent());
			requestHelper.execute(new CreateDocumentRequest(document),
					new TaskServiceCallback<CreateDocumentResult>() {
						@Override
						public void processResult(CreateDocumentResult result) {
							fireEvent(new ProcessingCompletedEvent());
							Document saved = result.getDocument();
							assert saved.getId() != null;
							bindForm(form, saved);
						}
					});
		}
		
	}

	protected void save(String commenttxt) {
		if(commenttxt==null || commenttxt.trim().isEmpty())
			return;
		
		getView().setComment("");
		Comment comment = new Comment();
		comment.setComment(commenttxt);
		comment.setDocumentId(documentId);
		comment.setParentId(null);
		comment.setUserId(AppContext.getUserId());
		//comment.setCreatedBy(AppContext.getUserId());
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new SaveCommentRequest(comment));
		action.addRequest(new GetActivitiesRequest(documentId));
		
		requestHelper.execute(action,
				 new TaskServiceCallback<MultiRequestActionResult>(){
			@Override
			public void processResult(MultiRequestActionResult result) {
				result.get(0);
				bindActivities((GetActivitiesResponse)result.get(1));
			}
		});
		
	}

	protected void showEditForm(final MODE mode) {
		
		createDocProvider.get(new ServiceCallback<CreateProgramPresenter>() {
			@Override
			public void processResult(CreateProgramPresenter result) {
				if(mode.equals(MODE.EDIT)){
					//result.setDocumentId(documentId);
				}
				
				addToPopupSlot(result, true);				
			}
		});
	}

	private void clear() {
		getView().showEdit(false);
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();	
		loadData();
	}
	
	public void loadData() {
		if(isDetailedView){
			return;
		}
		getView().hideTopHeader(false);
		MultiRequestAction requests = new MultiRequestAction();
		System.err.println("docID= "+documentId+"; taskId="+taskId);
		requests.addRequest(new GetDocumentRequest(documentId, taskId));
		requests.addRequest(new GetCommentsRequest(documentId));
		requests.addRequest(new GetActivitiesRequest(documentId));
		requests.addRequest(new GetFormModelRequest(FormModel.FORMMODEL,taskId,documentId));
		requests.addRequest(new GetAttachmentsRequest(documentId));
		
		fireEvent(new ProcessingEvent());
		if(documentId != null){
			requestHelper.execute(requests, 
					new TaskServiceCallback<MultiRequestActionResult>() {
				
				public void processResult(MultiRequestActionResult results) {					
					
					int i=0;
					GetDocumentResult result = (GetDocumentResult)results.get(i);
					bindDocumentResult(result);
					
					GetCommentsResponse commentsResult = (GetCommentsResponse)results.get(++i);
					bindCommentsResult(commentsResult);
					
					GetActivitiesResponse getActivities = (GetActivitiesResponse)results.get(++i);
					bindActivities(getActivities);
					
					GetFormModelResponse response = (GetFormModelResponse)results.get(++i);					
					
					if(!response.getFormModel().isEmpty()){
						bindForm((Form)response.getFormModel().get(0), result.getDoc());
						
					}else{
						getView().showDefaultFields(true);
					}
					
					GetAttachmentsResponse attachmentsresponse = (GetAttachmentsResponse)results.get(++i);
					bindAttachments(attachmentsresponse);
					
					
					fireEvent(new ProcessingCompletedEvent());
					
				}	
			});
		}
		
	}
	
	public void loadProgramData() {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetCommentsRequest(documentId));
		requests.addRequest(new GetActivitiesRequest(documentId));
		
		fireEvent(new ProcessingEvent());
		
		System.err.println("Doc Id >>"+documentId);
		if(documentId != null){
			requestHelper.execute(requests, 
					new TaskServiceCallback<MultiRequestActionResult>() {
				
				public void processResult(MultiRequestActionResult results) {					
					GetCommentsResponse commentsResult = (GetCommentsResponse)results.get(0);
					bindCommentsResult(commentsResult);
					
					GetActivitiesResponse getActivities = (GetActivitiesResponse)results.get(1);
					bindActivities(getActivities);
					
					fireEvent(new ProcessingCompletedEvent());
				}	
			});
		}
		
	}

	protected void bindForm(Form form, Doc doc) {
		this.doc = doc;
		this.form = form;
		if(form.getFields()==null){
			getView().showDefaultFields(true);
			return;
		}
			
		Map<String, Value> values = doc.getValues();
		//System.err.println("Details >>>>> "+doc.getDetails().size());
		for(Field field: form.getFields()){
			String name = field.getName();
			
			
			field.setDocId(((doc instanceof Document)? doc.getId() : ((HTSummary)doc).getDocumentRef())+""); //Add DocId to all field
			
			if(name==null || name.isEmpty()){
				continue;
			}
						
			if(field.getType()==DataType.GRID){
				List<DocumentLine> lines=doc.getDetails().get(field.getName());
				if(lines!=null){
					GridValue value = new GridValue();
					value.setKey(field.getName());
					value.setCollectionValue(lines);
					//System.err.println(">>"+lines.size());
					field.setValue(value);
				}
				continue;
			}else if(field.getType()==DataType.BUTTON){
				String submitType = field.getPropertyValue(SingleButton.SUBMITTYPE);
				if(submitType!=null){
					if(submitType.equals("CompleteProcess")){
						//Override default complete
						getView().overrideDefaultCompleteProcess();
					}else if(submitType.equals("StartProcess")){
						//Override default start
						getView().overrideDefaultStartProcess();
					}
					
				}
				
				if(doc instanceof Document){
					DocStatus status = ((Document)doc).getStatus();
					if(status==DocStatus.DRAFTED){
						Property prop = new Property(HasProperties.READONLY, "Read only", DataType.BOOLEAN);
						prop.setValue(new BooleanValue(null, HasProperties.READONLY, false));
						field.getProperties().add(prop);
					}
				}
			}
			
			Value value = values.get(name);
			field.setValue(value);
			
			if(value==null){
				if(name.equals("subject")){
					value = new StringValue(doc.getSubject());
				}
				
				if(name.equals("description")){
					value = new StringValue(doc.getDescription());
				}
				
				if(name.equals("docDate")){
					value = new DateValue(doc.getCreated());
				}
				field.setValue(value);
			}
				
		}		getView().setForm(form);
	}

	protected void bindActivities(GetActivitiesResponse response) {
		Map<Activity, List<Activity>> activitiesMap = response.getActivityMap();
		bindActivities(activitiesMap);		
	}
	
	public void bindActivities(Map<Activity, List<Activity>> activitiesMap){
		setInSlot(ACTIVITY_SLOT, null);
		
		Set<Activity> keyset = activitiesMap.keySet();
		List<Activity> activities= new ArrayList<Activity>();
		activities.addAll(keyset);
		Collections.sort(activities);
		Collections.reverse(activities);
		
		for(Activity activity: activities){
			bind(activity,false);	
			List<Activity> children = activitiesMap.get(activity);	
			if(children!=null){
				for(Activity child: children){
					bind(child, true);
				}
				
			}
		}		
		this.activities +=activitiesMap.size();
	}

	private void bind(final Activity child, boolean isChild) {
				
		if(child instanceof Comment){
			commentPresenterFactory.get(new ServiceCallback<CommentPresenter>() {
				@Override
				public void processResult(CommentPresenter result) {
					result.setComment((Comment)child);
					
					addToSlot(ACTIVITY_SLOT,result);
				}
			});
			
		}else if(child instanceof Notification){
			if(((Notification)child).getNotificationType()==NotificationType.FILE_UPLOADED){
				return;
			}
		
			notePresenterFactory.get(new ServiceCallback<NotePresenter>() {				
				@Override
				public void processResult(NotePresenter result) {
					result.setNotification((Notification)child, false);
					addToSlot(ACTIVITY_SLOT, result);
				}
			});
		}
		
	}

	protected void bindAttachments(GetAttachmentsResponse attachmentsresponse) {
		
		List<Attachment> attachments = attachmentsresponse.getAttachments();
		
		if(attachments.size()>0){
			getView().getDivAttachment().removeClassName("hidden");
			getView().getSpnAttachmentNo().setInnerText("Attachments (" + attachments.size() +")");
			fireEvent(new AfterAttachmentReloadedEvent(documentId));
		}
		
		setInSlot(ATTACHMENTS_SLOT, null);//clear
		for(final Attachment attachment: attachments){
			if(attachment.getFieldName()!=null){
				fireEvent(new FileLoadEvent(attachment));
				continue;
			}
			attachmentPresenterFactory.get(new ServiceCallback<AttachmentPresenter>() {
				@Override
				public void processResult(AttachmentPresenter result) {
					result.setAttachment(attachment);
					addToSlot(ATTACHMENTS_SLOT, result);
				}
			});
		}
		
	}

	protected void bindCommentsResult(GetCommentsResponse commentsResult) {
		setInSlot(ACTIVITY_SLOT, null);
		List<Comment> comments = commentsResult.getComments();
		
		this.activities += comments.size();
		
		getView().getSpnActivityNo().setInnerText("Activity("+this.activities+")");
		for(final Comment comment: comments){
			commentPresenterFactory.get(new ServiceCallback<CommentPresenter>() {
				@Override
				public void processResult(CommentPresenter result) {
					result.setComment(comment);
					addToSlot(ACTIVITY_SLOT,result);
				}
			});
		}
	}

	protected void bindDocumentResult(GetDocumentResult result) {

		this.doc = result.getDoc();
		Map<String, Value> vals= doc.getValues();
		
		long docId=0l;
		
		String taskDisplayName="";
		if(doc instanceof Document){
			docId = (Long)doc.getId();
			
		}else{
			HTSummary task = ((HTSummary)doc); 
			docId = task.getDocumentRef();
			this.taskId = task.getId();
			
			if(task.getName()!=null)
				taskDisplayName = task.getName();
			
			HTask humantask = (HTask)task;
			Delegate delegate = humantask.getDelegate();
			if(delegate!=null){
				getView().setDelegate(delegate);
			}
			
			if(!(task.getStatus()==HTStatus.COMPLETED)){
				if(task.getEndDateDue()!=null){
					getView().setDeadline(task.getEndDateDue());
				}else{
					//default 1 day allowance				
					getView().setDeadline(DateUtils.addDays(task.getCreated(), 1));
					
				}
			}
		}
		
		this.documentId = docId;
		
		Date created = doc.getCreated();
		String subject = doc.getSubject();
		
		Date docDate = doc.getDocumentDate();					
		String description = doc.getDescription();
		Integer priority = doc.getPriority();	
		
		String partner = null;
		String value= null;
		DocumentType docType = null;
		DocStatus status = null;
		
		String type=null;
		
		if(doc instanceof Document){
			Document d = (Document)doc;
			value = d.getValue();
			partner = d.getPartner();
			docType= d.getType();
			type = docType.getDisplayName();
			
			status = d.getStatus();
		}else{
			HTask task = (HTask)doc;
			type = task.getName();	
			status = task.getDocStatus();
		}
		
		if(value==null){
			Value val = vals.get("value");
			if(val!=null){
				value = val.getValue()==null? null: val.getValue().toString();
			}
		}
		
		if(partner==null){
			Value val = vals.get("partner");
			if(val!=null)
				value = ((StringValue)val).getValue();
		}
		
		
		getView().setValues(doc.getOwner(),created,
				type, subject, docDate,  value, partner, description, priority,status, documentId,
				taskDisplayName);
		
		if(status==DocStatus.DRAFTED){
			getView().showEdit(true);
		}else{
			getView().showEdit(false);
			clear();
		}
		
		//get document actions - if any
		AfterDocumentLoadEvent e = new AfterDocumentLoadEvent(documentId, taskId);
		fireEvent(e);		
		if(e.getValidActions()!=null){
			getView().setValidTaskActions(e.getValidActions());
		}	
		
		Long processInstanceId = doc.getProcessInstanceId();
		
		if(processInstanceId!=null){
			//System.err.println("Loading activities for ProcessInstanceId = "+processInstanceId);
			requestHelper.execute(new GetProcessStatusRequest(processInstanceId),
					new TaskServiceCallback<GetProcessStatusRequestResult>() {
				@Override
				public void processResult(
						GetProcessStatusRequestResult result) {
					List<NodeDetail> details = result.getNodes();
					setProcessState(details);
				}
			});
		}
	}

	public void setProcessState(List<NodeDetail> states){
		getView().setStates(states);
	}

	public void setDocId(Long documentId, Long taskId) {
		this.documentId=documentId;
		this.taskId = taskId;
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		this.unbind();
	}

	@Override
	public void onReloadDocument(ReloadDocumentEvent event) {
		if(event.getDocumentId()==this.documentId){
			loadData();
		}
	}

	@Override
	public void onActivitiesLoad(ActivitiesLoadEvent event) {
		bindActivities(event.getActivitiesMap());
	}

	@Override
	public void onReloadAttachments(ReloadAttachmentsEvent event) {
		reloadAttachments();
	}

	private void reloadAttachments() {
		requestHelper.execute(new GetAttachmentsRequest(documentId),
				new TaskServiceCallback<GetAttachmentsResponse>() {
			@Override
			public void processResult(GetAttachmentsResponse result) {
				bindAttachments(result);
			}
		});
	}
	
	/**
	 * Runtime - Delete Row
	 * -Enable save/ edit mode
	 */
	@Override
	public void onDeleteLine(DeleteLineEvent event) {
		DocumentLine line = event.getLine();
		if(line.getId()==null){
			return;
		}
		AppContext.fireEvent(new ProcessingEvent("Deleting ..."));
		requestHelper.execute(new DeleteLineRequest(line), 
				new TaskServiceCallback<DeleteLineResponse>() {
			@Override
			public void processResult(DeleteLineResponse result) {
				AppContext.fireEvent(new ProcessingCompletedEvent());
			}
		});
	}
	

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		String requestType = event.getRequestType();
		
		if(requestType==null && event.getCustomHandlerClass()==null){
			return;
		}
		
		if(requestType!=null){
			if(requestType.equals("StartProcess")){
				forwardForApproval();
			}else if(requestType.equals("CompleteProcess")){
				complete(event.getValues(),event.isValidateForm());
			}
		}
		else{
			executeGenericCode(event.getValues(), event.getCustomHandlerClass());
		}
	}

	private void executeGenericCode(Map<String, Value> values,
			String customHandlerClass) {
//		requestHelper.execute(new GenericRequest(values, customHandlerClass), 
//				new TaskServiceCallback<GenericResponse>() {
//			@Override
//			public void processResult(GenericResponse aResponse) {	
//			}
//		});
	}

	public void showDetailedView(final Long activityId) {
		this.isDetailedView=true;
		activityDetailFactory.get(new ServiceCallback<ActivityDetailPresenter>() {
			@Override
			public void processResult(ActivityDetailPresenter dResponse) {
				getView().hideTopHeader(true);
				dResponse.loadData(activityId);
				setInSlot(BODY_SLOT, dResponse);
			}
		});
	}

	@Override
	public void onLoadActivities(LoadActivitiesEvent event) {
		getView().showCommentsPanel(true);
		if (event.getDocumentId() != null) {
			setDocId(event.getDocumentId(), null);
			loadProgramData();
			getView().showCommentsPanel(true);
		}else{
			//hide comments
			getView().showCommentsPanel(false);
		}
		
	}
	
}
