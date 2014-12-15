package com.wira.pmgt.client.ui.home;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.wira.pmgt.client.model.TaskType;
import com.wira.pmgt.client.place.NameTokens;
import com.wira.pmgt.client.service.ServiceCallback;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.MainPagePresenter;
import com.wira.pmgt.client.ui.addDoc.DocumentPopupPresenter;
import com.wira.pmgt.client.ui.document.GenericDocumentPresenter;
import com.wira.pmgt.client.ui.events.ActivitiesSelectedEvent;
import com.wira.pmgt.client.ui.events.ActivitiesSelectedEvent.ActivitiesSelectedHandler;
import com.wira.pmgt.client.ui.events.AfterSaveEvent;
import com.wira.pmgt.client.ui.events.AfterSaveEvent.AfterSaveHandler;
import com.wira.pmgt.client.ui.events.AfterSearchEvent;
import com.wira.pmgt.client.ui.events.AlertLoadEvent;
import com.wira.pmgt.client.ui.events.AlertLoadEvent.AlertLoadHandler;
import com.wira.pmgt.client.ui.events.ContextLoadedEvent;
import com.wira.pmgt.client.ui.events.ContextLoadedEvent.ContextLoadedHandler;
import com.wira.pmgt.client.ui.events.CreateProgramEvent;
import com.wira.pmgt.client.ui.events.CreateProgramEvent.CreateProgramHandler;
import com.wira.pmgt.client.ui.events.DocumentSelectionEvent;
import com.wira.pmgt.client.ui.events.DocumentSelectionEvent.DocumentSelectionHandler;
import com.wira.pmgt.client.ui.events.LoadAlertsEvent;
import com.wira.pmgt.client.ui.events.PresentTaskEvent;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.wira.pmgt.client.ui.events.ReloadEvent;
import com.wira.pmgt.client.ui.events.ReloadEvent.ReloadHandler;
import com.wira.pmgt.client.ui.events.SearchEvent;
import com.wira.pmgt.client.ui.events.SearchEvent.SearchHandler;
import com.wira.pmgt.client.ui.filter.FilterPresenter;
import com.wira.pmgt.client.ui.login.LoginGateKeeper;
import com.wira.pmgt.client.ui.newsfeed.NewsFeedPresenter;
import com.wira.pmgt.client.ui.profile.ProfilePresenter;
import com.wira.pmgt.client.ui.program.save.CreateProgramPresenter;
import com.wira.pmgt.client.ui.programs.ProgramsPresenter;
import com.wira.pmgt.client.ui.reports.HomeReportsPresenter;
import com.wira.pmgt.client.ui.save.form.GenericFormPresenter;
import com.wira.pmgt.client.ui.tasklistitem.DateGroupPresenter;
import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.client.ui.util.DocMode;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.Doc;
import com.wira.pmgt.shared.model.DocStatus;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.HTSummary;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.SearchFilter;
import com.wira.pmgt.shared.requests.GetTaskList;
import com.wira.pmgt.shared.responses.GetTaskListResult;

