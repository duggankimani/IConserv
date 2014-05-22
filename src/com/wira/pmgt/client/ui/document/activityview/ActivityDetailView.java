package com.wira.pmgt.client.ui.document.activityview;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.wira.pmgt.shared.model.program.IsProgramDetail;

public class ActivityDetailView extends ViewImpl implements
		ActivityDetailPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ActivityDetailView> {
	}

	@Inject
	public ActivityDetailView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bind(IsProgramDetail singleActivity) {
		
	}
}
