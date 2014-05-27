package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class CommentSaveEvent extends
		GwtEvent<CommentSaveEvent.CommentSaveHandler> {

	public static Type<CommentSaveHandler> TYPE = new Type<CommentSaveHandler>();

	public interface CommentSaveHandler extends EventHandler {
		void onCommentSave(CommentSaveEvent event);
	}

	public CommentSaveEvent() {
	}

	@Override
	protected void dispatch(CommentSaveHandler handler) {
		handler.onCommentSave(this);
	}

	@Override
	public Type<CommentSaveHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CommentSaveHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CommentSaveEvent());
	}
}
