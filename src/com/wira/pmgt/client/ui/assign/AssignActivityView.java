package com.wira.pmgt.client.ui.assign;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AssignActivityView extends ViewImpl implements
		AssignActivityPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, AssignActivityView> {
	}

	@Inject
	public AssignActivityView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
