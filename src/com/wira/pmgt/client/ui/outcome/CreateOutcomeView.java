package com.wira.pmgt.client.ui.outcome;

import static com.wira.pmgt.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.IssuesPanel;
import com.wira.pmgt.client.ui.component.autocomplete.AutoCompleteField;
import com.wira.pmgt.client.ui.component.grid.AggregationGrid;
import com.wira.pmgt.client.ui.component.grid.ColumnConfig;
import com.wira.pmgt.client.ui.component.grid.DataMapper;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.client.ui.util.StringUtils;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.Listable;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.TargetAndOutcomeDTO;

public class CreateOutcomeView extends ViewImpl implements
		CreateOutcomePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateOutcomeView> {
	}
	
	@UiField IssuesPanel issues;
	@UiField TextArea txtOutcome;
	@UiField TextBox txtOutcomeRef;
	@UiField AggregationGrid gridView;
	@UiField BulletListPanel crumbContainer;
	@UiField AggregationGrid gridTargets;
	@UiField InlineLabel spnPeriod;
	@UiField Anchor aCopyTargets;

	List<Listable> donors = new ArrayList<Listable>();
	ColumnConfig donorField = new ColumnConfig("donor", "Donor Name", DataType.SELECTBASIC);
	
	@Inject
	public CreateOutcomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		createGrid();
		
		txtOutcome.getElement().setAttribute("rows", "3");
		createTargetsAndIndicatorsGrid();
	}
	
	private void createTargetsAndIndicatorsGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
