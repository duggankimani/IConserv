package com.wira.pmgt.client.ui.program.save;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.component.DropDownList;
import com.wira.pmgt.client.ui.component.autocomplete.AutoCompleteField;
import com.wira.pmgt.client.ui.component.grid.AggregationGrid;
import com.wira.pmgt.client.ui.component.grid.ColumnConfig;
import com.wira.pmgt.client.ui.component.grid.DataMapper;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.client.ui.images.ImageResources;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;

public class CreateProgramView extends PopupViewImpl implements
		CreateProgramPresenter.ICreateDocView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateProgramView> {
	}
	
	@UiField TextBox txtSubject;
	@UiField TextArea txtDescription;	
	@UiField DialogBox popupView;
	@UiField HasClickHandlers btnSave;
	@UiField HasClickHandlers btnCancel;
	@UiField DropDownList<PeriodDTO> lstPeriod;
	@UiField AutoCompleteField<UserGroup> autoComplete;
	@UiField AggregationGrid gridView;
	@UiField Image imgAdd;
	
	@Inject
	public CreateProgramView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		imgAdd.setResource(ImageResources.IMAGES.add());
		
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
	public HasClickHandlers getSave() {
		return btnSave;
	}

	@Override
	public HasClickHandlers getCancel() {
		return btnCancel;
	}
	
	public ProgramDTO getProgram(){
		ProgramDTO program = new ProgramDTO();
//		program.setActualAmount(actualAmount);
//		program.setBudgetAmount(budgetAmount);
		program.setDescription(txtDescription.getValue());
		//program.setFunding(funding);
		program.setId(null);
		program.setName(txtSubject.getName());
		program.setParentId(null);
		PeriodDTO period = lstPeriod.getValue();
		program.setPeriod(lstPeriod.getValue());
		if(period!=null){
			program.setEndDate(period.getEndDate());
			program.setStartDate(period.getStartDate());
		}
		program.setType(ProgramDetailType.PROGRAM);
		//program.setTargetsAndOutcomes(targetsAndOutcomes);
		List<ProgramFundDTO> funding = gridView.getData(programFundMapper);
		program.setFunding(funding);
		
		return program;
		
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
	public HasClickHandlers getAddPeriodLink() {
		return imgAdd;
	}

	@Override
	public void setPeriods(List<PeriodDTO> periods) {
		lstPeriod.setItems(periods);
	}

	@Override
	public void setGroups(List<UserGroup> groups) {
		autoComplete.setValues(groups);
	}
	
	DataMapper programFundMapper = new DataMapper() {
		
		@Override
		public ProgramFundDTO getData(DataModel model) {
			
			ProgramFundDTO fund = new ProgramFundDTO();
			model.get("donor");
			model.get("amount");
			model.getId();
			
			return fund;
		}
	}; 

}
