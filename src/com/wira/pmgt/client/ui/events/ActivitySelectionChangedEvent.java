package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.pmgt.shared.model.program.IsProgramDetail;

public class ActivitySelectionChangedEvent extends
		GwtEvent<ActivitySelectionChangedEvent.ActivitySelectionChangedHandler> {

	public static Type<ActivitySelectionChangedHandler> TYPE = new Type<ActivitySelectionChangedHandler>();
	private IsProgramDetail programActivity;
	private boolean isSelected;

	public interface ActivitySelectionChangedHandler extends EventHandler {
		void onActivitySelectionChanged(ActivitySelectionChangedEvent event);
	}

	public ActivitySelectionChangedEvent(IsProgramDetail programActivity,
			Boolean isSelected) {
		this.programActivity = programActivity;
		this.isSelected = isSelected;
	}

	public IsProgramDetail getProgramActivity() {
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

	public static void fire(HasHandlers source, IsProgramDetail programActivity,
			Boolean isSelected) {
		source.fireEvent(new ActivitySelectionChangedEvent(programActivity,
				isSelected));
	}
}
