package com.wira.pmgt.client.ui.activities;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.detailedActivity.CreateActivityPresenter;
import com.wira.pmgt.client.ui.events.ContextLoadedEvent;
import com.wira.pmgt.client.ui.events.ContextLoadedEvent.ContextLoadedHandler;
import com.wira.pmgt.client.ui.outcome.CreateOutcomePresenter;

public class ActivitiesPresenter extends
		PresenterWidget<ActivitiesPresenter.IActivitiesView> implements
		ContextLoadedHandler {

	public interface IActivitiesView extends View {
		void showContent(boolean b);

		HasClickHandlers getaNewOutcome();

		HasClickHandlers getaNewActivity();
	}

	@Inject
	DispatchAsync requestHelper;

	@Inject
	CreateOutcomePresenter createOutcome;

	@Inject
	CreateActivityPresenter createActivity;

	@Inject
	public ActivitiesPresenter(final EventBus eventBus,
			final IActivitiesView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getaNewOutcome().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Create Outcome",
						createOutcome.getWidget(), null, "Save", "Cancel");
			}
		});

		getView().getaNewActivity().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Create Activity",
						createActivity.getWidget(), null, "Save", "Cancel");
			}
		});

	}

	@Override
	protected void onReset() {
		super.onReset();
	}

	@Override
	public void onContextLoaded(ContextLoadedEvent event) {
	}

	public void showContent(Boolean status) {
		if (status) {
			getView().showContent(true);
		} else {
			getView().showContent(false);
		}
	}
}
