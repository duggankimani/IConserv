package com.wira.pmgt.client.ui.activities;

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
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.detailedActivity.CreateActivityPresenter;
import com.wira.pmgt.client.ui.outcome.CreateOutcomePresenter;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class ActivitiesPresenter extends
		PresenterWidget<ActivitiesPresenter.IActivitiesView> {

	public interface IActivitiesView extends View {
		void showContent(boolean b);
		HasClickHandlers getaNewOutcome();
		HasClickHandlers getaNewActivity();
		void setActivities(List<IsProgramActivity> programs);
		void setPrograms(List<IsProgramActivity> programs);
		void setActivity(IsProgramActivity singleResult);
	}

	@Inject DispatchAsync requestHelper;
	@Inject CreateOutcomePresenter createOutcome;
	@Inject CreateActivityPresenter createActivity;

	@Inject
	public ActivitiesPresenter(final EventBus eventBus,
			final IActivitiesView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getaNewOutcome().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Create Outcome",
						createOutcome.getWidget(), null, "Save", "Cancel");
			}
		});

		getView().getaNewActivity().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Create Activity",
						createActivity.getWidget(), null, "Save", "Cancel");
			}
		});

	}

	public void loadData(final Long activityId) {
		final boolean hasActivityId =activityId!=null && activityId!=0L;
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProgramsRequest(ProgramDetailType.PROGRAM, false));
		if(hasActivityId){
			action.addRequest(new GetProgramsRequest(activityId, true));
		}
		
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				GetProgramsResponse response = (GetProgramsResponse)aResponse.get(0);
				getView().setPrograms(response.getPrograms());
				
				//activities under a program
				if(hasActivityId){
					GetProgramsResponse response2 = (GetProgramsResponse)aResponse.get(1);
					getView().setActivity(response2.getSingleResult());
					
				}else{
					//load activities under default program
					if(response.getPrograms()!=null && !response.getPrograms().isEmpty()){
						loadProgram(response.getPrograms().get(0).getId());
					}
				}
			}
		});
	}

	protected void loadProgram(Long id) {
		
		GetProgramsRequest request = new GetProgramsRequest(id,true);
		
		requestHelper.execute(request, new TaskServiceCallback<GetProgramsResponse>() {
			@Override
			public void processResult(GetProgramsResponse aResponse) {
				GetProgramsResponse response = (GetProgramsResponse)aResponse;
				getView().setActivity(response.getSingleResult());
			}
		});
		
	}
	
	
	
}
