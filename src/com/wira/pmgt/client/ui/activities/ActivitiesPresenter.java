package com.wira.pmgt.client.ui.activities;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.ui.events.ContextLoadedEvent;
import com.wira.pmgt.client.ui.events.ContextLoadedEvent.ContextLoadedHandler;

public class ActivitiesPresenter extends
		PresenterWidget<ActivitiesPresenter.IActivitiesView> implements ContextLoadedHandler {

	public interface IActivitiesView extends View {
	
	}
	
	@Inject DispatchAsync requestHelper;

	
	@Inject
	public ActivitiesPresenter(final EventBus eventBus, final IActivitiesView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
	
	}
	
	@Override
	public void onContextLoaded(ContextLoadedEvent event) {
	
	}
}
