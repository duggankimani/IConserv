package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CreateProgramEvent extends
		GwtEvent<CreateProgramEvent.CreateDocumentHandler> {

	public static Type<CreateDocumentHandler> TYPE = new Type<CreateDocumentHandler>();
	private Long programId;

	public interface CreateDocumentHandler extends EventHandler {
		void onCreateDocument(CreateProgramEvent event);
	}

	public CreateProgramEvent(Long progId) {
		this.setProgramId(progId);
	}


	@Override
	protected void dispatch(CreateDocumentHandler handler) {
		handler.onCreateDocument(this);
	}

	@Override
	public Type<CreateDocumentHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CreateDocumentHandler> getType() {
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
}
