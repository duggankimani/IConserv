package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.client.ui.util.DocMode;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;

import javax.print.Doc;

import com.google.gwt.event.shared.HasHandlers;

public class DocumentSelectionEvent extends
		GwtEvent<DocumentSelectionEvent.DocumentSelectionHandler> {

	public static Type<DocumentSelectionHandler> TYPE = new Type<DocumentSelectionHandler>();
	private Long documentId;
	private DocMode mode;

	public interface DocumentSelectionHandler extends EventHandler {
		void onDocumentSelection(DocumentSelectionEvent event);
	}

	public DocumentSelectionEvent(Long documentId, DocMode docMode) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	public DocMode getDocMode(){
		return mode;
	}

	@Override
	protected void dispatch(DocumentSelectionHandler handler) {
		handler.onDocumentSelection(this);
	}

	@Override
	public Type<DocumentSelectionHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DocumentSelectionHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long documentId, DocMode docMode) {
		source.fireEvent(new DocumentSelectionEvent(documentId, docMode));
	}
}
