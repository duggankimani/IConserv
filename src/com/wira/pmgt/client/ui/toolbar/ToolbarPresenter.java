package com.wira.pmgt.client.ui.toolbar;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.wira.pmgt.shared.event.ExecuteWorkflowEvent;
import com.wira.pmgt.shared.event.SetStatusEvent;
import com.wira.pmgt.shared.event.SetStatusEvent.SetStatusHandler;
import com.wira.pmgt.shared.model.Actions;
import com.wira.pmgt.shared.model.HTStatus;
import com.wira.pmgt.shared.requests.ExecuteWorkflow;

public class ToolbarPresenter extends PresenterWidget<ToolbarPresenter.MyView> implements SetStatusHandler{

	public interface MyView extends View {

		void setTaskStatus(HTStatus status);
		
		HasClickHandlers getClaimLink();
		HasClickHandlers getStartLink();
		HasClickHandlers getSuspendLink();
		HasClickHandlers getResumeLink();
		HasClickHandlers getCompleteLink();
		HasClickHandlers getDelegateLink();
		HasClickHandlers getRevokeLink();
		HasClickHandlers getStopLink();
		HasClickHandlers getForwardLink();

	}

	@Inject DispatchAsync dispatcher;
	
	
	@Inject
	public ToolbarPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
		
	}

	@Override
	protected void onBind() {
		super.onBind();
		this.addRegisteredHandler(SetStatusEvent.TYPE, this);

		final ExecuteWorkflow workflow = new ExecuteWorkflow(0l, "calcacuervo", Actions.START);

		getView().getClaimLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.CLAIM);
				
			}
		});
		
		getView().getStartLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.START);
				
			}
		});
		
		getView().getSuspendLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.SUSPEND);
				
			}
		});
		
		getView().getResumeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.RESUME);
				
			}
		});
		
		getView().getCompleteLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.COMPLETE);
				
			}
		});
		
		getView().getDelegateLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.DELEGATE);
				
			}
		});
		
		getView().getRevokeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.REVOKE);
				
			}
		});
		
		getView().getStopLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.STOP);
				
			}
		});
		
		getView().getForwardLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.FORWARD);
				
			}
		});
		
	}

	protected void submitRequest(Actions action) {
		getEventBus().fireEvent(new ExecuteWorkflowEvent(action));
	}

	@Override
	public void onSetStatus(SetStatusEvent event) {
		
		getView().setTaskStatus(event.getStatus());
	}
}
