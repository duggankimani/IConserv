package com.wira.pmgt.client.ui.objective;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class CreateObjectivePresenter extends
		PresenterWidget<CreateObjectivePresenter.ICreateObjectiveView> {

	public interface ICreateObjectiveView extends View {
		void setPeriod(PeriodDTO period);

		void setBreadCrumbs(List<ProgramSummary> programSummary);

		void setObjective(IsProgramActivity singleResult);
		
		IsProgramActivity getProgram();

		boolean isValid();
	}
	
	@Inject DispatchAsync requestHelper;

	IsProgramActivity parent=null;
	Long objectiveId=null;
	
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
	}

	public void loadList(Long parentId, final Long objectiveId) {
		this.objectiveId = objectiveId;
		
		assert parentId!=null;
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProgramsRequest(parentId, false));
		if(objectiveId!=null){
			action.addRequest(new GetProgramsRequest(parentId, false));
		}
		
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {				
				
				GetProgramsResponse response = (GetProgramsResponse)aResponse.get(0);
				parent = response.getSingleResult();
				getView().setPeriod(parent.getPeriod());
				getView().setBreadCrumbs(parent.getProgramSummary());
				
				if(objectiveId!=null){
					GetProgramsResponse response2 = (GetProgramsResponse)aResponse.get(1);
					getView().setObjective(response2.getSingleResult());
				}
			}
		});
	}
}
