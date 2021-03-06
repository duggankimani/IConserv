package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.pmgt.shared.model.form.FormModel;

public class SavePropertiesEvent extends
		GwtEvent<SavePropertiesEvent.SavePropertiesHandler> {

	public static Type<SavePropertiesHandler> TYPE = new Type<SavePropertiesHandler>();
	private FormModel parent;

	public interface SavePropertiesHandler extends EventHandler {
		void onSaveProperties(SavePropertiesEvent event);
	}

	public SavePropertiesEvent(FormModel parent) {
		this.parent = parent;
	}

	public FormModel getParent() {
		return parent;
	}

	@Override
	protected void dispatch(SavePropertiesHandler handler) {
		handler.onSaveProperties(this);
	}

	@Override
	public Type<SavePropertiesHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SavePropertiesHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, FormModel parent) {
		source.fireEvent(new SavePropertiesEvent(parent));
	}
}
