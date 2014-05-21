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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.wira.pmgt.client.place.NameTokens;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.OptionControl;
import com.wira.pmgt.client.ui.assign.AssignActivityPresenter;
import com.wira.pmgt.client.ui.detailedActivity.CreateActivityPresenter;
import com.wira.pmgt.client.ui.document.activityview.ActivityDetailPresenter;
import com.wira.pmgt.client.ui.events.ActivitiesReloadEvent;
import com.wira.pmgt.client.ui.events.ActivitiesReloadEvent.ActivitiesReloadHandler;
import com.wira.pmgt.client.ui.events.ActivitySavedEvent;
import com.wira.pmgt.client.ui.events.ActivitySelectionChangedEvent;
import com.wira.pmgt.client.ui.events.ActivitySelectionChangedEvent.ActivitySelectionChangedHandler;
import com.wira.pmgt.client.ui.events.CreateProgramEvent;
import com.wira.pmgt.client.ui.events.LoadAlertsEvent;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.objective.CreateObjectivePresenter;
import com.wira.pmgt.client.ui.outcome.CreateOutcomePresenter;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.ParticipantType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.requests.AssignTaskRequest;
import com.wira.pmgt.shared.requests.CreateProgramRequest;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.AssignTaskResponse;
import com.wira.pmgt.shared.responses.CreateProgramResponse;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.wira.pmgt.shared.responses.GetPeriodsResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class ActivitiesPresenter extends
		PresenterWidget<ActivitiesPresenter.IActivitiesView> implements
		ActivitySelectionChangedHandler, ActivitiesReloadHandler {
	
	public static final Object DETAIL_SLOT = new Object();
	
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

		// void setSummaryView(boolean hasProgramId);

		void setFunds(List<FundDTO> funds);

		void setLastUpdatedId(Long id);

		void setProgramId(Long programId);

		void setSelection(ProgramDetailType programType, boolean isRowData);

		HasClickHandlers getAddButton();

		HasClickHandlers getaAssign();

		HasClickHandlers getDetailButton();

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
	@Inject
	AssignActivityPresenter assignActivity;
	
	@Inject
	ActivityDetailPresenter activityDetail;
	
	@Inject PlaceManager placeManager;

	Long programId;

	Long programDetailId; // Drill Down

	ProgramDetailType programType = ProgramDetailType.PROGRAM; // last selected

	IsProgramActivity selected;
	IsProgramActivity detail;

	@Inject
	public ActivitiesPresenter(final EventBus eventBus,
			final IActivitiesView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ActivitySelectionChangedEvent.TYPE, this);
		addRegisteredHandler(ActivitiesReloadEvent.TYPE, this);

		// getView().getProgramEdit().addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// AppContext.fireEvent(new CreateProgramEvent(programId));
		// }
		// });
		
		getView().getDetailButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(
						new PlaceRequest(NameTokens.home)
						.with("page", "detailed")
						.with("activityid", selected.getId()+"")
						);
			}
		});

		getView().getaAssign().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(selected!=null){
					assignActivity.load(selected.getId());
				}
				AppManager.showPopUp("Assign Activity",
						assignActivity.getWidget(), new OptionControl() {
							@Override
							public void onSelect(String name) {
							
								if (name.equals("Cancel")) {
									hide();
									return;
								}

								assignActivity.addItems(); //Add all users
								TaskInfo taskInfo = assignActivity
										.getTaskInfo();

								if (selected != null) {
									taskInfo.setDescription(selected
											.getDescription());
									taskInfo.setActivityId(selected.getId());
									String taskName = "Program-"
											+ selected.getId();
									String approvalTaskName = taskName+"-Approval";
									taskInfo.setTaskName(taskName);
									taskInfo.setApprovalTaskName(approvalTaskName);

									taskInfo.setDescription(selected
											.getDescription());
									assignTask(taskInfo);
								}
								hide();

							}
						}, "Done", "Cancel");

			}
		});
		//
		getView().getAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new CreateProgramEvent(null));
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
				showEditPopup(
						selected != null ? selected.getType() : detail
								.getType(), true);
			}
		});

	}

	protected void showEditPopup(ProgramDetailType type) {
		showEditPopup(type, false);
	}

	protected void showEditPopup(ProgramDetailType type, boolean edit) {
		IsProgramActivity activity = (selected != null) ? selected : detail;

		showEditPopup(type, activity, edit);
	}

	/**
	 * This could be a create or edit call;</br> If its an edit; we may be
	 * editing a selected element or a detail element <br/>
	 * 
	 * @param type
	 * @param activity
	 *            An activity selected on the grid or a detail
	 *            activity(#home;page=activities;activity=50d52)
	 * @param edit
	 */
	protected void showEditPopup(ProgramDetailType type,
			IsProgramActivity activity, boolean edit) {

		switch (type) {
		case TASK:
			// Use
			createTask.setType(type);
			if (edit) {
				// we are editing the selected item
				createTask.setActivity(activity);
				createTask.load(activity.getParentId());
			} else {
				// selected item is the parent - We are creating a new activity
				// based on selected item
				createTask.setActivity(null);
				createTask.load(activity.getId());
			}

			AppManager.showPopUp(edit ? "Edit Task" : "Create Task",
					createTask.getWidget(), new OptionControl() {
						@Override
						public void onSelect(String name) {

							if (name.equals("Save")) {
								if (createTask.getView().isValid()) {
									IsProgramActivity activity = createTask
											.getActivity();
									// System.err.println("")
									save(activity);
									hide();
								}
							} else {
								hide();
							}

						}
					}, "Save", "Cancel");
			break;
		case ACTIVITY:
			if (edit) {
				// we are editing the selected item
				createActivity.setActivity(activity);
				createActivity.load(activity.getParentId());
			} else {
				// selected item is the parent - We are creating a new activity
				// based on selected item
				// User selected an outcome & is now creating a new Activity
				createActivity.setActivity(null);
				createActivity.load(activity.getId());
			}

			AppManager.showPopUp(edit ? "Edit Activity" : "Create Activity",
					createActivity.getWidget(), new OptionControl() {
						@Override
						public void onSelect(String name) {

							if (name.equals("Save")) {
								if (createActivity.getView().isValid()) {
									IsProgramActivity activity = createActivity
											.getActivity();
									// System.err.println("")
									save(activity);
									hide();
								}
							} else {
								hide();
							}

						}
					}, "Save", "Cancel");
			break;

		case OBJECTIVE:
			objectivePresenter
					.setObjective((edit && activity.getType() == ProgramDetailType.OBJECTIVE) ? activity
							: null);
			Long parentId = edit ? activity.getParentId() : programId;

			// in case you selected an Program from summary view and clicked New
			// Objective
			parentId = (parentId != null) ? parentId
					: activity != null ? activity.getId() : null;
			objectivePresenter.load(parentId);// Parent Id Passed here

			AppManager.showPopUp(edit ? "Edit Objective" : "Add Objective",
					objectivePresenter.getWidget(), new OptionControl() {

						@Override
						public void onSelect(String name) {
							if (name.equals("Save")) {
								if (objectivePresenter.getView().isValid()) {
									IsProgramActivity objective = objectivePresenter
											.getObjective();
									objective.setParentId(programId);
									save(objective);
									hide();
								}
							} else {
								hide();
							}
						}

					}, "Save", "Cancel");

			break;

		case OUTCOME:
			createOutcome.setOutcome(edit ? activity : null);
			createOutcome.load(edit ? activity.getParentId() : programId);
			AppManager.showPopUp(edit ? "Edit Outcome" : "Create Outcome",
					createOutcome.getWidget(), new OptionControl() {

						@Override
						public void onSelect(String name) {

							if (name.equals("Save")) {
								if (createOutcome.getView().isValid()) {
									IsProgramActivity outcome = createOutcome
											.getOutcome();
									outcome.setParentId(programId);
									save(outcome);
									hide();
								}
							} else {
								hide();
							}

						}
					}, "Save", "Cancel");
			break;
		case PROGRAM:
			if (activity != null && edit) {
				AppContext.fireEvent(new CreateProgramEvent(activity.getId(),
						!edit));
			}
			break;
		default:
			break;
		}
	}

	private void save(final IsProgramActivity activity) {
		// program.setParentId(programId);
		requestHelper.execute(new CreateProgramRequest(activity),
				new TaskServiceCallback<CreateProgramResponse>() {
					@Override
					public void processResult(CreateProgramResponse aResponse) {
						getView().setLastUpdatedId(
								aResponse.getProgram().getId());
						loadData(programId);
						fireEvent(new ProcessingCompletedEvent());
						
						fireEvent(new ActivitySavedEvent(activity.getType()
								.name().toLowerCase()
								+ " successfully saved"));
					}
				});
	}

	public void loadData(final Long activityId) {
		loadData(activityId, programDetailId);
	}

	public void loadData(final Long programId, Long detailId) {
		fireEvent(new ProcessingEvent());
		this.programId = (programId == null || programId == 0L) ? null
				: programId;
		programDetailId = detailId == null ? null : detailId == 0 ? null
				: detailId;

		getView().setProgramId(this.programId);

		MultiRequestAction action = new MultiRequestAction();
		// List of Programs for tabs
		action.addRequest(new GetProgramsRequest(ProgramDetailType.PROGRAM,
				false));
		action.addRequest(new GetPeriodsRequest());
		action.addRequest(new GetFundsRequest());

		if (this.programId != null) {
			// Details of selected program
			this.programId = programId;
			action.addRequest(new GetProgramsRequest(programId,
					programDetailId == null));
		}

		if (programDetailId != null) {
			action.addRequest(new GetProgramsRequest(programDetailId, true));
		}

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						int i = 0;
						// Programs
						GetProgramsResponse response = (GetProgramsResponse) aResponse
								.get(i++);
						getView().setPrograms(response.getPrograms());

						// Periods
						GetPeriodsResponse getPeriod = (GetPeriodsResponse) aResponse
								.get(i++);
						getView().setPeriods(getPeriod.getPeriods());

						GetFundsResponse getFundsReq = (GetFundsResponse) aResponse
								.get(i++);
						getView().setFunds(getFundsReq.getFunds());

						// activities under a program
						// && programDetailId==null
						if (ActivitiesPresenter.this.programId != null) {
							GetProgramsResponse response2 = (GetProgramsResponse) aResponse
									.get(i++);
							setActivity(response2.getSingleResult());
						} else {
							getView().setActivities(response.getPrograms());
						}

						if (programDetailId != null) {
							GetProgramsResponse response2 = (GetProgramsResponse) aResponse
									.get(i++);
							setActivity(response2.getSingleResult());
						}

						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	protected void setActivity(IsProgramActivity activity) {
		getView().setActivity(activity);

		if (activity.getType() != ProgramDetailType.PROGRAM
				|| programId == null) {
			// this is a detail activity
			this.detail = activity;
		}
		programType = activity.getType();
	}

	protected void loadProgram(Long id) {
		this.programId = id;
		GetProgramsRequest request = new GetProgramsRequest(id, true);

		requestHelper.execute(request,
				new TaskServiceCallback<GetProgramsResponse>() {
					@Override
					public void processResult(GetProgramsResponse aResponse) {
						GetProgramsResponse response = (GetProgramsResponse) aResponse;
						setActivity(response.getSingleResult());
					}
				});

	}

	@Override
	public void onActivitySelectionChanged(ActivitySelectionChangedEvent event) {
		if (event.isSelected()) {
			this.selected = event.getProgramActivity();
			getView().setSelection(event.getProgramActivity().getType(),true);
		} else {
			this.selected = null;
			if (programId == null || programId == 0) {
				// summary view
				getView().setSelection(null);
			} else {
				getView().setSelection(programType, false);
			}

		}
	}

	@Override
	public void onActivitiesReload(ActivitiesReloadEvent event) {
		loadData(programId, programDetailId);
	}

	private void assignTask(final TaskInfo taskInfo) {
		requestHelper.execute(new AssignTaskRequest(taskInfo),
				new TaskServiceCallback<AssignTaskResponse>() {
					@Override
					public void processResult(AssignTaskResponse aResponse) {
						fireEvent(new LoadAlertsEvent());

						List<OrgEntity> assigned = taskInfo
								.getParticipants(ParticipantType.ASSIGNEE);
						String allocatedPeople = "";

						for (OrgEntity entity : assigned) {
							allocatedPeople = allocatedPeople+entity.getDisplayName() + ",";
						}

						fireEvent(new ActivitySavedEvent(
								"You successfully assigned "+taskInfo.getDescription()+" "+ allocatedPeople));
					}
				});
	}

}
