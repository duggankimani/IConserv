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
import com.wira.pmgt.client.ui.component.IssuesPanel;
import com.wira.pmgt.client.ui.component.autocomplete.AutoCompleteField;
import com.wira.pmgt.client.ui.component.grid.AggregationGrid;
import com.wira.pmgt.client.ui.component.grid.ColumnConfig;
import com.wira.pmgt.client.ui.component.grid.DataMapper;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.client.ui.images.ImageResources;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.Listable;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;

public class CreateProgramView extends PopupViewImpl implements
		CreateProgramPresenter.ICreateDocView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateProgramView> {
	}
	
	@UiField IssuesPanel issues;
	@UiField TextBox txtName;
	@UiField TextArea txtDescription;	
	@UiField DialogBox popupView;
	@UiField HasClickHandlers btnSave;
	@UiField HasClickHandlers btnCancel;
	@UiField DropDownList<PeriodDTO> lstPeriod;
	@UiField AutoCompleteField<UserGroup> autoComplete;
	@UiField AggregationGrid gridView;
	@UiField Image imgAdd;
	List<Listable> donors = new ArrayList<Listable>();
	ColumnConfig donorField = new ColumnConfig("donor", "Donor Name", DataType.SELECTBASIC);
	
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
		donorField.setDropDownItems(donors);
		configs.add(donorField);
		
		ColumnConfig config = new ColumnConfig("amount", "Amount", DataType.DOUBLE);
		config.setAggregationColumn(true);
		configs.add(config);
		
		gridView.setColumnConfigs(configs);
		gridView.setAutoNumber(true);
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
		program.setDescription(txtDescription.getValue());
		program.setId(null);
		program.setName(txtName.getValue());
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
		Double totalAmount=0.0;
		for(ProgramFundDTO programFund: funding){
			Double val = programFund.getAmount();
			if(val!=null){
				totalAmount+=val;
			}
		}
		program.setBudgetAmount(totalAmount);
		return program;
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if(isNullOrEmpty(txtName.getValue())){
			isValid = false;
			issues.addError("Program Name is mandatory");
		}
		
		if(isNullOrEmpty(txtDescription.getValue())){
			isValid = false;
			issues.addError("Program Description is mandatory");
		}
		
		if(lstPeriod.getValue()==null){
			isValid = false;
			issues.addError("Program period is mandatory");
		}
		
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
			if(model.get("donor")==null){
				return null;
			}
			
			fund.setFund(model.get("donor")==null? null: (FundDTO)model.get("donor"));
			fund.setAmount(model.get("amount")==null? null: (Double)model.get("amount"));
			fund.setId(model.getId());
			return fund;
		}
		
		@Override
		public List<DataModel> getDataModels(List<Object> funding) {
			List<DataModel> models = new ArrayList<DataModel>();
			for(Object obj: funding){
				ProgramFundDTO fund = (ProgramFundDTO)obj;
				DataModel model = new DataModel();
				model.set("donor", fund.getFund());
				model.setId(fund.getId());
				model.set("amount", fund.getAmount());
				models.add(model);
			}
			
			return models;
		}
	};

	@Override
	public void setFunds(List<FundDTO> funds) {
		if(funds!=null){
			for(FundDTO dto: funds){
				donors.add(dto);
			}
		}
		donorField.setDropDownItems(donors);
		gridView.refresh();
	} 

}
