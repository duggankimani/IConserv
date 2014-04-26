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
import com.wira.pmgt.client.ui.OptionControl;
import com.wira.pmgt.client.ui.activities.ActivitySelectionChangedEvent.ActivitySelectionChangedHandler;
import com.wira.pmgt.client.ui.detailedActivity.CreateActivityPresenter;
import com.wira.pmgt.client.ui.events.CreateProgramEvent;
import com.wira.pmgt.client.ui.objective.CreateObjectivePresenter;
import com.wira.pmgt.client.ui.outcome.CreateOutcomePresenter;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.requests.CreateProgramRequest;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.CreateProgramResponse;
import com.wira.pmgt.shared.responses.GetPeriodsResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class ActivitiesPresenter extends
		PresenterWidget<ActivitiesPresenter.IActivitiesView> implements
		ActivitySelectionChangedHandler {

	public interface IActivitiesView extends View {
		void showContent(boolean show);

		HasClickHandlers getNewOutcome();

		HasClickHandlers getNewActivityLink();

		HasClickHandlers getNewObjectiveLink();
		
		HasClickHandlers getEditLink();

		void setActivities(List<IsProgramActivity> programs);

		void setPrograms(List<IsProgramActivity> programs);

		void setActivity(IsProgramActivity singleResult);

		void setSelection(ProgramDetailType type);

		void setPeriods(List<PeriodDTO> periods);

		HasClickHandlers getProgramEdit();
	}

	@Inject
	DispatchAsync requestHelper;
	@Inject
	CreateOutcomePresenter createOutcome;
	@Inject
	CreateActivityPresenter createActivity;
	@Inject
	CreateObjectivePresenter objectivePresenter;

	Long programId;
	
	IsProgramActivity selected;

	@Inject
	public ActivitiesPresenter(final EventBus eventBus,
			final IActivitiesView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ActivitySelectionChangedEvent.TYPE, this);
		
		getView().getProgramEdit().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new CreateProgramEvent(programId));
			}
		});
		
		
		getView().getNewOutcome().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showEditPopup(ProgramDetailType.OUTCOME);
			}
		});

		getView().getNewActivityLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showEditPopup(ProgramDetailType.ACTIVITY);
			}
		});

		getView().getNewObjectiveLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showEditPopup(ProgramDetailType.OBJECTIVE);
			}
		});
		
		getView().getEditLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showEditPopup(selected.getType(), true);
			}
		});

	}
	
	protected void showEditPopup(ProgramDetailType type){
		showEditPopup(type, false);
	}
	
	protected void showEditPopup(ProgramDetailType type, boolean edit){
		
		switch (type) {
		case ACTIVITY:
			//Use selected an outcome
			createActivity.setActivity(edit?selected:null);
			AppManager.showPopUp("Create Activity",
					createActivity.getWidget(), new OptionControl() {
						
						@Override
						public void onSelect(String name) {
							
							if(name.equals("Save")){
								if(createActivity.getView().isValid()){
									save(createActivity.getView().getActivity());
									hide();
								}
							}else{
								hide();
							}
							
						}}, "Save", "Cancel");
			break;
	
		case OBJECTIVE:
			objectivePresenter.setObjective(edit?selected:null);
			objectivePresenter.load(programId);//Parent Id Passed here
			AppManager.showPopUp("Add Objective", objectivePresenter.getWidget(), new OptionControl() {
				
				@Override
				public void onSelect(String name) {
					if(name.equals("Save")){
						if(objectivePresenter.getView().isValid()){
							save(objectivePresenter.getObjective());
							hide();
						}
					}else{hide();}
				}

			}, "Save", "Cancel");
			
			break;
			
		case OUTCOME:
			createOutcome.setOutcome(edit?selected:null);
			createOutcome.load(programId);
			AppManager.showPopUp("Create Outcome",
					createOutcome.getWidget(), new OptionControl() {
						
						@Override
						public void onSelect(String name) {
							
							if(name.equals("Save")){
								if(createOutcome.getView().isValid()){
									save(createOutcome.getOutcome());
									hide();
								}
							}else{
								hide();
							}
							
						}}, "Save", "Cancel");
			break;
			
		case TASK:
			
			break;
		default:
			break;
		}
	}

	private void save(IsProgramActivity program) {
		program.setParentId(programId);
		requestHelper.execute(new CreateProgramRequest(program),
				new TaskServiceCallback<CreateProgramResponse>() {
					@Override
					public void processResult(CreateProgramResponse aResponse) {
						loadData(programId);
					}
				});
	}

	public void loadData(final Long activityId) {
		final boolean hasProgramId = activityId != null && activityId != 0L;

		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProgramsRequest(ProgramDetailType.PROGRAM,
				false));
		action.addRequest(new GetPeriodsRequest());

		if (hasProgramId) {
			this.programId = activityId;
			action.addRequest(new GetProgramsRequest(activityId, true));
		}

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						GetProgramsResponse response = (GetProgramsResponse) aResponse
								.get(0);
						getView().setPrograms(response.getPrograms());

						GetPeriodsResponse getPeriod = (GetPeriodsResponse) aResponse
								.get(1);
						getView().setPeriods(getPeriod.getPeriods());

						// activities under a program
						if (hasProgramId) {
							GetProgramsResponse response2 = (GetProgramsResponse) aResponse
									.get(2);
							getView().setActivity(response2.getSingleResult());

						}else{
							getView().setActivities(response.getPrograms());
						}
						//else {
//							// load activities under default program
//							if (response.getPrograms() != null
//									&& !response.getPrograms().isEmpty()) {
//								loadProgram(response.getPrograms().get(0)
//										.getId());
//							}
//						}
					}
				});
	}

	protected void loadProgram(Long id) {
		this.programId = id;
		GetProgramsRequest request = new GetProgramsRequest(id, true);

		requestHelper.execute(request,
				new TaskServiceCallback<GetProgramsResponse>() {
					@Override
					public void processResult(GetProgramsResponse aResponse) {
						GetProgramsResponse response = (GetProgramsResponse) aResponse;
						getView().setActivity(response.getSingleResult());
					}
				});

	}

	@Override
	public void onActivitySelectionChanged(ActivitySelectionChangedEvent event) {
		if (event.isSelected()) {
			this.selected = event.getProgramActivity();
			getView().setSelection(event.getProgramActivity().getType());
		} else {
			this.selected = null;
			getView().setSelection(ProgramDetailType.PROGRAM);
		}
	}

}
