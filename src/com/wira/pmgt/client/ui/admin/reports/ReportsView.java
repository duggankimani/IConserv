package com.wira.pmgt.client.ui.admin.reports;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ReportsView extends ViewImpl implements ReportsPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ReportsView> {
	}

	@Inject
	public ReportsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
