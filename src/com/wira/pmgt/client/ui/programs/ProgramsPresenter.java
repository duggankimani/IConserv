package com.wira.pmgt.client.ui.programs;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.wira.pmgt.client.place.NameTokens;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.OnOptionSelected;
import com.wira.pmgt.client.ui.OptionControl;
import com.wira.pmgt.client.ui.assign.AssignActivityPresenter;
import com.wira.pmgt.client.ui.component.Dropdown;
import com.wira.pmgt.client.ui.detailedActivity.CreateActivityPresenter;
import com.wira.pmgt.client.ui.document.activityview.ActivityDetailPresenter;
import com.wira.pmgt.client.ui.events.ActivitySavedEvent;
import com.wira.pmgt.client.ui.events.ActivitySelectionChangedEvent;
import com.wira.pmgt.client.ui.events.ActivitySelectionChangedEvent.ActivitySelectionChangedHandler;
import com.wira.pmgt.client.ui.events.AppResizeEvent;
import com.wira.pmgt.client.ui.events.AppResizeEvent.ResizeHandler;
import com.wira.pmgt.client.ui.events.CreateProgramEvent;
import com.wira.pmgt.client.ui.events.LoadAlertsEvent;
import com.wira.pmgt.client.ui.events.MoveProgramEvent;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.events.ProgramDeletedEvent;
import com.wira.pmgt.client.ui.events.ProgramDetailSavedEvent;
import com.wira.pmgt.client.ui.events.ProgramsReloadEvent;
import com.wira.pmgt.client.ui.events.ProgramsReloadEvent.ProgramsReloadHandler;
import com.wira.pmgt.client.ui.filter.FilterPresenter;
import com.wira.pmgt.client.ui.objective.CreateObjectivePresenter;
import com.wira.pmgt.client.ui.outcome.CreateOutcomePresenter;
import com.wira.pmgt.client.ui.programs.tree.TreeWidget;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.PermissionType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramTreeModel;
import com.wira.pmgt.shared.requests.AssignTaskRequest;
import com.wira.pmgt.shared.requests.CreateProgramRequest;
import com.wira.pmgt.shared.requests.DeleteProgramRequest;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.shared.requests.GetPermissionsRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.GetProgramsTreeRequest;
import com.wira.pmgt.shared.requests.MoveItemRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.AssignTaskResponse;
import com.wira.pmgt.shared.responses.CreateProgramResponse;
import com.wira.pmgt.shared.responses.DeleteProgramResponse;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.wira.pmgt.shared.responses.GetPeriodsResponse;
import com.wira.pmgt.shared.responses.GetPermissionsResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.GetProgramsTreeResponse;
import com.wira.pmgt.shared.responses.MoveItemResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class ProgramsPresenter extends
		PresenterWidget<ProgramsPresenter.IActivitiesView> implements
		ActivitySelectionChangedHandler, ProgramsReloadHandler, ResizeHandler {

	@ContentSlot
	public static final Type<RevealContentHandler<?>> FILTER_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	FilterPresenter filterPresenter;

	public interface IActivitiesView extends View {

		HasClickHandlers getNewOutcome();

		HasClickHandlers getNewActivityLink();

		HasClickHandlers getNewObjectiveLink();

		HasClickHandlers getNewTaskLink();

		HasClickHandlers getEditLink();

		HasClickHandlers getDeleteButton();

		void setData(List<IsProgramDetail> programs);

		void createProgramTabs(List<IsProgramDetail> programs);

		void setActivity(IsProgramDetail singleResult);

		void setSelection(ProgramDetailType type);

		void setPeriods(List<PeriodDTO> periods);

		// void setSummaryView(boolean hasProgramId);

		void setFunds(List<FundDTO> funds);

		void setLastUpdatedId(Long id);

		void setProgramId(Long programId);

		HasClickHandlers getAddProgramButton();

		HasClickHandlers getaAssign();

		HasClickHandlers getDetailButton();

		Dropdown<PeriodDTO> getPeriodDropDown();

		void setActivePeriod(Long period);

		//void selectTab(Long l);

		void selectTab(String url);

		void setMiddleHeight();

		void removeTab(Long id);

		void setGoalsTable(boolean isGoalsTable);

		void createDefaultTabs();

		void setDownloadUrl(Long programid, Long outcomeid, Long activityId,Long periodId,
				String programType);

		HasClickHandlers getaMove();

		void setPermissions(HashMap<Long, PermissionType> permissions);

		void setSelection(ProgramDetailType type, boolean isRowData, boolean canEdit);

		boolean canEdit(IsProgramDetail programActivity);

		void showBudgets(boolean status);
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

	@Inject
	PlaceManager placeManager;

	Long programId;
	String programCode;

	Long programDetailId; // Drill Down
	String programDetailCode; // Drill Down

	ProgramDetailType programType = ProgramDetailType.PROGRAM; // last selected

	Long periodId;

	IsProgramDetail selected;

	IsProgramDetail detail;

	//ProgramId, PermissionType Map
	private HashMap<Long, PermissionType> permissions = new HashMap<Long, PermissionType>();

	@Inject
	public ProgramsPresenter(final EventBus eventBus, final IActivitiesView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ActivitySelectionChangedEvent.TYPE, this);
		addRegisteredHandler(ProgramsReloadEvent.TYPE, this);
		addRegisteredHandler(AppResizeEvent.TYPE, this);

		getView().getPeriodDropDown().addValueChangeHandler(
				new ValueChangeHandler<PeriodDTO>() {

					@Override
					public void onValueChange(ValueChangeEvent<PeriodDTO> event) {
						PeriodDTO period = event.getValue();
						// period changed - reload all
						ProgramsPresenter.this.periodId = period.getId();
						periodChanged();
					}
				});

		getView().getDetailButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(new PlaceRequest(NameTokens.home)
						.with("page", "detailed").with("activityid",
								selected.getId() + ""));
			}
		});

		getView().getaAssign().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selected != null) {
					assignActivity.load(selected.getId(), selected.getType());
				}
				AppManager.showPopUp("Assign Activity",
						assignActivity.getWidget(), new OptionControl() {
							@Override
							public void onSelect(String name) {

								if (name.equals("Cancel")) {
									hide();
									return;
								}
								fireEvent(new ProcessingEvent());
								assignActivity.addItems(); // Add all users
								TaskInfo taskInfo = assignActivity
										.getTaskInfo();

								if (selected != null) {
									taskInfo.setDescription(selected
											.getDescription());
									taskInfo.setActivityId(selected.getId());
									String taskName = "Program-"
											+ selected.getId();
									String approvalTaskName = taskName
											+ "-Approval";
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
		
		
		getView().getaMove().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				requestHelper.execute(new GetProgramsTreeRequest(programId,null),
						new TaskServiceCallback<GetProgramsTreeResponse>() {
				
					@Override
					public void processResult(
							GetProgramsTreeResponse aResponse) {
						List<ProgramTreeModel> rootModels= aResponse.getModels();
						final TreeWidget tree = new TreeWidget(selected.getType(),rootModels);
						if(!rootModels.isEmpty())
						AppManager.showPopUp("Move '"+selected.getName()+"'",tree,new OptionControl(){
							@Override
							public void onSelect(String name) {
								if (name.equals("Done")) {
									moveProgramDetail(selected.getId(), tree.getSelectedTargetModel());
								}
								hide();
							}

						},"Done","Cancel");
					}
				});
			}
		});
		
		//Add Button
		getView().getAddProgramButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new CreateProgramEvent(null));
			}

		});

		getView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final IsProgramDetail program = (selected != null) ? selected
						: detail;
				AppManager.showPopUp("Delete "
						+ program.getType().getDisplayName(),
						"Do you want to delete "
								+ program.getType().getDisplayName() + " '"
								+ program.getName() + "'",
						new OnOptionSelected() {

							@Override
							public void onSelect(String name) {
								if (name.equals("Yes")) {
									delete(program.getId());
								}
							}
						}, "Cancel", "Yes");
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
		IsProgramDetail activity = (selected != null) ? selected : detail;

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
			IsProgramDetail activity, boolean edit) {

		switch (type) {
		case TASK:
			// Use
			createTask.setType(type);
			if (edit) {
				// we are editing the selected item
				createTask.setActivity(activity);
				createTask.load(activity.getParentId(), null,periodId);
			} else {
				// selected item is the parent - We are creating a new activity
				// based on selected item
				createTask.setActivity(null);
				createTask.load(activity.getId(), null,periodId);
			}

			AppManager.showPopUp(edit ? "Edit Task" : "Create Task",
					createTask.getWidget(), new OptionControl() {
						@Override
						public void onSelect(String name) {

							if (name.equals("Save")) {
								if (createTask.getView().isValid()) {
									IsProgramDetail activity = createTask
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
				createActivity.load(activity.getParentId(),
						activity.getActivityOutcomeId(),periodId);
			} else {
				// selected item is the parent - We are creating a new activity
				// based on selected item
				// User selected an outcome & is now creating a new Activity
				createActivity.setActivity(null);
				if (detail != null) {
					/*Drilling down to an outcome results in the outcome being set as the parent of 
					the new activity; which results in incorrect data storage. This is why we have to
					use programId at this point.
					*/ 
					createActivity.load(programId, selected.getId(),periodId);
				}

			}

			AppManager.showPopUp(edit ? "Edit Activity" : "Create Activity",
					createActivity.getWidget(), new OptionControl() {
						@Override
						public void onSelect(String name) {

							if (name.equals("Save")) {
								if (createActivity.getView().isValid()) {
									IsProgramDetail activity = createActivity
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
			objectivePresenter.setObjective((edit) ? activity : null);
			AppManager.showPopUp(edit ? "Edit Objective" : "Add Objective",
					objectivePresenter.getWidget(), new OptionControl() {

						@Override
						public void onSelect(String name) {
							if (name.equals("Save")) {
								if (objectivePresenter.getView().isValid()) {
									IsProgramDetail objective = objectivePresenter
											.getObjective();
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
			createOutcome
					.load(edit ? activity.getParentId() : activity.getId());
			AppManager.showPopUp(edit ? "Edit Outcome" : "Create Outcome",
					createOutcome.getWidget(), new OptionControl() {

						@Override
						public void onSelect(String name) {

							if (name.equals("Save")) {
								if (createOutcome.getView().isValid()) {
									IsProgramDetail outcome = createOutcome
											.getOutcome();
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

	private void save(final IsProgramDetail activity) {
		// program.setParentId(programId);
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new CreateProgramRequest(activity));

		requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						CreateProgramResponse createProgramsResponse = (CreateProgramResponse) aResponse
								.get(0);

						getView().setLastUpdatedId(
								createProgramsResponse.getProgram().getId());

						if (activity.getType() == ProgramDetailType.PROGRAM) {
							loadData(programId);
						} else if (activity.getType() == ProgramDetailType.OBJECTIVE) {
							loadData(null, null, null,
									ProgramDetailType.OBJECTIVE);
						} else if (activity.getType() == ProgramDetailType.ACTIVITY) {
							assert activity.getActivityOutcomeId() != null;
							afterSave(createProgramsResponse.getProgram()
									.getId(), activity.getActivityOutcomeId(),
									activity.getId() == null);
						} else {
							afterSave(createProgramsResponse.getProgram()
									.getId(), activity.getParentId(), activity
									.getId() == null);
						}

						fireEvent(new ProcessingCompletedEvent());
						fireEvent(new ActivitySavedEvent(activity.getType()
								.name().toLowerCase()
								+ " successfully saved"));

					}
				});

	}

	public void delete(Long programId) {
		requestHelper.execute(new DeleteProgramRequest(programId),
				new TaskServiceCallback<DeleteProgramResponse>() {
					@Override
					public void processResult(DeleteProgramResponse aResponse) {
						// refresh
						IsProgramDetail program = (selected != null) ? selected
								: detail;
						afterDelete(program.getId(), program.getParentId());

						if (program.getType() == ProgramDetailType.PROGRAM) {
							// remove Program Tab
							getView().removeTab(program.getId());
						}

						if (selected != null) {
							ProgramsPresenter.this.selected = null;
							getView().setSelection(
									detail == null ? null : detail.getType());
						} else if (detail != null) {
							ProgramsPresenter.this.detail = null;
							getView().setSelection(null);
						}

					}
				});
	}

	protected void afterDelete(Long id, final Long parentId) {

		// reload parent
		if (parentId != null) {
			MultiRequestAction requests = new MultiRequestAction();
			requests.addRequest(new GetProgramsRequest(parentId, false));
			requestHelper.execute(requests,
					new TaskServiceCallback<MultiRequestActionResult>() {
						@Override
						public void processResult(
								MultiRequestActionResult aResponse) {
							IsProgramDetail parent = ((GetProgramsResponse) (aResponse
									.get(0))).getSingleResult();
							fireEvent(new ProgramDetailSavedEvent(parent,
									false, false));
						}
					});
		}

		fireEvent(new ProgramDeletedEvent(id));

	}

	protected void afterSave(Long saveActivityId, final Long parentId,
			final boolean isNew) {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetProgramsRequest(saveActivityId, false));
		// reload parent
		if (parentId != null) {
			requests.addRequest(new GetProgramsRequest(parentId, false));
		}

		requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {

						IsProgramDetail activity = ((GetProgramsResponse) (aResponse
								.get(0))).getSingleResult();
						fireEvent(new ProgramDetailSavedEvent(activity, isNew,
								true));

						if (parentId != null) {
							IsProgramDetail parent = ((GetProgramsResponse) (aResponse
									.get(1))).getSingleResult();
							fireEvent(new ProgramDetailSavedEvent(parent,
									false, false));
						}
					}
				});
	}

	public void loadData(final Long activityId) {
		loadData(activityId, programDetailId);
	}

	public void loadData(final Long programId, Long detailId) {
		loadData(programId, detailId, null);
	}

	public void loadObjectives() {
		loadData(null, null, null, ProgramDetailType.OBJECTIVE);
	}

	public void loadData(Long programId, Long detailId, Long periodId) {
		loadData(programId, detailId, periodId, ProgramDetailType.PROGRAM);
	}

	public void loadActivitiesByOutcome(Long programId, Long outcomeId, Long periodId) {
		loadData(programId, outcomeId, periodId, true, ProgramDetailType.PROGRAM);
	}

	/**
	 * If PeriodId is null; current period is selected
	 * 
	 * @param programId
	 * @param detailId
	 * @param periodId
	 */
	public void loadData(Long programId, Long detailId, Long periodId,
			final ProgramDetailType typeToLoad) {
		loadData(programId, detailId, periodId, false, typeToLoad);
	}

	public void loadData(Long parentProgramId, Long detailId, final Long periodId,
			boolean searchByOutcome, final ProgramDetailType typeToLoad) {
		fireEvent(new ProcessingEvent());
		this.periodId = periodId;
		this.programType = typeToLoad;
		
		//DRILL DOWN BY OUTCOME
		//DRILL DOWN BY ACTIVITY
		//DRILL DOWN BY TASK
		this.programId = (parentProgramId == null || parentProgramId == 0L) ? null
				: parentProgramId;
		
		this.programDetailId = detailId == null ? null : detailId == 0 ? null
				: detailId;

		getView().setDownloadUrl(programId, 
				searchByOutcome?programDetailId:null, 
						searchByOutcome?null:programDetailId, 
								periodId,
								typeToLoad.name());
		
		MultiRequestAction action = new MultiRequestAction();

		// Get Periods
		action.addRequest(new GetPeriodsRequest());

		
		if (typeToLoad.equals(ProgramDetailType.OBJECTIVE)) {
			getView().setGoalsTable(true);
			GetProgramsRequest request = new GetProgramsRequest(
					ProgramDetailType.OBJECTIVE, true);
			action.addRequest(request);
		} else {
			getView().setProgramId(this.programId);
		}
		
		//Load Program Permissions
		action.addRequest(new GetPermissionsRequest(AppContext.getUserId(), periodId));

		
		// List of Programs for tabs
		{
			// Within a given period
			GetProgramsRequest request = new GetProgramsRequest(
					ProgramDetailType.PROGRAM, false);
			request.setPeriodId(periodId);
			action.addRequest(request);
		}


		// Get Funding Sources
		action.addRequest(new GetFundsRequest());

		
		//PARENT PROGRAM - FOR INSTANCES WHERE WE ARE DRILLING DOWN A PROGRAM
		if(typeToLoad!=ProgramDetailType.OBJECTIVE){
			if (this.programId != null) {
				// Details of selected program
				// this.programId = programId;
	
				if (periodId != null && programCode!=null) {
					// Period is not current period
					action.addRequest(new GetProgramsRequest(programCode, periodId,
							programDetailId == null));
				} else {
					action.addRequest(new GetProgramsRequest(this.programId,
							programDetailId == null));
				}
	
			}
	
			//DetailId - FOR INSTANCES WHERE WE ARE DRILLING DOWN AN OUTCOME/ ACTIVITY OR TASK
			if (programDetailId != null) {
				if (searchByOutcome) {
					//DRILLING DOWN AN OUTCOME
					if (periodId != null && programCode!=null) {
						assert programDetailCode != null;
						action.addRequest(new GetProgramsRequest(programDetailCode,
								periodId, this.programId != null));
					} else {
						action.addRequest(new GetProgramsRequest(this.programId,
								programDetailId, this.programId != null));
					}
				} else {
					//DRILLING DOWN AN ACTIVITY/ OR TASK
					if (periodId != null && programCode!=null) {
						assert programDetailCode != null;
						action.addRequest(new GetProgramsRequest(programDetailCode,
								periodId, this.programId != null));
					} else {
						action.addRequest(new GetProgramsRequest(programDetailId,
								this.programId != null));
					}
				}
	
			}
		}

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						
						int i = 0;
						// Periods
						GetPeriodsResponse getPeriod = (GetPeriodsResponse) aResponse
								.get(i++);
						getView().setPeriods(getPeriod.getPeriods());
						getView().setActivePeriod(periodId);
						
						
						if (typeToLoad.equals(ProgramDetailType.OBJECTIVE)) {
							GetProgramsResponse response = (GetProgramsResponse) aResponse
									.get(i++);
							getView().showBudgets(false);
							getView().setData(response.getPrograms());
						}
						
						//Get Program Permissions
						GetPermissionsResponse permissionsResp = (GetPermissionsResponse)aResponse.get(i++);
						setPermissions(permissionsResp.getPermissions());

						// Programs (Presented as tabs below)
						GetProgramsResponse response = (GetProgramsResponse) aResponse
								.get(i++);
						// System.err.println("Tabs >> "+response.getPrograms().size());
						getView().createProgramTabs(response.getPrograms());
						

						// Funds
						GetFundsResponse getFundsReq = (GetFundsResponse) aResponse
								.get(i++);
						getView().setFunds(getFundsReq.getFunds());

						// activities under a program
						// && programDetailId==null
						IsProgramDetail parentProgram = null;
						if (ProgramsPresenter.this.programId != null) {
							GetProgramsResponse response2 = (GetProgramsResponse) aResponse
									.get(i++);
							if (response2.getSingleResult() != null) {
								// The program detail might not be available for
								// the specified period
								programCode = response2.getSingleResult()
										.getCode();
								
								setActivity(parentProgram = response2.getSingleResult());
							}

						} else {

							if (programDetailId == null
									&& typeToLoad == ProgramDetailType.PROGRAM) {
								// This is a summary table with no program
								
								System.err.println("Summary Table");
								getView().showBudgets(true);
								getView().setData(response.getPrograms());
							}

							getView()
									.selectTab(
											typeToLoad == ProgramDetailType.OBJECTIVE ? "#home;page=objectives"
													: "#home;page=activities;activity=" + 0+
													(periodId==null? "":";period="+periodId));
							getView()
									.setSelection(
											typeToLoad == ProgramDetailType.OBJECTIVE ? ProgramDetailType.OBJECTIVE
													: null, false, true);
						}

						if (programDetailId != null) {
							GetProgramsResponse response2 = (GetProgramsResponse) aResponse
									.get(i++);

							if (response2.getSingleResult() != null) {
								// The program detail might not be available for
								// the specified period
								programDetailCode = response2.getSingleResult()
										.getCode();
								programDetailId = response2.getSingleResult()
										.getId();
								if(parentProgram!=null){
									IsProgramDetail child = response2.getSingleResult();
									child.setPeriod(parentProgram.getPeriod());
									setActivity(child);
								}
								
							}

						}

						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	protected void setPermissions(HashMap<Long, PermissionType> permissions) {
		this.permissions = permissions;
		
		getView().setPermissions(permissions);
		//Window.alert("Permissions = "+permissions);
	}

	protected void setActivity(IsProgramDetail activity) {
		getView().setActivity(activity);

		// if (activity.getType() != ProgramDetailType.PROGRAM
		// || programId == null) {
		// // this is a detail activity
		// this.detail = activity;
		// }
		this.detail = activity;
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
			getView().setSelection(event.getProgramActivity().getType(), true,getView().canEdit(event.getProgramActivity()));
		} else {
			this.selected = null;
			if (programId == null || programId == 0) {
				// summary view
				getView().setSelection(null);
			} else {
				getView().setSelection(programType, false,false);
			}

		}
	}

	@Override
	public void onProgramsReload(ProgramsReloadEvent event) {
		loadData(programId, programDetailId,event.getPeriodId());
	}

	private void assignTask(final TaskInfo taskInfo) {
		requestHelper.execute(new AssignTaskRequest(taskInfo),
				new TaskServiceCallback<AssignTaskResponse>() {
					@Override
					public void processResult(AssignTaskResponse aResponse) {
						fireEvent(new ProcessingCompletedEvent());
						fireEvent(new LoadAlertsEvent());

						List<OrgEntity> assigned = taskInfo
								.getParticipants(PermissionType.CAN_EXECUTE);
						String allocatedPeople = "";

						for (OrgEntity entity : assigned) {
							allocatedPeople = allocatedPeople
									+ entity.getDisplayName() + ",";
						}

						afterSave(selected.getId(), selected.getParentId(),
								false);
					
						fireEvent(new ActivitySavedEvent(
								"Assignment was successfully saved "));

					}
				});
	}

	/**
	 * On period change <br>
	 * Reload the currently selected program with details for the selected year <br>
	 * Programs from different years are related through a shared program code
	 */
	protected void periodChanged() {
		loadData(programId, programDetailId, periodId, programType);
	}

	/*
	 * on Window Resize - Position the middle section to occupy full width
	 */
	@Override
	public void onResize(AppResizeEvent event) {
		getView().setMiddleHeight();
	}

	@Override
	protected void onReset() {
		super.onReset();
		// System.err.println(">>>>On reset called");

		assert (filterPresenter != null);
		setInSlot(FILTER_SLOT, filterPresenter);
		getView().setMiddleHeight();
	}

	public void clear() {
		this.selected = null;
		this.detail = null;
		this.programId = null;
		this.programCode = null;

		this.programDetailId = null; // Drill Down
		this.programDetailCode = null;
	}

	private void moveProgramDetail(Long activityToMove,
			final ProgramTreeModel selectedTargetModel) {
		if(selectedTargetModel==null){
			return;
		}
		
		if(activityToMove.equals(selectedTargetModel.getId())){
			return;
		}
		
		Long parentId=null;
		Long outcomeId=null;
		if(selectedTargetModel.getType()==ProgramDetailType.OUTCOME){
			outcomeId = selectedTargetModel.getId();
		}else{
			parentId = selectedTargetModel.getId();
		}
		
		requestHelper.execute(new MoveItemRequest(activityToMove, parentId, outcomeId), 
				new TaskServiceCallback<MoveItemResponse>() {
			@Override
			public void processResult(MoveItemResponse aResponse) {
				//fire Move Event
				Long previousParentId;
				Long newParentId;
				if(selectedTargetModel.getType()==ProgramDetailType.OUTCOME){
					previousParentId = selected.getActivityOutcomeId();
					newParentId = selectedTargetModel.getId();
					selected.setActivityOutcomeId(newParentId);
					fireEvent(new MoveProgramEvent(selected, previousParentId, newParentId));
				}else{
					previousParentId = selected.getParentId();
					newParentId = selectedTargetModel.getId();
					selected.setParentId(newParentId);
					fireEvent(new MoveProgramEvent(selected, previousParentId, newParentId));
				}
				
			}
		});
	}
	
	
}
