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
import com.wira.pmgt.client.ui.events.ActivitySavedEvent;
import com.wira.pmgt.client.ui.objective.CreateObjectivePresenter;
import com.wira.pmgt.client.ui.outcome.CreateOutcomePresenter;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.requests.CreateProgramRequest;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.CreateProgramResponse;
import com.wira.pmgt.shared.responses.GetFundsResponse;
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
		
		HasClickHandlers getNewTaskLink();
		
		HasClickHandlers getEditLink();

		void setActivities(List<IsProgramActivity> programs);

		void setPrograms(List<IsProgramActivity> programs);

		void setActivity(IsProgramActivity singleResult);

		void setSelection(ProgramDetailType type);

		void setPeriods(List<PeriodDTO> periods);

		HasClickHandlers getProgramEdit();

		//void setSummaryView(boolean hasProgramId);

		void setFunds(List<FundDTO> funds);

		void setLastUpdatedId(Long id);

		void setProgramId(Long programId);

		void setDates(String text);
	}

	@Inject
	DispatchAsync requestHelper;
	@Inject
	CreateOutcomePresenter createOutcome;
	@Inject
	CreateActivityPresenter createActivity;
	@Inject
	CreateObjectivePresenter objectivePresenter;
	@Inject
	CreateActivityPresenter createTask;
	

	Long programId;
	
	Long programDetailId; //Drill Down
	
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
		
		getView().getNewTaskLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showEditPopup(ProgramDetailType.TASK);
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
		case TASK:
			//Use 
			createTask.setType(type);
			if(edit){
				//we are editing the selected item
				createTask.setActivity(selected);
				createTask.load(selected.getParentId());
			}else{
				//selected item is the parent - We are creating a new activity based on selected item
				createTask.setActivity(null);
				createTask.load(selected.getId());
			}
			
			AppManager.showPopUp(edit? "Edit Task":
				"Create Task",
					createTask.getWidget(), new OptionControl() {
						@Override
						public void onSelect(String name) {
							
							if(name.equals("Save")){
								if(createTask.getView().isValid()){
									IsProgramActivity activity=createTask.getActivity();
									//System.err.println("")
									save(activity);
									hide();
								}
							}else{
								hide();
							}
							
						}}, "Save", "Cancel");
			break;
		case ACTIVITY:
			if(edit){
				//we are editing the selected item
				createActivity.setActivity(selected);
				createActivity.load(selected.getParentId());
			}else{
				//selected item is the parent - We are creating a new activity based on selected item
				//User selected an outcome & is now creating a new Activity
				createActivity.setActivity(null);
				createActivity.load(selected.getId());
			}
			
			AppManager.showPopUp(edit? "Edit Activity":
				"Create Activity",
					createActivity.getWidget(), new OptionControl() {
						@Override
						public void onSelect(String name) {
							
							if(name.equals("Save")){
								if(createActivity.getView().isValid()){
									IsProgramActivity activity=createActivity.getActivity();
									//System.err.println("")
									save(activity);
									hide();
								}
							}else{
								hide();
							}
							
						}}, "Save", "Cancel");
			break;
	
		case OBJECTIVE:
			objectivePresenter.setObjective(
					(edit && selected.getType()==ProgramDetailType.OBJECTIVE)?selected:null);
			Long parentId = edit?selected.getParentId():programId;
			
			//in case you selected an Program from summary view and clicked New Objective
			parentId= parentId!=null? parentId : selected!=null? selected.getId():null;
			objectivePresenter.load(parentId);//Parent Id Passed here
			
			AppManager.showPopUp(edit?"Edit Objective"
					:"Add Objective", objectivePresenter.getWidget(), new OptionControl() {
				
				@Override
				public void onSelect(String name) {
					if(name.equals("Save")){
						if(objectivePresenter.getView().isValid()){
							IsProgramActivity objective =objectivePresenter.getObjective();		
							objective.setParentId(programId);
							save(objective);
							hide();
						}
					}else{hide();}
				}

			}, "Save", "Cancel");
			
			break;
			
		case OUTCOME:
			createOutcome.setOutcome(edit?selected:null);			
			createOutcome.load(edit?selected.getParentId():programId);
			AppManager.showPopUp(edit? "Edit Outcome":
					"Create Outcome",
					createOutcome.getWidget(), new OptionControl() {
						
						@Override
						public void onSelect(String name) {
							
							if(name.equals("Save")){
								if(createOutcome.getView().isValid()){
									IsProgramActivity outcome =createOutcome.getOutcome();
									outcome.setParentId(programId);
									save(outcome);
									hide();
								}
							}else{
								hide();
							}
							
						}}, "Save", "Cancel");
			break;
		case PROGRAM:
			if(selected!=null && edit)
				AppContext.fireEvent(new CreateProgramEvent(selected.getId()));
		break;
		default:
			break;
		}
	}

	private void save(final IsProgramActivity activity) {
		//program.setParentId(programId);
		requestHelper.execute(new CreateProgramRequest(activity),
				new TaskServiceCallback<CreateProgramResponse>() {
					@Override
					public void processResult(CreateProgramResponse aResponse) {
						fireEvent(new ActivitySavedEvent(activity.getType().name().toLowerCase() +" change(s) successfully saved"));
						getView().setLastUpdatedId(aResponse.getProgram().getId());
						loadData(programId);
					}
				});
	}
	public void loadData(final Long activityId){
		loadData(activityId, programDetailId);
	}
	
	public void loadData(final Long programId, Long detailId) {
		this.programId = (programId ==null || programId==0L) ? null : programId;
		programDetailId = detailId==null? null: detailId==0? null:
			detailId;
		
		getView().setProgramId(this.programId);
		
		MultiRequestAction action = new MultiRequestAction();
		//List of Programs for tabs
		action.addRequest(new GetProgramsRequest(ProgramDetailType.PROGRAM,false));
		action.addRequest(new GetPeriodsRequest());
		action.addRequest(new GetFundsRequest());

		if (this.programId!=null) {
			//Details of selected program
			this.programId = programId;
			action.addRequest(new GetProgramsRequest(programId, programDetailId==null));
		}
		

		if(programDetailId!=null){
			action.addRequest(new GetProgramsRequest(programDetailId, true));
		}
		
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						int i=0;
						//Programs
						GetProgramsResponse response = (GetProgramsResponse) aResponse
								.get(i++);
						getView().setPrograms(response.getPrograms());

						//Periods
						GetPeriodsResponse getPeriod = (GetPeriodsResponse) aResponse
								.get(i++);
						getView().setPeriods(getPeriod.getPeriods());

						GetFundsResponse getFundsReq = (GetFundsResponse)aResponse.get(i++);
						getView().setFunds(getFundsReq.getFunds());
						
						// activities under a program
						if (ActivitiesPresenter.this.programId!=null) {
							GetProgramsResponse response2 = (GetProgramsResponse) aResponse
									.get(i++);
							getView().setActivity(response2.getSingleResult());

						}else{
							getView().setActivities(response.getPrograms());
						}
						
						if(programDetailId!=null){
							GetProgramsResponse response2 = (GetProgramsResponse) aResponse
									.get(i++);
							getView().setActivity(response2.getSingleResult());
						}
						
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
			getView().setSelection(null);
		}
	}

}
