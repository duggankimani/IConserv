package com.wira.pmgt.client.ui.detailedActivity;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class CreateActivityPresenter extends
		PresenterWidget<CreateActivityPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public CreateActivityPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
