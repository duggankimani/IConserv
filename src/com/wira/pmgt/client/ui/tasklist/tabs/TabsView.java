package com.wira.pmgt.client.ui.tasklist.tabs;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class TabsView extends ViewImpl implements TabsPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, TabsView> {
	}

	@Inject
	public TabsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
