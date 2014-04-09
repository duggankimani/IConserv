package com.wira.pmgt.client.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.wira.pmgt.client.ui.MainPagePresenter;
import com.wira.pmgt.client.ui.admin.AdminHomePresenter;
import com.wira.pmgt.client.ui.admin.settings.SettingsPresenter;
import com.wira.pmgt.client.ui.error.ErrorPagePresenter;
import com.wira.pmgt.client.ui.error.NotfoundPresenter;
import com.wira.pmgt.client.ui.home.HomePresenter;
import com.wira.pmgt.client.ui.login.LoginGateKeeper;
import com.wira.pmgt.client.ui.login.LoginPresenter;
import com.wira.pmgt.client.ui.task.perfomancereview.PersonnelReviewPresenter;
import com.wira.pmgt.client.ui.task.personalreview.PersonalReviewPresenter;

@GinModules({ DispatchAsyncModule.class, ClientModule.class })
public interface ClientGinjector extends Ginjector {

	EventBus getEventBus();

	PlaceManager getPlaceManager();

	AsyncProvider<MainPagePresenter> getMainPagePresenter();

	AsyncProvider<HomePresenter> getTaskListUIPresenter();

	AsyncProvider<PersonalReviewPresenter> getPersonalReviewPresenter();

	AsyncProvider<PersonnelReviewPresenter> getPersonnelReviewPresenter();
	
	AsyncProvider<ErrorPagePresenter> getErrorPagePresenter();

	AsyncProvider<LoginPresenter> getLoginPresenter();
	
	LoginGateKeeper getLoggedInGateKeeper();
	
	AsyncProvider<NotfoundPresenter> getNotfoundPresenter();

	AsyncProvider<AdminHomePresenter> getAdminHomePresenter();

	AsyncProvider<SettingsPresenter> getSettingsPresenter();

}
