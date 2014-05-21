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
		void setObjective(IsProgramActivity objective);
		IsProgramActivity getObjective();
		boolean isValid();
		void clear();
	}
	
	@Inject DispatchAsync requestHelper;
	IsProgramActivity objective;
	Long parentId = null;
	
	@Inject
	public CreateObjectivePresenter(final EventBus eventBus, final ICreateObjectiveView view) {
		super(eventBus, view);
	}

	public IsProgramActivity getObjective(){
		IsProgramActivity viewObjective = getView().getObjective();
		if(objective!=null){
			objective.setDescription(viewObjective.getDescription());
			objective.setName(viewObjective.getName());
			return objective;
		}else{
			viewObjective.setParentId(parentId);
			return viewObjective;
		}
	}

	public void load(Long parentId) {
		this.parentId = parentId;
		assert parentId!=null;
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProgramsRequest(parentId, false));
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {				
				
				GetProgramsResponse response = (GetProgramsResponse)aResponse.get(0);
				IsProgramActivity parent = response.getSingleResult();
				getView().setPeriod(parent.getPeriod());
				getView().setBreadCrumbs(parent.getProgramSummary());
				getView().setObjective(objective);
			}
		});
	}

	public void setObjective(IsProgramActivity selected) {
		this.objective = selected;
		getView().clear();
	}

}
