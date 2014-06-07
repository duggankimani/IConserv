package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Boolean;
import com.google.gwt.event.shared.HasHandlers;

public class NavbarToggleEvent extends
		GwtEvent<NavbarToggleEvent.NavbarToggleHandler> {

	public static Type<NavbarToggleHandler> TYPE = new Type<NavbarToggleHandler>();
	private Boolean isClicked;

	public interface NavbarToggleHandler extends EventHandler {
		void onNavbarToggle(NavbarToggleEvent event);
	}

	public NavbarToggleEvent(Boolean isClicked) {
		this.isClicked = isClicked;
	}

	public Boolean getIsClicked() {
		return isClicked;
	}

	@Override
	protected void dispatch(NavbarToggleHandler handler) {
		handler.onNavbarToggle(this);
	}

	@Override
	public Type<NavbarToggleHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NavbarToggleHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Boolean isClicked) {
		source.fireEvent(new NavbarToggleEvent(isClicked));
	}
}
