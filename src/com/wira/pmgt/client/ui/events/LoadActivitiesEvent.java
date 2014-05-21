package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;
import com.google.gwt.event.shared.HasHandlers;

public class LoadActivitiesEvent extends
		GwtEvent<LoadActivitiesEvent.LoadActivitiesHandler> {

	public static Type<LoadActivitiesHandler> TYPE = new Type<LoadActivitiesHandler>();
	private Long documentId;

	public interface LoadActivitiesHandler extends EventHandler {
		void onLoadActivities(LoadActivitiesEvent event);
	}

	public LoadActivitiesEvent(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	@Override
	protected void dispatch(LoadActivitiesHandler handler) {
		handler.onLoadActivities(this);
	}

	@Override
	public Type<LoadActivitiesHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadActivitiesHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long documentId) {
		source.fireEvent(new LoadActivitiesEvent(documentId));
	}
}
