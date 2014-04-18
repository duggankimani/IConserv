package com.wira.pmgt.client.ui.save;

import static com.wira.pmgt.client.ui.save.CreateProgramPresenter.UPLOAD_SLOT;

import java.util.ArrayList;
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
import com.wira.pmgt.client.ui.component.grid.AggregationGrid;
import com.wira.pmgt.client.ui.component.grid.ColumnConfig;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.Priority;
import com.wira.pmgt.shared.model.UserGroup;

public class CreateProgramView extends PopupViewImpl implements
		CreateProgramPresenter.ICreateDocView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateProgramView> {
	}
	@UiField
	DialogBox popupView;
	
	@UiField
	HasClickHandlers btnSave;

	@UiField
	HasClickHandlers btnCancel;

	@UiField ListField<UserGroup> lstUsers;
	
	@UiField AggregationGrid gridView;
		
	@Inject
	public CreateProgramView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		
		
		int[] position=AppManager.calculatePosition(5, 50);
		popupView.setPopupPosition(position[1],position[0]);
		loadGrid();
	}

	private void loadGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig config = new ColumnConfig("donor", "Donor Name", DataType.STRING);
		configs.add(config);
		
		config = new ColumnConfig("amount", "Amount", DataType.DOUBLE);
		config.setAggregationColumn(true);
		configs.add(config);
		
		gridView.setColumnConfigs(configs);
				
		List<DataModel> models = new ArrayList<DataModel>();
		DataModel model = new DataModel();
		model.setId(null);
		model.set("donor", "USAID");
		model.set("amount", new Double(6000000));
		models.add(model);
		
		model = new DataModel();
		model.setId(null);
		model.set("donor", "EKM");
		model.set("amount", new Double(4000000));
		models.add(model);
		
		gridView.setAutoNumber(true);
		gridView.setData(models);
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

	public void setPriority(Priority priority) {
		
	}

	public Priority getPriority() {
		return null;
	}

	public void setDocumentType(DocumentType docType) {
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
