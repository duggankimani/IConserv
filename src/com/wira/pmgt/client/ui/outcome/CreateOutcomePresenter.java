package com.wira.pmgt.client.ui.outcome;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
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

		void setPeriod(PeriodDTO period);

		void setObjectives(List<IsProgramActivity> objectives);

		void setProgram(IsProgramActivity isProgramActivity);

		boolean isValid();

		IsProgramActivity getOutcome();
	}

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public CreateOutcomePresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
	}

	public void loadList(Long parentId) {
		assert parentId!=null;
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFundsRequest());
		action.addRequest(new GetPeriodRequest());
		action.addRequest(new GetProgramsRequest(ProgramDetailType.OBJECTIVE, false));
		action.addRequest(new GetProgramsRequest(parentId, false));
				
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				GetFundsResponse getFunds = (GetFundsResponse)aResponse.get(0);
				getView().setFunds(getFunds.getFunds());
				
				GetPeriodResponse getPeriod = (GetPeriodResponse)aResponse.get(1);
				getView().setPeriod(getPeriod.getPeriod());
			
				GetProgramsResponse getPrograms = (GetProgramsResponse)aResponse.get(2);
				getView().setObjectives(getPrograms.getPrograms());
				
				GetProgramsResponse getProgram = (GetProgramsResponse)aResponse.get(3);
				getView().setProgram(getProgram.getSingleResult());
			}
		});
	}
	
}
