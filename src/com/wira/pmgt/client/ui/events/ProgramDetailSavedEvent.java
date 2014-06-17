package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.google.gwt.event.shared.HasHandlers;

public class ProgramDetailSavedEvent extends
		GwtEvent<ProgramDetailSavedEvent.ProgramDetailSavedHandler> {

	public static Type<ProgramDetailSavedHandler> TYPE = new Type<ProgramDetailSavedHandler>();
	private IsProgramDetail program;
	private boolean isNew;

	public interface ProgramDetailSavedHandler extends EventHandler {
		void onProgramDetailSaved(ProgramDetailSavedEvent event);
	}

	public ProgramDetailSavedEvent(IsProgramDetail program, boolean isNew) {
		this.program = program;
		this.isNew = isNew;
	}

	public IsProgramDetail getProgram() {
		return program;
	}

	public boolean isNew() {
		return isNew;
	}

	@Override
	protected void dispatch(ProgramDetailSavedHandler handler) {
		handler.onProgramDetailSaved(this);
	}

	@Override
	public Type<ProgramDetailSavedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ProgramDetailSavedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, IsProgramDetail program,
			boolean isNew) {
		source.fireEvent(new ProgramDetailSavedEvent(program, isNew));
	}
}
