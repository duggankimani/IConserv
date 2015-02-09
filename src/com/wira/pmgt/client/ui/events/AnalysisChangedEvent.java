package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.pmgt.client.ui.programs.AnalysisType;

public class AnalysisChangedEvent extends GwtEvent<AnalysisChangedEvent.AnalysisChangedHandler> {

	public static Type<AnalysisChangedHandler> TYPE = new Type<AnalysisChangedHandler>();
	private AnalysisType analysisType;

	public interface AnalysisChangedHandler extends EventHandler {
		void onAnalysisChanged(AnalysisChangedEvent event);
	}

	public AnalysisChangedEvent(AnalysisType analysisType) {
		this.analysisType = analysisType;
	}

	@Override
	protected void dispatch(AnalysisChangedHandler handler) {
		handler.onAnalysisChanged(this);
	}

	@Override
	public Type<AnalysisChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AnalysisChangedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source,AnalysisType analysisType) {
		source.fireEvent(new AnalysisChangedEvent(analysisType));
	}

	public AnalysisType getAnalysisType() {
		return analysisType;
	}
}
