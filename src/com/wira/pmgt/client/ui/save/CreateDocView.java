package com.wira.pmgt.client.ui.save;

import static com.wira.pmgt.client.ui.save.CreateDocPresenter.UPLOAD_SLOT;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.admin.component.ListField;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.Priority;
import com.wira.pmgt.shared.model.UserGroup;

public class CreateDocView extends PopupViewImpl implements
		CreateDocPresenter.ICreateDocView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateDocView> {
	}
	@UiField
	DialogBox popupView;
	
	@UiField
	HasClickHandlers btnSave;

	@UiField
	HasClickHandlers btnCancel;

	@UiField ListField<UserGroup> lstUsers;
		
	@Inject
	public CreateDocView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		
		
		int[] position=AppManager.calculatePosition(5, 50);
		popupView.setPopupPosition(position[1],position[0]);
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void addToSlot(Object slot, Widget content) {

		if (slot == UPLOAD_SLOT) {
			if (content != null) {
			}
		} else {
			super.addToSlot(slot, content);
		}

	}

	@Override
	public HasClickHandlers getSave() {
		return btnSave;
	}

	@Override
	public HasClickHandlers getCancel() {

		return btnCancel;
	}

	

	@Override
	public boolean isValid() {
		// txtDescription.getValue();

		boolean isValid = true;

		return isValid;
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	@Override
	public void setValues(DocumentType docType, String subject, Date docDate,
			String partner, String value, String description, Priority priority, Long documentId) {


	}

	private void setPriority(Priority priority) {
		
	}

	public Priority getPriority() {
		return null;
	}

	private void setDocumentType(DocumentType docType) {
	}

	@Override
	public void setDocTypes(List<DocumentType> types) {
	}

	@Override
	public Document getDocument() {
		return null;
	}
	
	@Override
	public void setUsers(List<UserGroup> users) {
		lstUsers.addItems(users);
	}

}
