package com.wira.pmgt.client.ui.assign;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.autocomplete.AutoCompleteField;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.ParticipantType;
import com.wira.pmgt.shared.model.ProgramDetailType;

public class UserAssign extends Composite {

	private static UserAssignUiBinder uiBinder = GWT
			.create(UserAssignUiBinder.class);

	@UiField
	Anchor aAdd;
	@UiField
	AutoCompleteField<OrgEntity> allocatedToUsers;

	@UiField
	HTMLPanel divAllocations;

	@UiField
	Anchor aMessage;
	@UiField
	HTMLPanel divMessage;
	@UiField
	TextArea txtMessage;
	
	List<OrgEntity> selectedSet = new ArrayList<OrgEntity>();
	
	private ProgramDetailType detailType;
	
	Boolean isShowMessage = false;

	interface UserAssignUiBinder extends UiBinder<Widget, UserAssign> {
	}

	public UserAssign() {
		initWidget(uiBinder.createAndBindUi(this));

		
		aMessage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (isShowMessage) {
					divMessage.addStyleName("hidden");
					aMessage.setText("Add Message");
					isShowMessage = false;
				} else {
					// current state = hide message
					divMessage.removeStyleName("hidden");
					aMessage.setText("Discard Message");
					isShowMessage = true;
				}

			}
		});
	}

	protected void addAllocations(List<OrgEntity> entities) {

		// Nothing previously selected
		if (selectedSet.isEmpty()) {
			entities.add(AppContext.getContextUser());
		}

		for (OrgEntity entity : entities) {
			if (!selectedSet.contains(entity)) {
				selectedSet.add(entity);
				ParticipantType type = detailType == ProgramDetailType.PROGRAM ? ParticipantType.STAKEHOLDER
						: ParticipantType.ASSIGNEE;

				if (entity.equals(AppContext.getContextUser())) {
					type = ParticipantType.INITIATOR;
				}
				createTaskAllocation(entity, type);
			}
		}

		// loop and create widgets
		// for (OrgEntity entity : selectedSet) {
		// ParticipantType type = ParticipantType.ASSIGNEE;
		// if (entity.equals(AppContext.getContextUser())) {
		// type = ParticipantType.INITIATOR;
		// }
		//
		// createTaskAllocation(entity,type);
		// }
	}
	
	
	public void createTaskAllocation(OrgEntity entity, ParticipantType type) {

		final TaskAllocation allocation = new TaskAllocation(detailType,entity, type);
		divAllocations.add(allocation);

		allocation.getRemoveLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// event.getSource();
				OrgEntity entity = allocation.getOrgEntity();
				selectedSet.remove(entity);
				divAllocations.remove(allocation);
			}
		});
	}
	
	
	public HTMLPanel getDivAllocations() {
		return divAllocations;
	}
	
	public void clear() {
		selectedSet.clear();
		allocatedToUsers.clearSelection();
		divAllocations.clear();
	}
	
	public String getMessage(){
		return txtMessage.getValue();
	}
	
	public AutoCompleteField<OrgEntity> getAllocatedToUsers() {
		return allocatedToUsers;
	}
	
	public List<OrgEntity> getSelectedSet() {
		return selectedSet;
	}
	
	public HasClickHandlers getaddButton() {
		return aAdd;
	}

	public void setActivityId(Long activityId, ProgramDetailType type) {
		this.detailType = type;
	}

}
