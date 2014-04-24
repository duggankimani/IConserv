package com.wira.pmgt.client.ui.objective;

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
import com.wira.pmgt.client.ui.component.grid.DataMapper;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;

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
	PeriodDTO period;
	
	@Inject
	public CreateObjectiveView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		txtObjective.getElement().setAttribute("rows", "5");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public ProgramDTO getProgram(){
		assert period!=null;
		ProgramDTO program = new ProgramDTO();
		program.setDescription(txtObjective.getValue());
		program.setId(null);
		program.setName(txtObjectiveRef.getValue());
		program.setParentId(null); //Program ID
		program.setType(ProgramDetailType.OBJECTIVE);
		program.setPeriod(period);
		//program.setTargetsAndOutcomes(targetsAndOutcomes);
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
	
	@Override
	public void setPeriod(PeriodDTO period) {
		this.period = period;
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
	public void setObjective(IsProgramActivity singleResult) {
		
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

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

}
