package com.wira.pmgt.client.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.wira.pmgt.client.ui.MainPagePresenter;
import com.wira.pmgt.client.ui.activities.ActivitiesPresenter;
import com.wira.pmgt.client.ui.admin.AdminHomePresenter;
import com.wira.pmgt.client.ui.admin.settings.SettingsPresenter;
import com.wira.pmgt.client.ui.error.ErrorPagePresenter;
import com.wira.pmgt.client.ui.error.NotfoundPresenter;
import com.wira.pmgt.client.ui.home.HomePresenter;
import com.wira.pmgt.client.ui.login.LoginGateKeeper;
import com.wira.pmgt.client.ui.login.LoginPresenter;

@GinModules({ DispatchAsyncModule.class, ClientModule.class })
public interface ClientGinjector extends Ginjector {

	EventBus getEventBus();

	PlaceManager getPlaceManager();

	AsyncProvider<MainPagePresenter> getMainPagePresenter();

	AsyncProvider<HomePresenter> getTaskListUIPresenter();

	AsyncProvider<ErrorPagePresenter> getErrorPagePresenter();

	AsyncProvider<LoginPresenter> getLoginPresenter();
	
	AsyncProvider<ActivitiesPresenter> getActivitiesPresenter();
	
	LoginGateKeeper getLoggedInGateKeeper();
	
	AsyncProvider<NotfoundPresenter> getNotfoundPresenter();

	AsyncProvider<AdminHomePresenter> getAdminHomePresenter();

	AsyncProvider<SettingsPresenter> getSettingsPresenter();

}
