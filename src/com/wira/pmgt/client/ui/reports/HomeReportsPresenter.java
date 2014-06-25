package com.wira.pmgt.client.ui.reports;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.wira.pmgt.client.place.NameTokens;
import com.wira.pmgt.client.ui.admin.dashboard.charts.PieChartPresenter;
import com.wira.pmgt.client.ui.home.HomePresenter;
import com.wira.pmgt.client.ui.login.LoginGateKeeper;

public class HomeReportsPresenter extends
		Presenter<HomeReportsPresenter.MyView, HomeReportsPresenter.MyProxy> {

	public interface MyView extends View {
	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> PROGRAM_ANALYSIS = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> OVERALLTURNAROUND_SLOT = new Type<RevealContentHandler<?>>();
	
	private IndirectProvider<PieChartPresenter> pieChartFactory;
	
	@ProxyCodeSplit
	@NameToken(NameTokens.reports)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<HomeReportsPresenter> {
	}

	@Inject
	public HomeReportsPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, HomePresenter.ACTIVITIES_SLOT, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
