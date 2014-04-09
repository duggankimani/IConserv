package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;
import java.util.Map;

import com.google.gwt.event.shared.HasHandlers;
import com.wira.pmgt.shared.model.Actions;
import com.wira.pmgt.shared.model.Value;

public class ExecTaskEvent extends GwtEvent<ExecTaskEvent.ExecTaskHandler> {

	public static Type<ExecTaskHandler> TYPE = new Type<ExecTaskHandler>();
	private Long taskId;
	private Actions action;
	private Map<String, Value> values;
	
	public interface ExecTaskHandler extends EventHandler {
		void onExecTask(ExecTaskEvent event);
	}

	public ExecTaskEvent(Long taskId, Actions action) {
		this.taskId = taskId;
		this.action = action;
	}

	public Long getTaskId() {
		return taskId;
	}
	
	public Actions getAction(){
		return action;
	}

	@Override
	protected void dispatch(ExecTaskHandler handler) {
		handler.onExecTask(this);
	}

	@Override
	public Type<ExecTaskHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ExecTaskHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long taskId, Actions action) {
		source.fireEvent(new ExecTaskEvent(taskId, action));
	}

	public Map<String, Value> getValues() {
		return values;
	}

	public void setValues(Map<String, Value> values) {
		this.values = values;
	}
}
