package com.wira.pmgt.client.ui.admin.formbuilder.component;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.wira.pmgt.client.ui.admin.formbuilder.FormBuilderPresenter;

public class DragHandlerImpl implements DragHandler {
	
	private Widget widget;
	
	@Inject FormBuilderPresenter formbuilder;
	
	public DragHandlerImpl(Widget widget){
		this.widget = widget;
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		//System.err.println("DragEnd >>> "+event.getContext().draggable.getClass());
		Widget draggable=event.getContext().draggable;
		
		if(draggable instanceof FieldWidget)
			((FieldWidget)draggable).activatePopup();
	}

	@Override
	public void onDragStart(DragStartEvent event) {
//		System.err.println("Drag start");
	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
//		System.err.println("Preview Drag End");
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
//		System.err.println("Preview Drag Start");
	}

}
