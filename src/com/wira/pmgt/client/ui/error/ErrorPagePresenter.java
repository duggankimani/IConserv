package com.wira.pmgt.client.ui.error;

import java.util.Date;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.wira.pmgt.client.place.NameTokens;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.MainPagePresenter;
import com.wira.pmgt.shared.requests.GetErrorRequest;
import com.wira.pmgt.shared.responses.GetErrorRequestResult;

public class ErrorPagePresenter extends
		Presenter<ErrorPagePresenter.MyView, ErrorPagePresenter.MyProxy> {

	public interface MyView extends View {
		
		public void setError(Date errorDate,String message, String stack, String userAgent, String address);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.error)
	public interface MyProxy extends ProxyPlace<ErrorPagePresenter> {
	}
	
	@Inject DispatchAsync dispatcher;

	@Inject
	public ErrorPagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		String errorId = request.getParameter("errorid", "0");
		
		if(errorId.equals("0")){
			getView().setError(new Date(),"Not Error Retrieved. Error Id must be specified to retrieve errors", "", "", "");
			return;
		}
		
		dispatcher.execute(new GetErrorRequest(new Long(errorId)), new TaskServiceCallback<GetErrorRequestResult>() {
			@Override
			public void processResult(GetErrorRequestResult result) {
				getView().setError(result.getErrorDate(), result.getMessage(), result.getStack(), result.getAgent(), result.getRemoteAddress());
			}
		});
		
	}
	
	@Override
	protected void revealInParent() {
		//RevealRootContentEvent.fire(this, this);
		RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
