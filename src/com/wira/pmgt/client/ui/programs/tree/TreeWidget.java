package com.wira.pmgt.client.ui.programs.tree;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.events.MoveTargetSelectedEvent;
import com.wira.pmgt.client.ui.events.MoveTargetSelectedEvent.MoveTargetSelectedHandler;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.ProgramTreeModel;

public class TreeWidget extends Composite implements MoveTargetSelectedHandler{
	
	
	private static TreeWidgetUiBinder uiBinder = GWT
			.create(TreeWidgetUiBinder.class);
	
	@UiField HTMLPanel divContainer;
	@UiField Tree treeComponent;

	private ProgramDetailType typeToMove;

	interface TreeWidgetUiBinder extends UiBinder<Widget,TreeWidget> {
	}
	
	ProgramItem selected=null;
	
	public TreeWidget(ProgramDetailType typeToMove, List<ProgramTreeModel> models) {
		this.typeToMove = typeToMove;
		initWidget(uiBinder.createAndBindUi(this));
		
		for(ProgramTreeModel model: models){
			ProgramItem root = new ProgramItem(model);
			addItems(root, model.getChildren());
			treeComponent.addItem(root);
		}
		
	}

	private void addItems(ProgramItem root, List<ProgramTreeModel> children) {
		
		if(children==null){
			return;
		}
		for(ProgramTreeModel child: children){
			ProgramItem item = new ProgramItem(child);
			item.enableMoveFor(typeToMove);
			addItems(item, child.getChildren());
			root.addItem(item);
		}
	}

	@Override
	public void onMoveTargetSelected(MoveTargetSelectedEvent event) {
		if(selected!=null){
			selected.setItemSelected(false);
		}		
		if(event.isSelected()){
			selected =event.getItem();
			selected.setItemSelected(true);
		}else{
			selected=null;
		}
	}
	
	public ProgramTreeModel getSelectedTargetModel(){
		if(selected==null){
			return null;
		}
		return selected.getModel();
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		addRegisteredHandler(MoveTargetSelectedEvent.TYPE, this);
	}
	
	/**
	 * 
	 * @param type
	 * @param handler
	 */
	public <H extends EventHandler> void addRegisteredHandler(Type<H> type, H handler){
		@SuppressWarnings("unchecked")
		HandlerRegistration hr = AppContext.getEventBus().addHandler(
				(GwtEvent.Type<EventHandler>)type, handler);
		handlers.add(hr);
	}
	
	/**
	 * 
	 */
	private void cleanUpEvents() {
		for(HandlerRegistration hr: handlers){
			hr.removeHandler();
		}
		handlers.clear();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		cleanUpEvents();
	}
	
	List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();
	
}
