package com.wira.pmgt.client.ui.programs;

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
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.events.ProgramDeletedEvent;
import com.wira.pmgt.client.ui.events.ProgramDetailSavedEvent;
import com.wira.pmgt.client.ui.events.ProgramsReloadEvent;
import com.wira.pmgt.client.ui.events.ProgramsReloadEvent.ProgramsReloadHandler;
import com.wira.pmgt.client.ui.filter.FilterPresenter;
import com.wira.pmgt.client.ui.objective.CreateObjectivePresenter;
import com.wira.pmgt.client.ui.outcome.CreateOutcomePresenter;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.ParticipantType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.requests.AssignTaskRequest;
import com.wira.pmgt.shared.requests.CreateProgramRequest;
import com.wira.pmgt.shared.requests.DeleteProgramRequest;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.AssignTaskResponse;
import com.wira.pmgt.shared.responses.CreateProgramResponse;
import com.wira.pmgt.shared.responses.DeleteProgramResponse;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.wira.pmgt.shared.responses.GetPeriodsResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class ProgramsPresenter extends
		PresenterWidget<ProgramsPresenter.IActivitiesView> implements
		ActivitySelectionChangedHandler, ProgramsReloadHandler, ResizeHandler {
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> FILTER_SLOT = new Type<RevealContentHandler<?>>();
	
	@Inject FilterPresenter filterPresenter;
	
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

		void setSelection(ProgramDetailType programType, boolean isRowData);

		HasClickHandlers getAddButton();

		HasClickHandlers getaAssign();

		HasClickHandlers getDetailButton();

		Dropdown<PeriodDTO> getPeriodDropDown();

		void setActivePeriod(PeriodDTO period);

		void selectTab(Long l);

		void setMiddleHeight();

		void removeTab(Long id);
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
	String programCode;
	
	Long programDetailId; // Drill Down
	String programDetailCode; //Drill Down

	ProgramDetailType programType = ProgramDetailType.PROGRAM; // last selected

	PeriodDTO period;
	
	IsProgramDetail selected;
	
	IsProgramDetail detail;

	@Inject
	public ProgramsPresenter(final EventBus eventBus,
			final IActivitiesView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ActivitySelectionChangedEvent.TYPE, this);
		addRegisteredHandler(ProgramsReloadEvent.TYPE, this);
		addRegisteredHandler(AppResizeEvent.TYPE, this);
		
		
		getView().getPeriodDropDown().addValueChangeHandler(new ValueChangeHandler<PeriodDTO>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PeriodDTO> event) {
				PeriodDTO period =event.getValue();
				//period changed - reload all
				ProgramsPresenter.this.period = period;
				periodChanged();
			}
		});
		
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
					assignActivity.load(selected.getId(),selected.getType());
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
		
		getView().getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final IsProgramDetail program = (selected != null)? selected : detail;
				AppManager.showPopUp("Delete "+program.getType().getDisplayName(), 
						"Do you want to delete "+program.getType().getDisplayName()+" '"+program.getName()+"'",
						new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Yes")){
									delete(program.getId());
								}
							}
						}, "Cancel","Yes");
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
			objectivePresenter
					.setObjective((edit && activity.getType() == ProgramDetailType.OBJECTIVE) ? activity
							: null);
			
			//if creating new, activity is the parent
			final Long parentId = edit ? activity.getParentId() : activity.getId();
			
			objectivePresenter.load(parentId);// Parent Id Passed here

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
			createOutcome.load(edit ? activity.getParentId() : activity.getId());
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
						CreateProgramResponse createProgramsResponse = (CreateProgramResponse) aResponse.get(0);
						
						getView().setLastUpdatedId(
								createProgramsResponse.getProgram().getId());
						if(activity.getType()==ProgramDetailType.PROGRAM){
							loadData(programId);
						}

						//Reloading information in a new request 
						//due to an issue on the server in reloading uncommitted information
						//Budget Total Amounts && Parent program allocations updated values do not reflect
						//unless the user actively reloads them
						//This is a hack to prevent that issue
						afterSave(createProgramsResponse.getProgram().getId(),activity.getParentId(),activity.getId()==null);						
						fireEvent(new ProcessingCompletedEvent());
						fireEvent(new ActivitySavedEvent(activity.getType()
								.name().toLowerCase()
								+ " successfully saved"));
						
					}
				});
				
	}
	
	public void delete(Long programId){
		requestHelper.execute(new DeleteProgramRequest(programId), new TaskServiceCallback<DeleteProgramResponse>() {
			@Override
			public void processResult(DeleteProgramResponse aResponse) {
				//refresh
				IsProgramDetail program = (selected != null)? selected : detail;
				afterDelete(program.getId(), program.getParentId());
				
				if(program.getType()==ProgramDetailType.PROGRAM){
					//remove Program Tab
					getView().removeTab(program.getId());
				}
				
				if(selected!=null){
					ProgramsPresenter.this.selected = null;
					getView().setSelection(detail==null ? null : detail.getType());
				}else if(detail!=null){
					ProgramsPresenter.this.detail=null;
					getView().setSelection(null);
				}
				
			}
		});
	}

	protected void afterDelete(Long id, final Long parentId) {
		
		//reload parent
		if(parentId!=null){
			MultiRequestAction requests = new MultiRequestAction();
			requests.addRequest(new GetProgramsRequest(parentId, false, false));		
			requestHelper.execute(requests, 
					new TaskServiceCallback<MultiRequestActionResult>() {
				@Override
				public void processResult(MultiRequestActionResult aResponse) {
					IsProgramDetail parent = ((GetProgramsResponse)(aResponse.get(0))).getSingleResult();
					fireEvent(new ProgramDetailSavedEvent(parent,false,false));
				}
			});
		}
		
		fireEvent(new ProgramDeletedEvent(id));
		
	}

	protected void afterSave(Long saveActivityId, final Long parentId, final boolean isNew) {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetProgramsRequest(saveActivityId, false, false));
		//reload parent
		if(parentId!=null){							
			requests.addRequest(new GetProgramsRequest(parentId, false, false));
		}
		
		requestHelper.execute(requests, 
				new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				
				IsProgramDetail activity = ((GetProgramsResponse)(aResponse.get(0))).getSingleResult();
				fireEvent(new ProgramDetailSavedEvent(activity,isNew,true));
				
				if(parentId!=null){
					IsProgramDetail parent = ((GetProgramsResponse)(aResponse.get(1))).getSingleResult();
					fireEvent(new ProgramDetailSavedEvent(parent,false,false));
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
	
	/**
	 * If PeriodId is null; current period is selected
	 * 
	 * @param programId
	 * @param detailId
	 * @param periodId
	 */
	public void loadData(Long programId, Long detailId, Long periodId) {
		fireEvent(new ProcessingEvent());
		System.err.println("programId, detailId, periodId ["+programId+", "+detailId+", "+periodId+"]");
		
		this.programId = (programId == null || programId == 0L) ? null
				: programId;
		programDetailId = detailId == null ? null : detailId == 0 ? null
				: detailId;

		getView().setProgramId(this.programId);

		MultiRequestAction action = new MultiRequestAction();
		// List of Programs for tabs
		if(periodId!=null){
			GetProgramsRequest request = new GetProgramsRequest(ProgramDetailType.PROGRAM,false);
			request.setPeriodId(periodId);
			action.addRequest(request);
		}else{
			action.addRequest(new GetProgramsRequest(ProgramDetailType.PROGRAM,
					false));
		}
		
		action.addRequest(new GetPeriodsRequest());
		action.addRequest(new GetFundsRequest());

		if (this.programId != null) {
			// Details of selected program
			this.programId = programId;
			
			if(periodId!=null){
				//Period is not current period
				action.addRequest(new GetProgramsRequest(programCode,periodId,programDetailId == null));
			}else{
				action.addRequest(new GetProgramsRequest(programId,
						programDetailId == null));
			}
			
		}

		if (programDetailId != null) {
			//Drill Down
			if(this.programId==null){
				//Summary Table Drill down - Load program and objectives only
				if(periodId!=null){
					assert programDetailCode!=null;
					action.addRequest(new GetProgramsRequest(programDetailCode,periodId,false,true));
				}else{
					action.addRequest(new GetProgramsRequest(programDetailId,false,true));
				}
				
			}else{
				//We are loading details of an item under a program
				//Program Table Drill Down - Load program detail/ activity without the objectives
				if(periodId!=null){
					action.addRequest(new GetProgramsRequest(programDetailCode,periodId,true,false));
				}else{
					action.addRequest(new GetProgramsRequest(programDetailId,true,false));
				}
				
			}
			
		}
		
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						int i = 0;
						// Programs (Presented as tabs below)
						GetProgramsResponse response = (GetProgramsResponse) aResponse
								.get(i++);
						getView().createProgramTabs(response.getPrograms());

						// Periods
						GetPeriodsResponse getPeriod = (GetPeriodsResponse) aResponse
								.get(i++);
						getView().setPeriods(getPeriod.getPeriods());

						//Funds
						GetFundsResponse getFundsReq = (GetFundsResponse) aResponse
								.get(i++);
						getView().setFunds(getFundsReq.getFunds());

						// activities under a program
						// && programDetailId==null
						if (ProgramsPresenter.this.programId != null) {
							GetProgramsResponse response2 = (GetProgramsResponse) aResponse
									.get(i++);
							if(response2.getSingleResult()!=null){
								//The program detail might not be available for the specified period
								programCode = response2.getSingleResult().getCode();
								ProgramsPresenter.this.programId = response2.getSingleResult().getId();
								setActivity(response2.getSingleResult());
							}
							
						} else{
					
							if(programDetailId == null){
								//This is a summary table with no program selecte
								getView().setData(response.getPrograms());
							}
							getView().setSelection(null);
							getView().selectTab(0L);
						}

						if (programDetailId != null) {
							GetProgramsResponse response2 = (GetProgramsResponse) aResponse
									.get(i++);
							
							if(response2.getSingleResult()!=null){
								//The program detail might not be available for the specified period
								programDetailCode = response2.getSingleResult().getCode();
								programDetailId = response2.getSingleResult().getId();
								setActivity(response2.getSingleResult());
							}							
							
						}
						
						getView().setActivePeriod(period);
						
						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	protected void setActivity(IsProgramDetail activity) {
		getView().setActivity(activity);

//		if (activity.getType() != ProgramDetailType.PROGRAM
//				|| programId == null) {
//			// this is a detail activity
//			this.detail = activity;
//		}
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
	public void onProgramsReload(ProgramsReloadEvent event) {
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


						afterSave(selected.getId(), selected.getParentId(), false);
						fireEvent(new ActivitySavedEvent(
								"You successfully assigned '"+taskInfo.getDescription()+"' "+ allocatedPeople));
						
					}
				});
	}


	/**
	 * On period change
	 * <br>
	 * Reload the currently selected program with details for the selected year
	 * <br>
	 * Programs from different years are related through a shared program code
	 */
	protected void periodChanged() {
		loadData(programId, programDetailId, period.getId());
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
		//System.err.println(">>>>On reset called");
		
		assert(filterPresenter!=null);
		setInSlot(FILTER_SLOT, filterPresenter);
		getView().setMiddleHeight();
	}
	
}
