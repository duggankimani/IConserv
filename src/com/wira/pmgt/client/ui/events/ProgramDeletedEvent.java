package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;
import com.google.gwt.event.shared.HasHandlers;

public class ProgramDeletedEvent extends
		GwtEvent<ProgramDeletedEvent.ProgramDeletedHandler> {

	public static Type<ProgramDeletedHandler> TYPE = new Type<ProgramDeletedHandler>();
	private Long programId;

	public interface ProgramDeletedHandler extends EventHandler {
		void onProgramDeleted(ProgramDeletedEvent event);
	}

	public ProgramDeletedEvent(Long programId) {
		this.programId = programId;
	}

	public Long getProgramId() {
		return programId;
	}

	@Override
	protected void dispatch(ProgramDeletedHandler handler) {
		handler.onProgramDeleted(this);
	}

	@Override
	public Type<ProgramDeletedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ProgramDeletedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long programId) {
		source.fireEvent(new ProgramDeletedEvent(programId));
	}
}
