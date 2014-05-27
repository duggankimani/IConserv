package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class AppResizeEvent extends GwtEvent<AppResizeEvent.ResizeHandler> {

	public static Type<ResizeHandler> TYPE = new Type<ResizeHandler>();

	public interface ResizeHandler extends EventHandler {
		void onResize(AppResizeEvent event);
	}

	public AppResizeEvent() {
	}

	@Override
	protected void dispatch(ResizeHandler handler) {
		handler.onResize(this);
	}

	@Override
	public Type<ResizeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ResizeHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new AppResizeEvent());
	}
}
