package com.wira.pmgt.client.ui.objective;

import static com.wira.pmgt.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.IssuesPanel;
import com.wira.pmgt.client.ui.component.grid.AggregationGrid;
import com.wira.pmgt.client.ui.component.grid.ColumnConfig;
import com.wira.pmgt.client.ui.component.grid.DataMapper;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.client.ui.util.StringUtils;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.TargetAndOutcomeDTO;

import static com.wira.pmgt.client.ui.util.StringUtils.*;

public class CreateObjectiveView extends ViewImpl implements
		CreateObjectivePresenter.ICreateObjectiveView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateObjectiveView> {
	}
	
	@UiField IssuesPanel issues;
	@UiField TextBox txtObjectiveRef;
	@UiField TextArea txtObjective;
	@UiField BulletListPanel crumbContainer;
	@UiField InlineLabel spnPeriod;
	@UiField AggregationGrid gridTargets;
	PeriodDTO period;
	
	@Inject
	public CreateObjectiveView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		txtObjective.getElement().setAttribute("rows", "5");
		
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
	public void setObjective(IsProgramDetail objective) {
		if(objective==null){
			return;
		}
		
		txtObjective.setValue(objective.getDescription());
		txtObjectiveRef.setValue(objective.getName());
		setTargetsAndOutComes(objective.getTargetsAndOutcomes());
		setPeriod(period);
		
	}
	
	public ProgramDTO getObjective(){
		ProgramDTO program = new ProgramDTO();
		program.setDescription(txtObjective.getValue());
		program.setName(txtObjectiveRef.getValue());
		program.setType(ProgramDetailType.OBJECTIVE);
		//Targets and Outcomes
		List<TargetAndOutcomeDTO> targets = gridTargets.getData(targetAndOutcomeMapper); 
		program.setTargetsAndOutcomes(targets);
				
//		List<ProgramFundDTO> funding = gridView.getData(programFundMapper);
//		program.setFunding(funding);
//		Double totalAmount=0.0;
//		for(ProgramFundDTO programFund: funding){
//			Double val = programFund.getAmount();
//			if(val!=null){
//				totalAmount+=val;
//			}
//		}
//		program.setBudgetAmount(totalAmount);
		return program;
		
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
	public void setPeriod(PeriodDTO period) {
		
		this.period = period;
		if(period==null){
			return;
		}
		spnPeriod.setText(period.getDescription());
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
	
	public void createCrumb(String text,Long id, Boolean isActive){
		BreadCrumbItem crumb = new BreadCrumbItem();
		crumb.setActive(isActive);
		crumb.setLinkText(text);
		crumb.setHref("#home;page=activities;activity="+id);
		crumbContainer.add(crumb);
	}

	@Override
	public void setBreadCrumbs(List<ProgramSummary> summaries) {
		for(int i=summaries.size()-1; i>-1; i--){
			ProgramSummary summary = summaries.get(i);
			createCrumb(summary.getName(), summary.getId(), i==0);
		}
		
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if(isNullOrEmpty(txtObjectiveRef.getValue())){
			isValid = false;
			issues.addError("Program reference is mandatory");
		}
		
		if(isNullOrEmpty(txtObjective.getValue())){
			isValid = false;
			issues.addError("Program Description is mandatory");
		}
				
		return isValid;
	}

	@Override
	public void clear() {
		txtObjective.setValue("");
		txtObjectiveRef.setValue("");
		crumbContainer.clear();
		spnPeriod.setText("");
	}

}
