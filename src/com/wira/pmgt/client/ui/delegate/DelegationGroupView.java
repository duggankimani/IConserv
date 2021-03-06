package com.wira.pmgt.client.ui.delegate;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.admin.formbuilder.component.FieldWidget;
import com.wira.pmgt.client.ui.delegate.userDisplay.UserDisplay;
import com.wira.pmgt.client.ui.events.PresentUserEvent;
import com.wira.pmgt.client.ui.events.PresentUserEvent.PresentUserHandler;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.UserGroup;

public class DelegationGroupView extends Composite implements PresentUserHandler {

	private static DelegationGroupViewUiBinder uiBinder = GWT
			.create(DelegationGroupViewUiBinder.class);

	interface DelegationGroupViewUiBinder extends
			UiBinder<Widget, DelegationGroupView> {
	}
	

	@UiField HTMLPanel divNames;
	
	@UiField DivElement divContainer;
	@UiField Anchor aAccordion;
	@UiField DivElement divAccordion;
	

	UserGroup group;
	int groupCounter;
	
	public DelegationGroupView(UserGroup group, int count) {
		initWidget(uiBinder.createAndBindUi(this));
		this.groupCounter=count;
		this.group = group;
		setAccordion();
		aAccordion.setText(group.getFullName());
		addRegisteredHandler(PresentUserEvent.TYPE, this);
	}
	
	
	private void setAccordion() {
		divContainer.setAttribute("id", "accordion"+groupCounter);
		aAccordion.getElement().setAttribute("data-toggle", "collapse");
		aAccordion.getElement().setAttribute("data-parent", "#accordion"+groupCounter);
		aAccordion.setHref("#collapse"+groupCounter);
		divAccordion.setAttribute("id", "collapse"+groupCounter);
		if(groupCounter==1){
			divAccordion.addClassName("in");
			aAccordion.removeStyleName("collapsed");
		}else{
			
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
		cleanUpEvents();
	}

	/**
	 * 
	 * @param type
	 * @param handler
	 */
	public void addRegisteredHandler(Type<? extends EventHandler> type, DelegationGroupView handler){
		@SuppressWarnings("unchecked")
		HandlerRegistration hr = AppContext.getEventBus().addHandler(
				(GwtEvent.Type<EventHandler>)type, handler);
		handlers.add(hr);
	}
	
	/**
	 * 
	 */
	private void cleanUpEvents() {
		for(HandlerRegistration hr: handlers){
			hr.removeHandler();
		}
		handlers.clear();
	}
	
	List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

	@Override
	public void onPresentUser(PresentUserEvent event) {
		if(event.getGroup().equals(group)){
			//add user here
			HTUser user = event.getUser();
			UserDisplay display = new UserDisplay(user);	
			divNames.add(display);
		}
	}


	public HTUser getSelectedUser() {
		int count = divNames.getWidgetCount();
		for(int i=0; i<count; i++){
			UserDisplay view = (UserDisplay)divNames.getWidget(i);
			HTUser user = view.getSelectedUser();
			if(user!=null){
				return user;
			}
		}
		
		return null;
	}
	
}
