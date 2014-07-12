package com.wira.pmgt.client.ui.reports;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class HomeReportsPresenter extends
		PresenterWidget<HomeReportsPresenter.MyView> {

	public interface MyView extends View {
	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> PROGRAM_ANALYSIS = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> OVERALLTURNAROUND_SLOT = new Type<RevealContentHandler<?>>();
	
	
//	@UseGatekeeper(LoginGateKeeper.class)
//	public interface MyProxy extends ProxyPlace<HomeReportsPresenter> {
//	}

	@Inject
	public HomeReportsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