public class HomePresenter extends
		Presenter<HomePresenter.MyView, HomePresenter.MyProxy> implements
		AfterSaveHandler, DocumentSelectionHandler, ReloadHandler,
		AlertLoadHandler, ActivitiesSelectedHandler, ProcessingHandler,
		ProcessingCompletedHandler, SearchHandler, CreateProgramHandler,
		ContextLoadedHandler {

	public interface MyView extends View {
		void showmask(boolean mask);

		void setHasItems(boolean b);

		void setHeading(String string);

		void bindAlerts(HashMap<TaskType, Integer> alerts);

		void setSelectedTab(String page);

		void showUserImg(HTUser currentUser);

		void showActivitiesPanel(boolean show);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.home)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<HomePresenter> {
	}

	public static final Object DATEGROUP_SLOT = new Object();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> DOCPOPUP_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> DOCUMENT_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> FILTER_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> ACTIVITIES_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> ADMIN_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	DispatchAsync dispatcher;
	@Inject
	PlaceManager placeManager;
	@Inject
	DocumentPopupPresenter docPopup;

	private IndirectProvider<CreateProgramPresenter> createDocProvider;
	private IndirectProvider<GenericDocumentPresenter> docViewFactory;
	private IndirectProvider<HomeReportsPresenter> reportFactory;
	private IndirectProvider<DateGroupPresenter> dateGroupFactory;
	private IndirectProvider<NewsFeedPresenter> newsFeedFactory;
	private IndirectProvider<ProgramsPresenter> activitiesFactory;
	private IndirectProvider<ProfilePresenter> profileFactory;

	private TaskType currentTaskType;

	/**
	 * Url processInstanceId (pid) - required incase the use hits refresh
	 */
	private Long processInstanceId = null;

	/**
	 * Url documentId (did) - required incase the use hits refresh
	 */
	private Long documentId = null;

	@Inject
	FilterPresenter filterPresenter;
	Timer timer = new Timer() {

		@Override
		public void run() {
			search();
		}
	};

	@Inject
	public HomePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, Provider<CreateProgramPresenter> docProvider,
			Provider<GenericFormPresenter> formProvider,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<HomeReportsPresenter> reportsProvider,
			Provider<DateGroupPresenter> dateGroupProvider,
			Provider<NewsFeedPresenter> newsfeedProvider,
			Provider<ProfilePresenter> profileProvider,
			Provider<ProgramsPresenter> activitiesProvider) {
		super(eventBus, view, proxy);

		createDocProvider = new StandardProvider<CreateProgramPresenter>(
				docProvider);
		docViewFactory = new StandardProvider<GenericDocumentPresenter>(
				docViewProvider);
		reportFactory = new StandardProvider<HomeReportsPresenter>(
				reportsProvider);
		dateGroupFactory = new StandardProvider<DateGroupPresenter>(
				dateGroupProvider);
		newsFeedFactory = new StandardProvider<NewsFeedPresenter>(
				newsfeedProvider);
		profileFactory = new StandardProvider<ProfilePresenter>(profileProvider);
		activitiesFactory = new StandardProvider<ProgramsPresenter>(
				activitiesProvider);
	}

	protected void search() {
		timer.cancel();
		if (searchTerm.isEmpty()) {
			loadTasks();
			return;
		}

		// fireEvent(new ProcessingEvent());
		SearchFilter filter = new SearchFilter();
		filter.setSubject(searchTerm);
		// filter.setPhrase(searchTerm);
		search(filter);
	}

	public void search(final SearchFilter filter) {

		GetTaskList request = new GetTaskList(AppContext.getUserId(), filter);
		fireEvent(new ProcessingEvent());
		dispatcher.execute(request,
				new TaskServiceCallback<GetTaskListResult>() {
					@Override
					public void processResult(GetTaskListResult result) {

						GetTaskListResult rst = (GetTaskListResult) result;
						List<Doc> tasks = rst.getTasks();
						loadLines(tasks);
						if (tasks.isEmpty())
							getView().setHasItems(false);
						else
							getView().setHasItems(true);

						fireEvent(new AfterSearchEvent(filter.getSubject(),
								filter.getPhrase()));
						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}

	String searchTerm = "";

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(AfterSaveEvent.TYPE, this);
		addRegisteredHandler(DocumentSelectionEvent.TYPE, this);
		addRegisteredHandler(ReloadEvent.TYPE, this);
		addRegisteredHandler(AlertLoadEvent.TYPE, this);
		addRegisteredHandler(ActivitiesSelectedEvent.TYPE, this);
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		addRegisteredHandler(SearchEvent.TYPE, this);
		addRegisteredHandler(CreateProgramEvent.TYPE, this);
		addRegisteredHandler(ContextLoadedEvent.TYPE, this);

	}

	/**
	 * 
	 */
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		fireEvent(new LoadAlertsEvent());
		clear();
		processInstanceId = null;
		documentId = null;

		final String name = request.getParameter("type", null);
		String processInstID = request.getParameter("pid", null);
		String documentSearchID = request.getParameter("did", null);
		String period = request.getParameter("period", null);
		final Long periodId = period==null? null: new Long(period);
		
		if (processInstID != null) {
			processInstanceId = Long.parseLong(processInstID);
		}
		if (documentSearchID != null) {
			documentId = Long.parseLong(documentSearchID);
		}

		final String page = request.getParameter("page", null);

		if (page != null && page.equals("profile")) {
			Window.setTitle("Profile");
			profileFactory.get(new ServiceCallback<ProfilePresenter>() {
				@Override
				public void processResult(ProfilePresenter aResponse) {
					setInSlot(ACTIVITIES_SLOT, aResponse);
					getView().setSelectedTab("Profile");
				}
			});

		} else if (page != null
				&& (page.equals("activities") || page.equals("objectives"))) {
			String program = request.getParameter("activity", "0");
			String detail = "0";
			String outcome = "0";

			if (program.contains("d")) {
				String[] ids = program.split("d");
				program = ids[0];
				detail = ids[1];
			}

			if (program.contains("O")) {
				String[] ids = program.split("O");
				program = ids[0];
				outcome = ids[1];
			}

			final Long programId = new Long(program);
			final Long detailId = new Long(detail);
			final Long outcomeId = new Long(outcome);
			Window.setTitle(page.equals("activities")?"Activities":"Objectives");
			activitiesFactory.get(new ServiceCallback<ProgramsPresenter>() {
				@Override
				public void processResult(ProgramsPresenter aResponse) {
					aResponse.clear();
					if (page.equals("activities")) {

						if (!outcomeId.equals(0L)) {
							aResponse.loadActivitiesByOutcome(programId,
									outcomeId, periodId);
						} else {
							aResponse.loadData(programId, detailId, periodId);
						}

					} else {
						aResponse.loadObjectives();
					}

					setInSlot(ACTIVITIES_SLOT, aResponse);
					getView().setSelectedTab("Activities");
				}
			});

		} else if (page != null && page.equals("detailed")) {
			final String activityId = request.getParameter("activityid", null);
			if (activityId == null) {
				History.back();
			}

			Window.setTitle("Detailed Activity View");
			docViewFactory.get(new ServiceCallback<GenericDocumentPresenter>() {
				@Override
				public void processResult(GenericDocumentPresenter docResponse) {
					// docResponse.loadData();
					docResponse.showDetailedView(new Long(activityId));
					setInSlot(ACTIVITIES_SLOT, docResponse);
					getView().setSelectedTab("Activities");
				}
			});

		} else if (page != null && page.equals("reports")) {
			Window.setTitle("Reports Dashboard");
			reportFactory.get(new ServiceCallback<HomeReportsPresenter>() {
				@Override
				public void processResult(HomeReportsPresenter reportResponse) {
					setInSlot(ACTIVITIES_SLOT, reportResponse);
					getView().setSelectedTab("Reports");
					reportResponse.loadData(periodId);
				}
			});

		} else if (name != null) {
			TaskType type = TaskType.getTaskType(name);
			this.currentTaskType = type;

			// getView().setTaskType(currentTaskType);
			loadTasks(type);
		} else {
			Window.setTitle("Home");
			newsFeedFactory.get(new ServiceCallback<NewsFeedPresenter>() {
				@Override
				public void processResult(NewsFeedPresenter presenter) {
					setInSlot(ACTIVITIES_SLOT, presenter);
					getView().setSelectedTab("Home");
					presenter.loadActivities();
				}
			});
		}

	}

	private void clear() {
		// clear document slot
		setInSlot(DATEGROUP_SLOT, null);
		setInSlot(DOCUMENT_SLOT, null);
	}

	private void loadTasks() {
		loadTasks(currentTaskType);
	}

	/**
	 * Load JBPM records
	 * 
	 * @param type
	 */
	private void loadTasks(final TaskType type) {
		clear();
		// if(type==null){
		// getView().setHeading("Home");
		// History.newItem("home;type=drafts");
		// return;
		// }
		//
		// getView().setHeading(type.getTitle());
		//
		String userId = AppContext.getUserId();

		GetTaskList request = new GetTaskList(userId, currentTaskType);
		request.setProcessInstanceId(processInstanceId);
		request.setDocumentId(documentId);

		// System.err.println("###### Search:: did="+documentId+"; PID="+processInstanceId+"; TaskType="+type);

		fireEvent(new ProcessingEvent());
		dispatcher.execute(request,
				new TaskServiceCallback<GetTaskListResult>() {
					@Override
					public void processResult(GetTaskListResult result) {

						GetTaskListResult rst = (GetTaskListResult) result;
						List<Doc> tasks = rst.getTasks();
						loadLines(tasks);
						System.err.println("Tasks Loaded >> " + tasks.size());

						if (tasks.size() > 0) {
							getView().setHasItems(true);

							Doc doc = tasks.get(0);
							Long docId = null;
							DocMode docMode = DocMode.READ;

							if (doc instanceof Document) {
								docId = (Long) doc.getId();
								if (((Document) doc).getStatus() == DocStatus.DRAFTED) {
									docMode = DocMode.READWRITE;
								}
								// Load document
								fireEvent(new DocumentSelectionEvent(docId,
										null, docMode));
							} else {
								docId = ((HTSummary) doc).getDocumentRef();
								long taskId = ((HTSummary) doc).getId();
								// Load Task
								fireEvent(new DocumentSelectionEvent(docId,
										taskId, docMode));
							}

						} else {
							getView().setHasItems(false);
						}

						fireEvent(new ProcessingCompletedEvent());
					}

				});
	}

	/**
	 * 
	 * @param tasks
	 */
	protected void loadLines(final List<Doc> tasks) {
		setInSlot(DATEGROUP_SLOT, null);
		final List<Date> dates = new ArrayList<Date>();

		for (int i = 0; i < tasks.size(); i++) {
			// final String dt =
			// DateUtils.FULLDATEFORMAT.format(tasks.get(i).getCreated());
			final Doc doc = tasks.get(i);
			final String dt = DateUtils.DATEFORMAT.format(doc.getCreated());
			final Date date = DateUtils.DATEFORMAT.parse(dt);

			if (dates.contains(date)) {
				fireEvent(new PresentTaskEvent(doc));
			} else {
				dateGroupFactory.get(new ServiceCallback<DateGroupPresenter>() {
					@Override
					public void processResult(DateGroupPresenter result) {
						result.setDate(doc.getCreated());
						HomePresenter.this.addToSlot(DATEGROUP_SLOT, result);
						fireEvent(new PresentTaskEvent(doc));
						dates.add(date);
					}
				});

			}
		}

	}

	protected void showEditForm(final Long programId) {
		showEditForm(programId, true);
	}

	/**
	 * Navigate to the program Url on Save
	 * 
	 * @param programId
	 * @param navigateOnSave
	 */
	protected void showEditForm(final Long programId,
			final boolean navigateOnSave) {
		createDocProvider.get(new ServiceCallback<CreateProgramPresenter>() {
			@Override
			public void processResult(CreateProgramPresenter result) {
				result.setNavigateOnSave(navigateOnSave);
				result.setProgramId(programId);
				addToPopupSlot(result, false);
			}
		});
	}

	@Override
	protected void onReset() {
		super.onReset();
		// System.err.println("HomePresenter - OnReset :: "+this);
		setInSlot(FILTER_SLOT, filterPresenter);
		setInSlot(DOCPOPUP_SLOT, docPopup);
	}

	@Override
	public void onAfterSave(AfterSaveEvent event) {
		loadTasks();
	}

	@Override
	public void onDocumentSelection(DocumentSelectionEvent event) {

		displayDocument(event.getDocumentId(), event.getTaskId());
	}

	private void displayDocument(final Long documentId, final Long taskId) {
		if (documentId == null && taskId == null) {
			setInSlot(DOCUMENT_SLOT, null);
			return;
		}

		docViewFactory.get(new ServiceCallback<GenericDocumentPresenter>() {
			@Override
			public void processResult(GenericDocumentPresenter result) {
				result.setDocId(documentId, taskId);
				setInSlot(DOCUMENT_SLOT, result);
			}
		});
	}

	@Override
	public void onReload(ReloadEvent event) {
		loadTasks();
	}

	@Override
	public void onAlertLoad(AlertLoadEvent event) {
		// event.getAlerts();
		getView().bindAlerts(event.getAlerts());
		Integer count = event.getAlerts().get(currentTaskType);
		if (count == null)
			count = 0;
		if (currentTaskType != null)
			Window.setTitle(currentTaskType.getTitle()
					+ (count == 0 ? "" : " (" + count + ")"));
	}

	@Override
	public void onActivitiesSelected(ActivitiesSelectedEvent event) {

	}

	@Override
	public void onProcessingCompleted(ProcessingCompletedEvent event) {
		getView().showmask(false);

	}

	@Override
	public void onProcessing(ProcessingEvent event) {
		getView().showmask(true);
	}

	@Override
	public void onSearch(SearchEvent event) {
		SearchFilter filter = event.getFilter();
		search(filter);
	}

	@Override
	public void onCreateProgram(CreateProgramEvent event) {
		Long programId = event.getProgramId();
		showEditForm(programId, event.isNavigateOnSave());
	}

	@Override
	public void onContextLoaded(ContextLoadedEvent event) {
		getView().showUserImg(event.getCurrentUser());
	}

}
