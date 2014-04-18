package com.wira.pmgt.client.ui.outcome;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class CreateOutcomePresenter extends
		PresenterWidget<CreateOutcomePresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public CreateOutcomePresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
