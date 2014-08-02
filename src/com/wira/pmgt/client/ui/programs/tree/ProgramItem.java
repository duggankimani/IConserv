package com.wira.pmgt.client.ui.programs.tree;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TreeItem;
import com.wira.pmgt.client.ui.events.MoveTargetSelectedEvent;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.ProgramTreeModel;

public class ProgramItem extends TreeItem{

	private ProgramTreeModel model;
	ProgramItemView item;
	public ProgramItem(ProgramTreeModel model) {
		this.model = model;
		item = new ProgramItemView(model);
		setWidget(item);
		item.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				AppContext.fireEvent(new MoveTargetSelectedEvent(ProgramItem.this,event.getValue()));
			}
		});
	}

	public void enableMoveFor(ProgramDetailType typeToMove) {
		item.enableMoveFor(typeToMove);
	}

	public void setItemSelected(boolean selected) {
		item.setSelected(selected);
	}

	public ProgramTreeModel getModel() {
		return model;
	}
}
