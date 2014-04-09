package com.wira.pmgt.client.gin;

import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.wira.pmgt.client.place.ClientPlaceManager;
import com.wira.pmgt.client.place.DefaultPlace;
import com.wira.pmgt.client.place.NameTokens;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.MainPagePresenter;
import com.wira.pmgt.client.ui.MainPageView;
import com.wira.pmgt.client.ui.activityfeed.ActivitiesPresenter;
import com.wira.pmgt.client.ui.activityfeed.ActivitiesView;
import com.wira.pmgt.client.ui.addDoc.DocumentPopupPresenter;
import com.wira.pmgt.client.ui.addDoc.DocumentPopupView;
import com.wira.pmgt.client.ui.addDoc.doctypeitem.DocTypeItemPresenter;
import com.wira.pmgt.client.ui.addDoc.doctypeitem.DocTypeItemView;
import com.wira.pmgt.client.ui.admin.AdminHomePresenter;
import com.wira.pmgt.client.ui.admin.AdminHomeView;
import com.wira.pmgt.client.ui.admin.dashboard.DashboardPresenter;
import com.wira.pmgt.client.ui.admin.dashboard.DashboardView;
import com.wira.pmgt.client.ui.admin.dashboard.charts.PieChartPresenter;
import com.wira.pmgt.client.ui.admin.dashboard.charts.PieChartView;
import com.wira.pmgt.client.ui.admin.dashboard.linegraph.LineGraphPresenter;
import com.wira.pmgt.client.ui.admin.dashboard.linegraph.LineGraphView;
import com.wira.pmgt.client.ui.admin.dashboard.table.TableDataPresenter;
import com.wira.pmgt.client.ui.admin.dashboard.table.TableDataView;
import com.wira.pmgt.client.ui.admin.ds.DataSourcePresenter;
import com.wira.pmgt.client.ui.admin.ds.DataSourceView;
import com.wira.pmgt.client.ui.admin.ds.item.DSItemPresenter;
import com.wira.pmgt.client.ui.admin.ds.item.DSItemView;
import com.wira.pmgt.client.ui.admin.ds.save.DSSavePresenter;
import com.wira.pmgt.client.ui.admin.ds.save.DSSaveView;
import com.wira.pmgt.client.ui.admin.formbuilder.FormBuilderPresenter;
import com.wira.pmgt.client.ui.admin.formbuilder.FormBuilderView;
import com.wira.pmgt.client.ui.admin.formbuilder.propertypanel.PropertyPanelPresenter;
import com.wira.pmgt.client.ui.admin.formbuilder.propertypanel.PropertyPanelView;
import com.wira.pmgt.client.ui.admin.processes.ProcessPresenter;
import com.wira.pmgt.client.ui.admin.processes.ProcessView;
import com.wira.pmgt.client.ui.admin.processes.save.ProcessSavePresenter;
import com.wira.pmgt.client.ui.admin.processes.save.ProcessSaveView;
import com.wira.pmgt.client.ui.admin.processitem.ProcessItemPresenter;
import com.wira.pmgt.client.ui.admin.processitem.ProcessItemView;
import com.wira.pmgt.client.ui.admin.reports.ReportsPresenter;
import com.wira.pmgt.client.ui.admin.reports.ReportsView;
import com.wira.pmgt.client.ui.admin.settings.SettingsPresenter;
import com.wira.pmgt.client.ui.admin.settings.SettingsView;
import com.wira.pmgt.client.ui.admin.users.UserPresenter;
import com.wira.pmgt.client.ui.admin.users.UserView;
import com.wira.pmgt.client.ui.admin.users.groups.GroupPresenter;
import com.wira.pmgt.client.ui.admin.users.groups.GroupView;
import com.wira.pmgt.client.ui.admin.users.item.UserItemPresenter;
import com.wira.pmgt.client.ui.admin.users.item.UserItemView;
import com.wira.pmgt.client.ui.admin.users.save.UserSavePresenter;
import com.wira.pmgt.client.ui.admin.users.save.UserSaveView;
import com.wira.pmgt.client.ui.comments.CommentPresenter;
import com.wira.pmgt.client.ui.comments.CommentView;
import com.wira.pmgt.client.ui.docActivity.DocumentActivityPresenter;
import com.wira.pmgt.client.ui.docActivity.DocumentActivityView;
import com.wira.pmgt.client.ui.document.GenericDocumentPresenter;
import com.wira.pmgt.client.ui.document.GenericDocumentView;
import com.wira.pmgt.client.ui.error.ErrorPagePresenter;
import com.wira.pmgt.client.ui.error.ErrorPageView;
import com.wira.pmgt.client.ui.error.ErrorPresenter;
import com.wira.pmgt.client.ui.error.ErrorView;
import com.wira.pmgt.client.ui.error.NotfoundPresenter;
import com.wira.pmgt.client.ui.error.NotfoundView;
import com.wira.pmgt.client.ui.filter.FilterPresenter;
import com.wira.pmgt.client.ui.filter.FilterView;
import com.wira.pmgt.client.ui.header.HeaderPresenter;
import com.wira.pmgt.client.ui.header.HeaderView;
import com.wira.pmgt.client.ui.home.HomePresenter;
import com.wira.pmgt.client.ui.home.HomeView;
import com.wira.pmgt.client.ui.login.LoginPresenter;
import com.wira.pmgt.client.ui.login.LoginView;
import com.wira.pmgt.client.ui.notifications.NotificationsPresenter;
import com.wira.pmgt.client.ui.notifications.NotificationsView;
import com.wira.pmgt.client.ui.notifications.note.NotePresenter;
import com.wira.pmgt.client.ui.notifications.note.NoteView;
import com.wira.pmgt.client.ui.popup.GenericPopupPresenter;
import com.wira.pmgt.client.ui.popup.GenericPopupView;
import com.wira.pmgt.client.ui.profile.ProfilePresenter;
import com.wira.pmgt.client.ui.profile.ProfileView;
import com.wira.pmgt.client.ui.save.CreateDocPresenter;
import com.wira.pmgt.client.ui.save.CreateDocView;
import com.wira.pmgt.client.ui.save.form.GenericFormPresenter;
import com.wira.pmgt.client.ui.save.form.GenericFormView;
import com.wira.pmgt.client.ui.task.perfomancereview.PersonnelReviewPresenter;
import com.wira.pmgt.client.ui.task.perfomancereview.PersonnelReviewView;
import com.wira.pmgt.client.ui.task.personalreview.PersonalReviewPresenter;
import com.wira.pmgt.client.ui.task.personalreview.PersonalReviewView;
import com.wira.pmgt.client.ui.tasklist.tabs.TabsPresenter;
import com.wira.pmgt.client.ui.tasklist.tabs.TabsView;
import com.wira.pmgt.client.ui.tasklistitem.DateGroupPresenter;
import com.wira.pmgt.client.ui.tasklistitem.DateGroupView;
import com.wira.pmgt.client.ui.tasklistitem.TaskItemPresenter;
import com.wira.pmgt.client.ui.tasklistitem.TaskItemView;
import com.wira.pmgt.client.ui.toolbar.ToolbarPresenter;
import com.wira.pmgt.client.ui.toolbar.ToolbarView;
import com.wira.pmgt.client.ui.upload.UploadDocumentPresenter;
import com.wira.pmgt.client.ui.upload.UploadDocumentView;
import com.wira.pmgt.client.ui.upload.attachment.AttachmentPresenter;
import com.wira.pmgt.client.ui.upload.attachment.AttachmentView;
import com.wira.pmgt.client.ui.upload.href.IFrameDataPresenter;
import com.wira.pmgt.client.ui.upload.href.IFrameDataView;
import com.wira.pmgt.client.ui.user.UserSelectionPresenter;
import com.wira.pmgt.client.ui.user.UserSelectionView;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.client.util.Definitions;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(ClientPlaceManager.class));
		
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
		
		bindConstant().annotatedWith(SecurityCookie.class).to(Definitions.AUTHENTICATIONCOOKIE);

		bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
				MainPageView.class, MainPagePresenter.MyProxy.class);

		bindPresenter(HomePresenter.class,
				HomePresenter.MyView.class, HomeView.class,
				HomePresenter.MyProxy.class);

		bindPresenterWidget(TaskItemPresenter.class,
				TaskItemPresenter.ITaskItemView.class, TaskItemView.class);

		bindPresenterWidget(HeaderPresenter.class,
				HeaderPresenter.IHeaderView.class, HeaderView.class);

		bindPresenterWidget(TabsPresenter.class, TabsPresenter.MyView.class,
				TabsView.class);

		bindPresenter(PersonalReviewPresenter.class,
				PersonalReviewPresenter.MyView.class, PersonalReviewView.class,
				PersonalReviewPresenter.MyProxy.class);

		bindPresenter(PersonnelReviewPresenter.class,
				PersonnelReviewPresenter.MyView.class,
				PersonnelReviewView.class,
				PersonnelReviewPresenter.MyProxy.class);
		
		bindPresenterWidget(ToolbarPresenter.class,
				ToolbarPresenter.MyView.class, ToolbarView.class);

		bindPresenterWidget(CreateDocPresenter.class,
				CreateDocPresenter.ICreateDocView.class, CreateDocView.class);
		
		bindPresenter(ErrorPagePresenter.class,
				ErrorPagePresenter.MyView.class, ErrorPageView.class,
				ErrorPagePresenter.MyProxy.class);
		
		bindPresenterWidget(ErrorPresenter.class, ErrorPresenter.MyView.class,
				ErrorView.class);

		bindPresenterWidget(GenericDocumentPresenter.class,
				GenericDocumentPresenter.MyView.class,
				GenericDocumentView.class);

		bindPresenter(LoginPresenter.class, LoginPresenter.ILoginView.class,
				LoginView.class, LoginPresenter.MyProxy.class);
		
		requestStaticInjection(AppContext.class);
		requestStaticInjection(AppManager.class);

		bindPresenterWidget(DateGroupPresenter.class,
				DateGroupPresenter.MyView.class, DateGroupView.class);

		bindPresenter(NotfoundPresenter.class, NotfoundPresenter.MyView.class,
				NotfoundView.class, NotfoundPresenter.MyProxy.class);

		bindPresenterWidget(NotificationsPresenter.class,
				NotificationsPresenter.MyView.class, NotificationsView.class);

		bindPresenterWidget(NotePresenter.class,
				NotePresenter.MyView.class, NoteView.class);

		bindPresenterWidget(ActivitiesPresenter.class,
				ActivitiesPresenter.MyView.class, ActivitiesView.class);

		bindPresenterWidget(CommentPresenter.class,
				CommentPresenter.ICommentView.class, CommentView.class);
		
		bindPresenterWidget(AttachmentPresenter.class, 
				AttachmentPresenter.IAttachmentView.class, AttachmentView.class);
		
		bindPresenterWidget(UserSelectionPresenter.class,
				UserSelectionPresenter.MyView.class, UserSelectionView.class);

		bindPresenterWidget(UploadDocumentPresenter.class,
				UploadDocumentPresenter.MyView.class, UploadDocumentView.class);

		bindPresenterWidget(FilterPresenter.class,
				FilterPresenter.MyView.class, FilterView.class);

		bindPresenter(AdminHomePresenter.class,
				AdminHomePresenter.MyView.class, AdminHomeView.class,
				AdminHomePresenter.MyProxy.class);

		bindPresenterWidget(ProcessSavePresenter.class,
				ProcessSavePresenter.IProcessSaveView.class, ProcessSaveView.class);

		bindPresenterWidget(ProcessItemPresenter.class,
				ProcessItemPresenter.MyView.class, ProcessItemView.class);
		
		bindPresenterWidget(ProcessPresenter.class, ProcessPresenter.IProcessView.class, ProcessView.class);


		bindPresenterWidget(UserPresenter.class, UserPresenter.MyView.class,
				UserView.class);

		bindPresenterWidget(DashboardPresenter.class,
				DashboardPresenter.IDashboardView.class, DashboardView.class);

		bindPresenterWidget(ReportsPresenter.class,
				ReportsPresenter.MyView.class, ReportsView.class);
		
		bindPresenterWidget(UserSavePresenter.class,
				UserSavePresenter.IUserSaveView.class, UserSaveView.class);
		
		bindPresenterWidget(UserItemPresenter.class, UserItemPresenter.MyView.class,
				UserItemView.class);
		
		bindPresenterWidget(GroupPresenter.class, GroupPresenter.MyView.class, GroupView.class);

		bindPresenterWidget(FormBuilderPresenter.class,
				FormBuilderPresenter.IFormBuilderView.class, FormBuilderView.class);

		bindPresenterWidget(PropertyPanelPresenter.class,
				PropertyPanelPresenter.MyView.class, PropertyPanelView.class);
		
		bindPresenterWidget(GenericPopupPresenter.class, GenericPopupPresenter.MyView.class,
				GenericPopupView.class);
		
		bindPresenterWidget(GenericFormPresenter.class, GenericFormPresenter.ICreateDocView.class,
				GenericFormView.class);

		bindPresenterWidget(DocumentPopupPresenter.class,
				DocumentPopupPresenter.MyView.class, DocumentPopupView.class);

		bindPresenterWidget(DocTypeItemPresenter.class,
				DocTypeItemPresenter.MyView.class, DocTypeItemView.class);

		bindPresenterWidget(DocumentActivityPresenter.class,
				DocumentActivityPresenter.MyView.class,
				DocumentActivityView.class);
		
		bindPresenterWidget(IFrameDataPresenter.class,
				IFrameDataPresenter.IFrameView.class, IFrameDataView.class);

		bindPresenterWidget(DataSourcePresenter.class,
				DataSourcePresenter.IDataSourceView.class,
				DataSourceView.class);
		
		bindPresenterWidget(DSItemPresenter.class,
				DSItemPresenter.MyView.class,
				DSItemView.class);
		
		bindPresenterWidget(DSSavePresenter.class,
				DSSavePresenter.IDSSaveView.class,
				DSSaveView.class);

		bindPresenterWidget(ProfilePresenter.class, ProfilePresenter.IProfileView.class,
				ProfileView.class);
		
		bindPresenterWidget(PieChartPresenter.class, PieChartPresenter.IPieChartView.class,
				PieChartView.class);
		
		bindPresenterWidget(LineGraphPresenter.class, LineGraphPresenter.ILineGraphView.class,
				LineGraphView.class);

		bindPresenterWidget(SettingsPresenter.class, SettingsPresenter.ISettingsView.class,
				SettingsView.class);
		
		bindPresenterWidget(TableDataPresenter.class, TableDataPresenter.ITableDataView.class,
				TableDataView.class);

	}
}
