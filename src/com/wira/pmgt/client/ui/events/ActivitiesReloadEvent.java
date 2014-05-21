package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ActivitiesReloadEvent extends
		GwtEvent<ActivitiesReloadEvent.ActivitiesReloadHandler> {

	public static Type<ActivitiesReloadHandler> TYPE = new Type<ActivitiesReloadHandler>();

	public interface ActivitiesReloadHandler extends EventHandler {
		void onActivitiesReload(ActivitiesReloadEvent event);
	}

	public ActivitiesReloadEvent() {
	}

	@Override
	protected void dispatch(ActivitiesReloadHandler handler) {
		handler.onActivitiesReload(this);
	}

	@Override
	public Type<ActivitiesReloadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ActivitiesReloadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ActivitiesReloadEvent());
	}
}
