package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.pmgt.shared.model.program.IsProgramDetail;

public class MoveProgramEvent extends
		GwtEvent<MoveProgramEvent.MoveProgramHandler> {

	public static Type<MoveProgramHandler> TYPE = new Type<MoveProgramHandler>();
	private Long previousParentId;
	private Long newParentId;
	private IsProgramDetail itemMoved;

	public interface MoveProgramHandler extends EventHandler {
		void onMoveProgram(MoveProgramEvent event);
	}

	public MoveProgramEvent(IsProgramDetail itemMoved, Long previousParentId, Long newParentId) {
		this.itemMoved = itemMoved;
		this.previousParentId = previousParentId;
		this.newParentId = newParentId;
	}

	@Override
	protected void dispatch(MoveProgramHandler handler) {
		handler.onMoveProgram(this);
	}

	@Override
	public Type<MoveProgramHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MoveProgramHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, IsProgramDetail itemMoved, Long previousParentId, Long newParentId) {
		source.fireEvent(new MoveProgramEvent(itemMoved, previousParentId, newParentId));
	}

	public Long getPreviousParentId() {
		return previousParentId;
	}

	public Long getNewParentId() {
		return newParentId;
	}

	public IsProgramDetail getItemMoved() {
		return itemMoved;
	}
}
