package com.wira.pmgt.client.ui.programs.tree;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.programs.tree.item.TreeWidgetItem;

public class TreeWidget extends Composite {
	
	
	private static TreeWidgetUiBinder uiBinder = GWT
			.create(TreeWidgetUiBinder.class);
	
	@UiField HTMLPanel divContainer;
	@UiField Tree treeComponent;

	interface TreeWidgetUiBinder extends UiBinder<Widget,TreeWidget> {
	}
	
	public TreeWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		
		TreeItem outerRoot = new TreeItem();
		outerRoot.setWidget(new TreeWidgetItem("Wildlife Program"));
		
		TreeItem innerRoot = new TreeItem();
		innerRoot.setWidget(new TreeWidgetItem("Outcome 1.2"));
		innerRoot.addItem(new TreeWidgetItem("Facilitate community meeting in each Unit.."));
	
		outerRoot.addItem(innerRoot);
		
		outerRoot.addItem(new TreeWidgetItem("Outcome 1.3"));
		
		treeComponent.addItem(outerRoot);
	}
	
}
