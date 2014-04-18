package com.wira.pmgt.client.ui.save;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.events.AfterSaveEvent;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.Priority;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.requests.GetDocumentRequest;
import com.wira.pmgt.shared.requests.GetDocumentTypesRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetDocumentResult;
import com.wira.pmgt.shared.responses.GetDocumentTypesResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class CreateProgramPresenter extends
		PresenterWidget<CreateProgramPresenter.ICreateDocView> {

	public interface ICreateDocView extends PopupView {

		HasClickHandlers getSave();

		HasClickHandlers getCancel();

		Document getDocument();

		boolean isValid();

		void setDocTypes(List<DocumentType> types);

		void setValues(DocumentType docType, String subject, Date docDate,
				String partner, String value, String description,
				Priority priority, Long documentId);

		void setUsers(List<UserGroup> lstUsers);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> UPLOAD_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	DispatchAsync requestHelper;

	private Long Id;

	@Inject
	PlaceManager placeManager;

	@Inject
	public CreateProgramPresenter(final EventBus eventBus, final ICreateDocView view) {
		super(eventBus, view);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetDocumentTypesRequest());

		if (Id != null)
			requests.addRequest(new GetDocumentRequest(Id, null));

		requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {

					public void processResult(MultiRequestActionResult responses) {

						GetDocumentTypesResponse response = (GetDocumentTypesResponse) responses
								.get(0);
						getView().setDocTypes(response.getDocumentTypes());

						if (Id != null)
							showDocument((GetDocumentResult) responses.get(1));
					}
				});
		
		loadList();
	}

	protected void showDocument(GetDocumentResult result) {
		Document document = (Document) result.getDoc();

		DocumentType docType = document.getType();
		String subject = document.getSubject();
		Date docDate = document.getDocumentDate();
		String partner = document.getPartner();
		String value = document.getValue();
		String description = document.getDescription();
		Integer priority = document.getPriority();

		getView().setValues(docType, subject, docDate, partner, value,
				description, Priority.get(priority), document.getId());
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
			/*	Document document = getView().getDocument();
				document.setStatus(DocStatus.DRAFTED);
				document.setId(Id);

				// document.setDescription(null);
				if (getView().isValid()) {
					requestHelper.execute(new CreateDocumentRequest(document),
							new TaskServiceCallback<CreateDocumentResult>() {
								@Override
								public void processResult(CreateDocumentResult result) {

									Document saved = result.getDocument();
									assert saved.getId() != null;
									fireEvent(new AfterSaveEvent());
								}
							});
				}
			*/
			// Temporary method to fire method
			//	
				getView().hide();
				History.newItem("home;page=activities;type=listing");
			}
		});
	}

	private void loadList() {
		List<UserGroup> lstUsers = new ArrayList<UserGroup>();

		UserGroup g = new UserGroup();
		g.setFullName("Peter Njoroge");
		g.setName("Peter");

		lstUsers.add(g);

		getView().setUsers(lstUsers);

	}
}
