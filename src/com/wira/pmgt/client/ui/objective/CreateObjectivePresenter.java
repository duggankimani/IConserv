package com.wira.pmgt.client.ui.objective;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetPeriodRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;

public class CreateObjectivePresenter extends
		PresenterWidget<CreateObjectivePresenter.ICreateObjectiveView> {

	public interface ICreateObjectiveView extends View {
		void setPeriod(PeriodDTO period);
	}
	
	@Inject DispatchAsync requestHelper;

	@Inject
	public CreateObjectivePresenter(final EventBus eventBus, final ICreateObjectiveView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		loadList();
	}

	private void loadList() {
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFundsRequest());
		action.addRequest(new GetPeriodRequest());
//		
//		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
//			@Override
//			public void processResult(MultiRequestActionResult aResponse) {
//				GetFundsResponse getFunds = (GetFundsResponse)aResponse.get(0);
//				getView().setFunds(getFunds.getFunds());
//				
//				GetPeriodResponse getPeriod = (GetPeriodResponse)aResponse.get(1);
//				getView().setPeriod(getPeriod.getPeriod());
//			}
//		});
	}
}
