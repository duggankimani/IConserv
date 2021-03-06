package com.wira.pmgt.client.ui.save.form;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.events.AfterSaveEvent;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.events.WorkflowProcessEvent;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.DocStatus;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.requests.ApprovalRequest;
import com.wira.pmgt.shared.requests.CreateDocumentRequest;
import com.wira.pmgt.shared.requests.GetFormModelRequest;
import com.wira.pmgt.shared.responses.ApprovalRequestResult;
import com.wira.pmgt.shared.responses.CreateDocumentResult;
import com.wira.pmgt.shared.responses.GetFormModelResponse;

public class GenericFormPresenter extends
		PresenterWidget<GenericFormPresenter.ICreateDocView> {

	public interface ICreateDocView extends PopupView {
		HasClickHandlers getSave();
		HasClickHandlers getCancel();
		HasClickHandlers getForward();
		Document getDocument();
		boolean isValid();
		void setForm(Form form);
		HasClickHandlers getForwardForApproval();
		void showButtons(boolean b);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> UPLOAD_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	DispatchAsync requestHelper;

	private Long documentId;
	
	private DocumentType documentType;

	@Inject
	PlaceManager placeManager;

	@Inject
	public GenericFormPresenter(final EventBus eventBus, final ICreateDocView view) {
		super(eventBus, view);
	}
	
	@Override
	protected void onBind() {
		super.onBind();

		getView().getCancel().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getView().hide();
			}
		});
		
		getView().getSave().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Document document = getView().getDocument();
				document.setStatus(DocStatus.DRAFTED);
				document.setId(documentId);
				document.setType(documentType);

				// document.setDescription(null);
				if (getView().isValid()) {
					getView().showButtons(false);
					
					requestHelper.execute(new CreateDocumentRequest(document),
							new TaskServiceCallback<CreateDocumentResult>() {
								@Override
								public void processResult(
										CreateDocumentResult result) {
									getView().showButtons(true);
									Document saved = result.getDocument();
									assert saved.getId() != null;
									AppContext.fireEvent(new AfterSaveEvent());
									getView().hide();
								}
								
							});
				}
			}
		});
		
		getView().getForwardForApproval().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				final Document document = getView().getDocument();
				document.setStatus(DocStatus.DRAFTED);
				document.setId(documentId);
				document.setType(documentType);
				
				if (getView().isValid()) {
					getView().showButtons(false);
					fireEvent(new ProcessingEvent());
					requestHelper.execute(
							new ApprovalRequest(AppContext.getUserId(),document),
							new TaskServiceCallback<ApprovalRequestResult>() {
								@Override
								public void processResult(
										ApprovalRequestResult result) {
									getView().showButtons(true);
									fireEvent(new ProcessingCompletedEvent());
									getView().hide();
									fireEvent(new AfterSaveEvent());
									
									Document doc = result.getDocument();
											
									fireEvent(new WorkflowProcessEvent(doc.getSubject(),"You have forwarded for approval - "
											 ,doc));
									getView().hide();
								}
							});
				}

			}

		});
		
		
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		if(documentType.getFormId()!=null)
			loadForm(documentType.getFormId());
	}
	
	protected void loadForm(Long formid) {
		GetFormModelRequest request = new GetFormModelRequest(Form.FORMMODEL, formid, true);
		requestHelper.execute(request, new TaskServiceCallback<GetFormModelResponse>() {
			@Override
			public void processResult(GetFormModelResponse result) {
				assert result.getFormModel().size()==1;
				Form form = (Form)result.getFormModel().get(0);
				getView().setForm(form);
				//getView().center();
			}
		});
	}

	public void setDocumentType(DocumentType type) {
		this.documentType = type;
	}

}