//		ColumnConfig config = new ColumnConfig("condition", "Check", DataType.SELECTBASIC);
//		configs.add(config);
		
		ColumnConfig config = new ColumnConfig("target", "Target", DataType.DOUBLE, "1000");
		configs.add(config);
		
		config = new ColumnConfig("indicator", "Indicator", DataType.STRING, "Participants");
		configs.add(config);
		
		config = new ColumnConfig("actual", "Outcome", DataType.DOUBLE, "1000");
		configs.add(config);
		
		gridTargets.setColumnConfigs(configs);
		gridTargets.setAutoNumber(true);
	}

	@Override
	public Widget asWidget() {
		return widget;
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
		Collections.sort(funds, new Comparator<FundDTO>() {
			@Override
			public int compare(FundDTO o1, FundDTO o2) {
				
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
		if(funds!=null){
			for(FundDTO dto: funds){
				donors.add(dto);
			}
		}
		donorField.setDropDownItems(donors);
		gridView.refresh();
	} 


	public void createCrumb(String text, Boolean isActive){
		BreadCrumbItem crumb = new BreadCrumbItem();
		crumb.setActive(isActive);
		crumb.setLinkText(text);
		crumbContainer.add(crumb);
	}

	public void setPeriod(String period) {
		if(period!=null){
			spnPeriod.getElement().setInnerText(period);
		}
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

//	@Override
//	public void setObjectives(List<IsProgramDetail> objectives) {
//		if(objectives!=null){
//			autoComplete.setValues(objectives);
//		}
//	}

	public void createCrumb(String text,Long id, Boolean isActive){
		BreadCrumbItem crumb = new BreadCrumbItem();
		crumb.setActive(isActive);
		crumb.setLinkText(text);
		crumb.setHref("#home;page=activities;activity="+id);
		crumbContainer.add(crumb);
	}

	public void setBreadCrumbs(List<ProgramSummary> summaries) {
		for(int i=summaries.size()-1; i>-1; i--){
			ProgramSummary summary = summaries.get(i);
			createCrumb(summary.getName()+" - "+summary.getDescription(), summary.getId(), i==0);
		}
		
	}
	
	@Override
	public void setParentProgram(IsProgramDetail isProgramActivity) {
		setBreadCrumbs(isProgramActivity.getProgramSummary());
		if(isProgramActivity.getTargetsAndOutcomes()==null || isProgramActivity.getTargetsAndOutcomes().isEmpty()){
			aCopyTargets.addStyleName("hide");
		}else{
			aCopyTargets.removeStyleName("hide");
		}
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if(isNullOrEmpty(txtOutcomeRef.getValue())){
			isValid = false;
			issues.addError("Outcome Ref is mandatory");
		}
		
		if(isNullOrEmpty(txtOutcome.getValue())){
			isValid = false;
			issues.addError("Outcome is mandatory");
		}
		
		List<ProgramFundDTO> funding = gridView.getData(programFundMapper);
		for(ProgramFundDTO dto: funding){
			int frequency = Collections.frequency(funding, dto);
			if(frequency>1){
				isValid=false;
				issues.addError(dto.getFund().getName()+" is repeated");
			}
		}
		
		return isValid;
	}

	@Override
	public IsProgramDetail getOutcome() {
		ProgramDTO program = new ProgramDTO();
		program.setDescription(txtOutcome.getValue());
		program.setName(txtOutcomeRef.getValue());
		program.setType(ProgramDetailType.OUTCOME);
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
		
		//Targets and Outcomes
		List<TargetAndOutcomeDTO> targets = gridTargets.getData(targetAndOutcomeMapper); 
		program.setTargetsAndOutcomes(targets);
				
		return program;
		
	}

	@Override
	public void setOutcome(IsProgramDetail outcome) {
		if(outcome==null){
			return;		
		}
		
		txtOutcomeRef.setValue(outcome.getName());
		txtOutcome.setValue(outcome.getDescription());
		Collections.sort(outcome.getFunding(), new Comparator<ProgramFundDTO>() {
			@Override
			public int compare(ProgramFundDTO o1, ProgramFundDTO o2) {
				
				return o1.getFund().getName().compareTo(o2.getFund().getName());
			}
		});
		//program.setTargetsAndOutcomes(targetsAndOutcomes);
		List<Object> lst = new ArrayList<Object>();
		for(ProgramFundDTO dto: outcome.getFunding()){
			lst.add(dto);
		}		
		
		List<DataModel> models = programFundMapper.getDataModels(lst); 
		gridView.setData(models);
		
		setTargetsAndOutComes(outcome.getTargetsAndOutcomes());
	}
	
	public void setTargetsAndOutComes(
			List<TargetAndOutcomeDTO> targetsAndOutComes) {
		Collections.sort(targetsAndOutComes, new Comparator<TargetAndOutcomeDTO>(){
			@Override
			public int compare(TargetAndOutcomeDTO o1, TargetAndOutcomeDTO o2) {
				
				return o1.getMeasure().compareTo(o2.getMeasure());
			}
		});
		
		List<Object> targets = new ArrayList<Object>();
		
		if(targetsAndOutComes==null || targetsAndOutComes.isEmpty()){
			return;
		}			
			
		for(TargetAndOutcomeDTO dto: targetsAndOutComes){
			targets.add(dto);
		}	
		List<DataModel> models = targetAndOutcomeMapper.getDataModels(targets); 
		gridTargets.setData(models);
				
	}
	
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
				model.set("actual", target.getActualOutcome());
				models.add(model);
			}
			
			return models;
		}
		
		@Override
		public TargetAndOutcomeDTO getData(DataModel model) {

			if(model.isEmpty()){
				return null;
			}
			
			TargetAndOutcomeDTO dto = new TargetAndOutcomeDTO();
			dto.setId(model.getId());
			dto.setMeasure(isNullOrEmpty(model.get("indicator"))? null: 
					model.get("indicator").toString());
			
			dto.setTarget(model.get("target")==null ? null: 
				(Double)model.get("target"));
			
			dto.setActualOutcome(model.get("actual")==null ? null: 
				(Double)model.get("actual"));
			
			dto.setKey(StringUtils.camelCase(dto.getMeasure()));
			return dto;
		}
	};


	@Override
	public void clear() {
		gridView.setData(new ArrayList<DataModel>());
		txtOutcome.setValue(null);
		txtOutcomeRef.setValue(null);
		crumbContainer.clear();
		spnPeriod.setText(null);
	}

	@Override
	public HasClickHandlers getCopyTargetsLink() {
		
		return aCopyTargets;
	}


}
