package com.wira.pmgt.client.ui.assign;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.wira.pmgt.client.ui.component.autocomplete.AutoCompleteField;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.ParticipantType;

public class AssignActivityView extends ViewImpl implements
		AssignActivityPresenter.MyView {

	private final Widget widget;
	
	@UiField Anchor aAdd;
	@UiField AutoCompleteField<OrgEntity> allocatedToUsers;
	@UiField HTMLPanel divAllocations;
	
	@UiField Anchor aMessage;
	@UiField HTMLPanel divMessage;
	
	Boolean isShowMessage =false;

	public interface Binder extends UiBinder<Widget, AssignActivityView> {
	}

	List<OrgEntity> selectedSet = new ArrayList<OrgEntity>();
	
	@Inject
	public AssignActivityView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		selectedSet.add(AppContext.getContextUser());
				
		aAdd.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				List<OrgEntity> selected = allocatedToUsers.getSelectedItems();
				if(selected!=null && !selected.isEmpty()){
					addAllocations(selected);
				}
				allocatedToUsers.clearSelection();
			}
		});
		
		aMessage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(isShowMessage){
					//current state = show message
					//therefore negate
					divMessage.addStyleName("hidden");
					aMessage.setText("Add Message");
					isShowMessage=false;
				}else{
					//current state = hide message
					divMessage.removeStyleName("hidden");
					aMessage.setText("Discard Message");
					isShowMessage=true;
				}
				
			}
		});
	}
	
	protected void addAllocations(List<OrgEntity> entities) {
		divAllocations.clear();
		if(selectedSet.isEmpty()){
			entities.add(AppContext.getContextUser());
		}
		
		for(OrgEntity entity: entities){
			if(!selectedSet.contains(entity)){
				selectedSet.add(entity);
			}
		}
		
		//loop and create widgets
		for(OrgEntity entity:selectedSet){
			ParticipantType type = ParticipantType.ASSIGNEE;
			if(entity.equals(AppContext.getContextUser())){
				type = ParticipantType.INITIATOR;
			}
			final TaskAllocation allocation =new TaskAllocation(entity, type);
			divAllocations.add(allocation);
			
			allocation.getRemoveLink().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//event.getSource();
					OrgEntity entity = allocation.getOrgEntity();
					selectedSet.remove(entity);
					divAllocations.remove(allocation);
				}
			});
		}
	}

	/**
	 * Drop Down of Users & Groups for selection
	 */
	public void setSelection(List<OrgEntity> entities){
		allocatedToUsers.addItems(entities);
		
		addAllocations(new ArrayList<OrgEntity>());
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
