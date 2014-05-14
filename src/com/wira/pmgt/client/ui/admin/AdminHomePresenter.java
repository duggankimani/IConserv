package com.wira.pmgt.client.ui.admin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;
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
import com.wira.pmgt.client.place.NameTokens;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.MainPagePresenter;
import com.wira.pmgt.client.ui.admin.dashboard.DashboardPresenter;
import com.wira.pmgt.client.ui.admin.ds.DataSourcePresenter;
import com.wira.pmgt.client.ui.admin.formbuilder.FormBuilderPresenter;
import com.wira.pmgt.client.ui.admin.processes.ProcessPresenter;
import com.wira.pmgt.client.ui.admin.reports.ReportsPresenter;
import com.wira.pmgt.client.ui.admin.settings.SettingsPresenter;
import com.wira.pmgt.client.ui.admin.users.UserPresenter;
import com.wira.pmgt.client.ui.admin.users.save.UserSavePresenter.TYPE;
import com.wira.pmgt.client.ui.events.LoadAlertsEvent;
import com.wira.pmgt.client.ui.events.LoadGroupsEvent;
import com.wira.pmgt.client.ui.events.LoadProcessesEvent;
import com.wira.pmgt.client.ui.events.LoadUsersEvent;
import com.wira.pmgt.client.ui.login.LoginGateKeeper;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.requests.CreateActivityFormRequest;
import com.wira.pmgt.shared.requests.GetContextRequest;
import com.wira.pmgt.shared.responses.CreateActivityFormResponse;
import com.wira.pmgt.shared.responses.GetContextRequestResult;

