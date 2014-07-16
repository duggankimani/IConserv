package com.wira.pmgt.client.ui.outcome;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.TargetAndOutcomeDTO;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetPeriodRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.wira.pmgt.shared.responses.GetPeriodResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class CreateOutcomePresenter extends
		PresenterWidget<CreateOutcomePresenter.MyView> {

	public interface MyView extends View {
		void setFunds(List<FundDTO> funds);
		//void setObjectives(List<IsProgramDetail> objectives);
		void setParentProgram(IsProgramDetail isProgramActivity);
		boolean isValid();
		IsProgramDetail getOutcome();
		void setOutcome(IsProgramDetail outcome);
		void clear();
		HasClickHandlers getCopyTargetsLink();
		void setTargetsAndOutComes(List<TargetAndOutcomeDTO> clones);
	}

	@Inject
	DispatchAsync requestHelper;
	
	private List<TargetAndOutcomeDTO> parentTargetsAndOutcomes=null;
	IsProgramDetail outcome;
	private Long parentId;
	private PeriodDTO period;

	@Inject
	public CreateOutcomePresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		getView().getCopyTargetsLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				assert parentId!=null;
				List<TargetAndOutcomeDTO> clones = new ArrayList<TargetAndOutcomeDTO>();
				
				if(clones!=null)
				for(TargetAndOutcomeDTO dto: parentTargetsAndOutcomes){
					TargetAndOutcomeDTO clone = dto.clone();
					clone.setActualOutcome(0.0);
					clone.setProgramDetailId(null);
					clone.setId(null);
					clones.add(clone);
				}
				getView().setTargetsAndOutComes(clones);					
				
			}
		});
	}

	public IsProgramDetail getOutcome(){
		IsProgramDetail viewoutcome= getView().getOutcome();
		if(outcome!=null){
			viewoutcome.setId(outcome.getId());
			viewoutcome.setParentId(outcome.getParentId());
			viewoutcome.setPeriod(outcome.getPeriod());
		}else{
			viewoutcome.setParentId(parentId);
			viewoutcome.setPeriod(period);
		}
		
		return viewoutcome;
	}

	public void load(Long parentId) {
		assert parentId!=null;
		this.parentId = parentId;
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFundsRequest());
		action.addRequest(new GetPeriodRequest());
		action.addRequest(new GetProgramsRequest(parentId, false));
				
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				GetFundsResponse getFunds = (GetFundsResponse)aResponse.get(0);
				getView().setFunds(getFunds.getFunds());
				
				GetPeriodResponse getPeriod = (GetPeriodResponse)aResponse.get(1);
				period = getPeriod.getPeriod();
			
				GetProgramsResponse getProgram = (GetProgramsResponse)aResponse.get(2);
				getView().setParentProgram(getProgram.getSingleResult());
				parentTargetsAndOutcomes = getProgram.getSingleResult().getTargetsAndOutcomes();
				getView().setOutcome(outcome);
			}
		});
	}

	public void setOutcome(IsProgramDetail selected) {
		this.outcome = selected;	
		getView().clear();
	}
	
}
