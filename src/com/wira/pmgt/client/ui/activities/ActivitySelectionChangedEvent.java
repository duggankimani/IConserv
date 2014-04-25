package com.wira.pmgt.client.ui.activities;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import java.lang.Boolean;
import com.google.gwt.event.shared.HasHandlers;

public class ActivitySelectionChangedEvent extends
		GwtEvent<ActivitySelectionChangedEvent.ActivitySelectionChangedHandler> {

	public static Type<ActivitySelectionChangedHandler> TYPE = new Type<ActivitySelectionChangedHandler>();
	private IsProgramActivity programActivity;
	private boolean isSelected;

	public interface ActivitySelectionChangedHandler extends EventHandler {
		void onActivitySelectionChanged(ActivitySelectionChangedEvent event);
	}

	public ActivitySelectionChangedEvent(IsProgramActivity programActivity,
			Boolean isSelected) {
		this.programActivity = programActivity;
		this.isSelected = isSelected;
	}

	public IsProgramActivity getProgramActivity() {
		return programActivity;
	}

	public boolean isSelected() {
		return isSelected;
	}

	@Override
	protected void dispatch(ActivitySelectionChangedHandler handler) {
		handler.onActivitySelectionChanged(this);
	}

	@Override
	public Type<ActivitySelectionChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ActivitySelectionChangedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, IsProgramActivity programActivity,
			Boolean isSelected) {
		source.fireEvent(new ActivitySelectionChangedEvent(programActivity,
				isSelected));
	}
}
