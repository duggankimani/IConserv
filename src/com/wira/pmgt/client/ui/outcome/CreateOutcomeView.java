package com.wira.pmgt.client.ui.outcome;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class CreateOutcomeView extends ViewImpl implements
		CreateOutcomePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateOutcomeView> {
	}

	@Inject
	public CreateOutcomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