public class AdminHomePresenter extends
		Presenter<AdminHomePresenter.MyView, AdminHomePresenter.MyProxy> {

	public interface MyView extends View {
		public void SetDashboardLink(boolean status, ADMINPAGES pages);

		public void SetProcessLink(boolean status, ADMINPAGES pages);

		public void SetUsersLink(boolean status, ADMINPAGES pages);

		public void SetReportLink(boolean status, ADMINPAGES pages);

		public void SetDSLink(boolean status, ADMINPAGES pages);
		
		public void clearAllLinks();

		public void SetFormBuilderLinks(boolean b, ADMINPAGES page);

		public void SetSettingsLink(boolean b, ADMINPAGES page);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.adminhome)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<AdminHomePresenter> {
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> CONTENT_SLOT = new Type<RevealContentHandler<?>>();

	@Inject	ProcessPresenter process;
	@Inject	UserPresenter users;
	@Inject	DashboardPresenter dashboard;
	@Inject	ReportsPresenter reports;
	@Inject	FormBuilderPresenter formbuilder;
	@Inject DataSourcePresenter datasources;
	@Inject SettingsPresenter settings;
	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeMgr;
	
	enum ADMINPAGES {
		DASHBOARD("Dashboard", "icon-dashboard"), 
		PROCESSES("Processes", "icon-cogs"),
		USERS("Users","icon-group"),
		GROUPS("Groups","icon-group"), 
		REPORTS("Reports","icon-bar-chart"), 
		FORMBUILDER("Form Builder","icon-edit"),
		DATASOURCES("Data Sources","icon-briefcase"), 
		SETTINGS("General Settings","icon-globe");

		private String displayName;
		private String displayIcon;

		private ADMINPAGES(String displayName, String displayIcon) {
			this.displayName = displayName;
			this.displayIcon = displayIcon;
		}

		public String getDisplayName() {
			return displayName;
		}
		
		public String getDisplayIcon() {
			return displayIcon;
		}

	}

	ADMINPAGES page = null;

	@Inject
	public AdminHomePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);

	}

	@Override
	protected void revealInParent() {
		if(AppContext.getContextUser()==null || AppContext.getContextUser().getGroups()==null){
			dispatcher.execute(new GetContextRequest(), new TaskServiceCallback<GetContextRequestResult>() {
				@Override
				public void processResult(GetContextRequestResult result) {
					HTUser user = result.getUser();
					revealMeToUser(user);
				}			
			});
		}else{
			revealMeToUser(AppContext.getContextUser());
		}
		
	}

	protected void revealMeToUser(HTUser user) {
		if(AppContext.isCurrentUserAdmin()){
			RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
		}else{
			//redirect
			History.newItem("#home");
		}
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		String name = request.getParameter("page",
				ADMINPAGES.DASHBOARD.toString());

		ADMINPAGES pages = null;
		
		try{
			pages = ADMINPAGES.valueOf(name.toUpperCase());
		}catch(Exception e){
			History.newItem(NameTokens.adminhome);
			return;
		}

		//reload alerts
		fireEvent(new LoadAlertsEvent());
		
		this.page = pages;

		switch (pages) {
		case DASHBOARD:
			showDashBoard();
			break;

		case PROCESSES:
			showProcessPanel();
			fireEvent(new LoadProcessesEvent());
			break;

		case USERS:
			showUserPanel();
			fireEvent(new LoadUsersEvent());
			break;

		case FORMBUILDER:
			String value = request.getParameter("formid", "");
			String action= request.getParameter("create", null);
			if(action!=null){
				createFormForActivity(new Long(action));
				//break flow here-- redirect on feedback
				return;
			}
			
			Long formId=null;
			if(!value.isEmpty() && value.matches("[0-9]+")){
				formId = new Long(value);
			}
			
			showFormBuilderPanel(formId);
			//fireEvent(new LoadFormBuilderEvent());
			break;

		case GROUPS:
			showUserPanel(TYPE.GROUP);
			fireEvent(new LoadGroupsEvent());
			break;
		
		case SETTINGS:
			showSettingsPanel();
			break;
			
		case REPORTS:
			showReportPanel();
			break;
			
		case DATASOURCES:
			showDSPanel();
			break;
		}

	}

	private void createFormForActivity(Long activityId) {
		dispatcher.execute(new CreateActivityFormRequest(activityId),
				new TaskServiceCallback<CreateActivityFormResponse>() {
			@Override
			public void processResult(
					CreateActivityFormResponse aResponse) {
				System.err.println("Generated FormId = "+aResponse.getFormId());
				PlaceRequest place = new PlaceRequest("adminhome")
						.with("page", "formbuilder")
						.with("formid", aResponse.getFormId()+"");
				placeMgr.revealPlace(place);
			}
		});
	}

	private void showSettingsPanel() {
		setInSlot(CONTENT_SLOT, null);
		setInSlot(CONTENT_SLOT, settings);
		getView().clearAllLinks();
		getView().SetSettingsLink(true, page);
	}

	private void showDSPanel() {
		setInSlot(CONTENT_SLOT, null);
		setInSlot(CONTENT_SLOT, datasources);
		getView().clearAllLinks();
		getView().SetDSLink(true, page);
	}

	private void showDashBoard() {
		setInSlot(CONTENT_SLOT, null);
		setInSlot(CONTENT_SLOT, dashboard);
		getView().clearAllLinks();
		getView().SetDashboardLink(true, page);
	}

	private void showProcessPanel() {
		setInSlot(CONTENT_SLOT, process);
		getView().clearAllLinks();
		getView().SetProcessLink(true, page);
	}

	private void showUserPanel() {
		showUserPanel(TYPE.USER);
	}

	private void showUserPanel(TYPE type) {
		users.setType(type);
		setInSlot(CONTENT_SLOT, users);
		getView().clearAllLinks();
		getView().SetUsersLink(true, page);
	}
	
	private void showFormBuilderPanel(Long formId) {
		formbuilder.setFormId(formId);
		setInSlot(CONTENT_SLOT, formbuilder);
		getView().clearAllLinks();
		getView().SetFormBuilderLinks(true, page);
	}
	
	private void showReportPanel() {
		setInSlot(CONTENT_SLOT, reports);
		getView().clearAllLinks();
		getView().SetReportLink(true, page);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	protected void onReset() {
		super.onReset();
	}
}
