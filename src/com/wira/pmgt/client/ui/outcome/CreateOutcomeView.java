package com.wira.pmgt.client.ui.outcome;

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
import com.wira.pmgt.client.ui.component.autocomplete.AutoCompleteField;
import com.wira.pmgt.client.ui.component.grid.AggregationGrid;
import com.wira.pmgt.client.ui.component.grid.ColumnConfig;
import com.wira.pmgt.client.ui.component.grid.DataMapper;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.Listable;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class CreateOutcomeView extends ViewImpl implements
		CreateOutcomePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateOutcomeView> {
	}
	
	@UiField TextArea txtOutcome;
	@UiField AggregationGrid gridView;
	@UiField BulletListPanel crumbContainer;
	@UiField InlineLabel spnPeriod;
	@UiField AutoCompleteField<IsProgramActivity> autoComplete;

	List<Listable> donors = new ArrayList<Listable>();
	ColumnConfig donorField = new ColumnConfig("donor", "Donor Name", DataType.SELECTBASIC);
	
	@Inject
	public CreateOutcomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		createGrid();
		
		txtOutcome.getElement().setAttribute("rows", "3");
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
	
	public ProgramDTO getProgram(){
		ProgramDTO program = new ProgramDTO();
		program.setDescription(txtOutcome.getValue());
		program.setId(null);
		program.setName(txtOutcome.getValue());
		program.setParentId(null); //Program ID
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
		return program;
		
	}

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


	public void createCrumb(String text, Boolean isActive){
		BreadCrumbItem crumb = new BreadCrumbItem();
		crumb.setActive(isActive);
		crumb.setLinkText(text);
		crumbContainer.add(crumb);
	}

	@Override
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
	};

	@Override
	public void setPeriod(PeriodDTO period) {
		
	}
	
	@Override
	public void setObjectives(List<IsProgramActivity> objectives) {
		autoComplete.setValues(objectives);
	}

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
			createCrumb(summary.getName(), summary.getId(), i==0);
		}
		
	}
	
	@Override
	public void setProgram(IsProgramActivity isProgramActivity) {
		setBreadCrumbs(isProgramActivity.getProgramSummary());
	}

}
