package com.wira.pmgt.client.ui.admin.reports;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class AdminReportsPresenter extends PresenterWidget<AdminReportsPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public AdminReportsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
