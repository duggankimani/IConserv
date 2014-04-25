package com.wira.pmgt.client.ui.detailedActivity;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class CreateActivityView extends ViewImpl implements
		CreateActivityPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateActivityView> {
	}

	IsProgramActivity Outcome; //The outcome under which this activity is created
		
	@Inject
	public CreateActivityView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public IsProgramActivity getActivity() {
		// TODO Auto-generated method stub
		return null;
	}
}
