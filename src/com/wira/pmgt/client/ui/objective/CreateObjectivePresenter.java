package com.wira.pmgt.client.ui.objective;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
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
		void setObjective(IsProgramDetail objective);
		IsProgramDetail getObjective();
		boolean isValid();
		void clear();
	}
	
	@Inject DispatchAsync requestHelper;
	IsProgramDetail objective;
	
	@Inject
	public CreateObjectivePresenter(final EventBus eventBus, final ICreateObjectiveView view) {
		super(eventBus, view);
	}

	public IsProgramDetail getObjective(){
		IsProgramDetail viewObjective = getView().getObjective();
		viewObjective.setParentId(null);
		if(objective!=null){
			objective.setDescription(viewObjective.getDescription());
			objective.setName(viewObjective.getName());
			return objective;
		}else{
			return viewObjective;
		}
	}

	public void setObjective(IsProgramDetail selected) {
		this.objective = selected;
		getView().clear();
		getView().setObjective(objective);
		
	}

}
