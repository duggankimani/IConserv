package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.pmgt.client.ui.programs.tree.ProgramItem;

public class MoveTargetSelectedEvent extends
		GwtEvent<MoveTargetSelectedEvent.MoveTargetSelectedHandler> {

	public static Type<MoveTargetSelectedHandler> TYPE = new Type<MoveTargetSelectedHandler>();
	private boolean selected;
	private ProgramItem item;

	public interface MoveTargetSelectedHandler extends EventHandler {
		void onMoveTargetSelected(MoveTargetSelectedEvent event);
	}

	public MoveTargetSelectedEvent(ProgramItem item, boolean selected) {
		this.item = item;
		this.selected = selected;
	}

	@Override
	protected void dispatch(MoveTargetSelectedHandler handler) {
		handler.onMoveTargetSelected(this);
	}

	@Override
	public Type<MoveTargetSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MoveTargetSelectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source,ProgramItem item, boolean selected) {
		source.fireEvent(new MoveTargetSelectedEvent(item,selected));
	}

	public boolean isSelected() {
		return selected;
	}

	public ProgramItem getItem() {
		return item;
	}
}
