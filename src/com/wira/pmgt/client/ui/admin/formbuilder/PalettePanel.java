package com.wira.pmgt.client.ui.admin.formbuilder;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.admin.formbuilder.component.FieldWidget;

public class PalettePanel extends VerticalPanel {

	private PickupDragController dragController;

	public PalettePanel() {
		
		addStyleName("demo-PalettePanel");
		setSpacing(2);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}

	public void registerDragController(PickupDragController controller) {
		this.dragController = controller;
		for (int i = 0; i < this.getWidgetCount(); i++) {
			controller.makeDraggable(this.getWidget(i));
		}

	}

	/**
	 * Overloaded method that makes widgets draggable.
	 * 
	 * @param w
	 *  the widget to be added are made draggable
	 */
	public void add(FieldWidget w) {
		if(dragController!=null){
			dragController.makeDraggable(w.asWidget());
		}
		
		super.add(w);
	}

	/**
	 * Removed widgets that are instances of {@link PaletteWidget} are
	 * immediately replaced with a cloned copy of the original.
	 * 
	 * @param w
	 *            the widget to remove
	 * @return true if a widget was removed
	 */
	@Override
	public boolean remove(Widget w) {
		int index = getWidgetIndex(w);
		if (index != -1 && w instanceof FieldWidget) {
			FieldWidget clone = ((FieldWidget) w).cloneWidget();
			dragController.makeDraggable(clone);
			insert(clone, index);
		}
		return super.remove(w);
	}
}