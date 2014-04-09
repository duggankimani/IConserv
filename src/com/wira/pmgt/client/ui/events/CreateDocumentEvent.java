package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.pmgt.shared.model.DocumentType;

public class CreateDocumentEvent extends
		GwtEvent<CreateDocumentEvent.CreateDocumentHandler> {

	public static Type<CreateDocumentHandler> TYPE = new Type<CreateDocumentHandler>();
	private DocumentType docType;

	public interface CreateDocumentHandler extends EventHandler {
		void onCreateDocument(CreateDocumentEvent event);
	}

	public CreateDocumentEvent(DocumentType docType) {
		this.docType = docType;
	}

	public DocumentType getDocType() {
		return docType;
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

	public static void fire(HasHandlers source, DocumentType docType) {
		source.fireEvent(new CreateDocumentEvent(docType));
	}
}
