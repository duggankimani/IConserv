package com.wira.pmgt.client.ui.upload.href;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.wira.pmgt.client.ui.events.CloseAttatchmentEvent;
import com.wira.pmgt.client.ui.events.ReloadAttachmentsEvent;
import com.wira.pmgt.client.ui.events.CloseAttatchmentEvent.CloseAttatchmentHandler;

public class IFrameDataPresenter extends
		PresenterWidget<IFrameDataPresenter.IFrameView> implements CloseAttatchmentHandler{

	public interface IFrameView extends PopupView {
		HasClickHandlers getDoneButton();
	}

	@Inject
	public IFrameDataPresenter(final EventBus eventBus, final IFrameView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(CloseAttatchmentEvent.TYPE, this);
		getView().getDoneButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ReloadAttachmentsEvent());
			}
		});
	}

	@Override
	public void onCloseAttatchment(CloseAttatchmentEvent event) {
		getView().hide();
	}
}
