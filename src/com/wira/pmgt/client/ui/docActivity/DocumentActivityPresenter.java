package com.wira.pmgt.client.ui.docActivity;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class DocumentActivityPresenter extends
		PresenterWidget<DocumentActivityPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public DocumentActivityPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
