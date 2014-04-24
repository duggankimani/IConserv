package com.wira.pmgt.client.ui.outcome;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.Objective;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetPeriodRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.wira.pmgt.shared.responses.GetPeriodResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class CreateOutcomePresenter extends
		PresenterWidget<CreateOutcomePresenter.MyView> {

	public interface MyView extends View {
		void setFunds(List<FundDTO> funds);

		void setPeriod(PeriodDTO period);

		void setPeriod(String period);

		void setObjectives(List<Objective> objectives);
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
		
		List<Objective> objList = new ArrayList<Objective>();
		
		Objective obj1 = new Objective();
		obj1.setObjName("Increase Understanding in value of wildlife");
		obj1.setObjRef("Obj 2.1");
		objList.add(obj1);
		
		Objective obj2 = new Objective();
		obj2.setObjName("Increase Understanding in value of Forest");
		obj2.setObjRef("Obj 2.2");
		objList.add(obj2);
		
		Objective obj3 = new Objective();
		obj1.setObjName("Increase Understanding in value of education");
		obj1.setObjRef("Obj 2.3");
		objList.add(obj3);
		
		getView().setObjectives(objList);
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
		
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				GetFundsResponse getFunds = (GetFundsResponse)aResponse.get(0);
				getView().setFunds(getFunds.getFunds());
				
				GetPeriodResponse getPeriod = (GetPeriodResponse)aResponse.get(1);
				getView().setPeriod(getPeriod.getPeriod());
			}
		});
	}
	
}
