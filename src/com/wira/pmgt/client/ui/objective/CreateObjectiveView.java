package com.wira.pmgt.client.ui.objective;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.grid.DataMapper;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;

public class CreateObjectiveView extends ViewImpl implements
		CreateObjectivePresenter.ICreateObjectiveView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateObjectiveView> {
	}
	
	@UiField TextBox txtObjectiveRef;
	@UiField TextArea txtObjective;
	
	@Inject
	public CreateObjectiveView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public ProgramDTO getProgram(){
		ProgramDTO program = new ProgramDTO();
		program.setDescription(txtObjective.getValue());
		program.setId(null);
		program.setName(txtObjectiveRef.getValue());
		program.setParentId(null); //Program ID
		program.setType(ProgramDetailType.OUTCOME);
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

}
