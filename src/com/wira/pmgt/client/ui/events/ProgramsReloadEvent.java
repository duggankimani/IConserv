package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ProgramsReloadEvent extends
		GwtEvent<ProgramsReloadEvent.ProgramsReloadHandler> {

	public static Type<ProgramsReloadHandler> TYPE = new Type<ProgramsReloadHandler>();

	public interface ProgramsReloadHandler extends EventHandler {
		void onProgramsReload(ProgramsReloadEvent event);
	}

	public ProgramsReloadEvent() {
	}

	@Override
	protected void dispatch(ProgramsReloadHandler handler) {
		handler.onProgramsReload(this);
	}

	@Override
	public Type<ProgramsReloadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ProgramsReloadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ProgramsReloadEvent());
	}
}
