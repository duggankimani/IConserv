package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CreateProgramEvent extends
		GwtEvent<CreateProgramEvent.CreateProgramHandler> {

	public static Type<CreateProgramHandler> TYPE = new Type<CreateProgramHandler>();
	private Long programId;
	private boolean navigateOnSave;

	public interface CreateProgramHandler extends EventHandler {
		void onCreateProgram(CreateProgramEvent event);
	}

	public CreateProgramEvent(Long progId) {
		this.setProgramId(progId);
	}

	public CreateProgramEvent(Long progId, boolean navigateOnSave) {
		this.setProgramId(progId);
		this.navigateOnSave = navigateOnSave;
	}

	@Override
	protected void dispatch(CreateProgramHandler handler) {
		handler.onCreateProgram(this);
	}

	@Override
	public Type<CreateProgramHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CreateProgramHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long progId) {
		source.fireEvent(new CreateProgramEvent(progId));
	}


	public Long getProgramId() {
		return programId;
	}


	public void setProgramId(Long programId) {
		this.programId = programId;
	}


	public boolean isNavigateOnSave() {
		return navigateOnSave;
	}
}
