package com.wira.pmgt.client.ui.programs.tree.item;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class TreeWidgetItem extends Composite {
	
	@UiField InlineLabel aName;

	private static TreeWidgetItemUiBinder uiBinder = GWT
			.create(TreeWidgetItemUiBinder.class);

	interface TreeWidgetItemUiBinder extends UiBinder<Widget, TreeWidgetItem> {
	}

	public TreeWidgetItem() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public TreeWidgetItem(String name){
		this();
		aName.setText(name);
	}

}
