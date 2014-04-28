package com.wira.pmgt.client.ui.detailedActivity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.DateRangeWidget;
import com.wira.pmgt.client.ui.component.IssuesPanel;
import com.wira.pmgt.client.ui.component.autocomplete.AutoCompleteField;
import com.wira.pmgt.client.ui.component.grid.AggregationGrid;
import com.wira.pmgt.client.ui.component.grid.ColumnConfig;
import com.wira.pmgt.client.ui.component.grid.DataMapper;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.Listable;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.TargetAndOutcomeDTO;

public class CreateActivityView extends ViewImpl implements
		CreateActivityPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateActivityView> {
	}

	IsProgramActivity Outcome; //The outcome under which this activity is created

	@UiField IssuesPanel issues;
	@UiField TextArea txtActivity;
	@UiField AggregationGrid gridView;
	@UiField BulletListPanel crumbContainer;
	@UiField InlineLabel spnPeriod;
	@UiField AggregationGrid gridTargets;
	@UiField AutoCompleteField<HTUser> allocatedToUsers;
	@UiField AutoCompleteField<UserGroup> allocatedToGroups;
	@UiField AutoCompleteField<IsProgramActivity> objectivesAutoComplete;
	
	@UiField DateRangeWidget dtRange;

	List<Listable> donors = new ArrayList<Listable>();
	ColumnConfig donorField = new ColumnConfig("donor", "Donor Name", DataType.SELECTBASIC);
	
	@Inject
	public CreateActivityView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		createGrid();
		txtActivity.getElement().setAttribute("rows", "3");
		createTargetsAndIndicatorsGrid();

	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	private void createTargetsAndIndicatorsGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
//		ColumnConfig config = new ColumnConfig("condition", "Check", DataType.SELECTBASIC);
//		configs.add(config);
		
		ColumnConfig config = new ColumnConfig("target", "Target", DataType.DOUBLE);
		configs.add(config);
		
		config = new ColumnConfig("indicator", "Indicator", DataType.STRING);
		configs.add(config);
		
//		config = new ColumnConfig("actual", "Actual", DataType.INTEGER);
//		configs.add(config);
		
		gridTargets.setColumnConfigs(configs);
		gridTargets.setAutoNumber(true);
	}
	
	public void createGrid(){
		gridView.refresh();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		configs.add(donorField);
		
		ColumnConfig config = new ColumnConfig("amount", "Amount", DataType.DOUBLE);
		config.setAggregationColumn(true);
		configs.add(config);
		
		gridView.setColumnConfigs(configs);
		gridView.setAutoNumber(true);
	}
	
	@Override
	public void setFunds(List<FundDTO> funds) {
		donors.clear();
		if(funds!=null){
			for(FundDTO dto: funds){
				donors.add(dto);
			}
		}
		donorField.setDropDownItems(donors);
		gridView.refresh();
	} 

	@Override
	public void setPeriod(PeriodDTO period) {
		if(period!=null){
			spnPeriod.setText(period.getDescription());
			dtRange.setRangeValidation(period.getStartDate(), period.getEndDate());
			dtRange.setDates(period.getStartDate(), period.getEndDate());
		}
	}
	
	@Override
	public void setObjectives(List<IsProgramActivity> objectives) {
		if(objectives!=null){
			objectivesAutoComplete.setValues(objectives);
		}
	}

	public void createCrumb(String text,String title,Long id, Boolean isActive){
		BreadCrumbItem crumb = new BreadCrumbItem();
		crumb.setActive(isActive);
		crumb.setTitle(title);
		if(text.length()>25){			
			text = text.substring(0, 25)+"...";			
		}
		crumb.setLinkText(text);
		crumb.setHref("#home;page=activities;activity="+id);
		crumbContainer.add(crumb);
	}

	public void setBreadCrumbs(List<ProgramSummary> summaries) {
		crumbContainer.clear();
		for(int i=summaries.size()-1; i>-1; i--){
			ProgramSummary summary = summaries.get(i);
			createCrumb(summary.getName(),summary.getDescription(), summary.getId(), i==0);
		}
		
	}
	
	@Override
	public void setParentProgram(IsProgramActivity isProgramActivity) {
		setBreadCrumbs(isProgramActivity.getProgramSummary());
	}
	
	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if(isNullOrEmpty(txtActivity.getValue())){
			isValid = false;
			issues.addError("Activity Name is mandatory");
		}
		
		String err = dtRange.isValid();
		if(err!=null){
			isValid=false;
			issues.addError(err);
		}
		
		return isValid;

	}

	@Override
	public IsProgramActivity getActivity() {
		ProgramDTO program = new ProgramDTO();
		program.setDescription(txtActivity.getValue());
		program.setName(txtActivity.getValue());
		program.setType(ProgramDetailType.ACTIVITY);
		program.setObjectives(objectivesAutoComplete.getSelectedItems());
		
		//Targets and Outcomes
		List<TargetAndOutcomeDTO> targets = gridTargets.getData(targetAndOutcomeMapper); 
		program.setTargetsAndOutcomes(targets);
		
		//Activity Funds
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
	public void setActivity(IsProgramActivity activity) {
		if(activity==null){
			return;		
		}
		
		txtActivity.setValue(activity.getDescription());
		System.err.println(activity.getObjectives());
		objectivesAutoComplete.select(activity.getObjectives());
		
		//program.setTargetsAndOutcomes(targetsAndOutcomes);
		//
		List<Object> targets = new ArrayList<Object>();
		if(activity.getTargetsAndOutcomes()!=null)
		for(TargetAndOutcomeDTO dto: activity.getTargetsAndOutcomes()){
			targets.add(dto);
		}	
		List<DataModel> models = targetAndOutcomeMapper.getDataModels(targets); 
		gridTargets.setData(models);
		
		//Program Funds
		List<Object> lst = new ArrayList<Object>();
		for(ProgramFundDTO dto: activity.getFunding()){
			lst.add(dto);
		}	
		models = programFundMapper.getDataModels(lst); 
		gridView.setData(models);
		
	}

	@Override
	public void clear() {
		gridView.setData(new ArrayList<DataModel>());
		txtActivity.setValue(null);
		crumbContainer.clear();
		spnPeriod.setText(null);
		objectivesAutoComplete.clearSelection();
	}

	@Override
	public void setGroups(List<UserGroup> groups) {
		allocatedToGroups.setValues(groups);
	}

	@Override
	public void setUsers(List<HTUser> users) {
		allocatedToUsers.setValues(users);
	}
	

	/**
	 * Program Fund Mapper
	 */
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

	DataMapper targetAndOutcomeMapper = new DataMapper() {
		
		@Override
		public List<DataModel> getDataModels(List<Object> funding) {

			List<DataModel> models = new ArrayList<DataModel>();
			for(Object obj: funding){
				TargetAndOutcomeDTO target = (TargetAndOutcomeDTO)obj;
				DataModel model = new DataModel();
				model.setId(target.getId());
				model.set("target", target.getTarget());
				model.set("indicator", target.getMeasure());
				models.add(model);
			}
			
			return models;
		}
		
		@Override
		public TargetAndOutcomeDTO getData(DataModel model) {

			TargetAndOutcomeDTO dto = new TargetAndOutcomeDTO();
			dto.setId(model.getId());
			dto.setMeasure(model.get("indicator")==null? null:
				model.get("indicator").toString().isEmpty()? null: 
					model.get("indicator").toString());
			
			dto.setTarget(model.get("target")==null ? null: 
				(Double)model.get("target"));
			return dto;
		}
	};

}
