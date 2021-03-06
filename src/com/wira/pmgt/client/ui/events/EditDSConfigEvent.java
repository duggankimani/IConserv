package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.pmgt.shared.model.DSConfiguration;

public class EditDSConfigEvent extends
		GwtEvent<EditDSConfigEvent.EditDSConfigHandler> {

	public static Type<EditDSConfigHandler> TYPE = new Type<EditDSConfigHandler>();
	private DSConfiguration configuration;

	public interface EditDSConfigHandler extends EventHandler {
		void onEditDSConfig(EditDSConfigEvent event);
	}

	public EditDSConfigEvent(DSConfiguration configuration) {
		this.configuration = configuration;
	}

	public DSConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	protected void dispatch(EditDSConfigHandler handler) {
		handler.onEditDSConfig(this);
	}

	@Override
	public Type<EditDSConfigHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditDSConfigHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, DSConfiguration configuration) {
		source.fireEvent(new EditDSConfigEvent(configuration));
	}
}
