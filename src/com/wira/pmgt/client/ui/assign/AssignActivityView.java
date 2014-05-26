package com.wira.pmgt.client.ui.assign;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.autocomplete.AutoCompleteField;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.ParticipantType;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class AssignActivityView extends ViewImpl implements
		AssignActivityPresenter.MyView {

	private final Widget widget;

	@UiField
	Anchor aAdd;
	@UiField
	Anchor aCreateForm;

	@UiField
	AutoCompleteField<OrgEntity> allocatedToUsers;

	@UiField
	BulletListPanel crumbContainer;

	@UiField
	HTMLPanel divAllocations;

	@UiField
	Anchor aMessage;
	@UiField
	HTMLPanel divMessage;
	@UiField
	TextArea txtMessage;

	Boolean isShowMessage = false;

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
				addAllItems();
			}
		});

		aMessage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (isShowMessage) {
					// current state = show message
					// therefore negate
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

	public void addAllItems() {
		List<OrgEntity> selected = allocatedToUsers.getSelectedItems();
		if (selected != null && !selected.isEmpty()) {
			addAllocations(selected);
		}
		allocatedToUsers.clearSelection();
	}

	protected void addAllocations(List<OrgEntity> entities) {
		
		//Nothing previously selected
		if (selectedSet.isEmpty()) {
			entities.add(AppContext.getContextUser());
		}

		for (OrgEntity entity : entities) {
			if (!selectedSet.contains(entity)) {
				selectedSet.add(entity);
				ParticipantType type = ParticipantType.STAKEHOLDER;
				if (entity.equals(AppContext.getContextUser())) {
					type = ParticipantType.INITIATOR;
				}
				createTaskAllocation(entity,type);
			}
		}

		// loop and create widgets
//		for (OrgEntity entity : selectedSet) {
//			ParticipantType type = ParticipantType.ASSIGNEE;
//			if (entity.equals(AppContext.getContextUser())) {
//				type = ParticipantType.INITIATOR;
//			}
//			
//			createTaskAllocation(entity,type);
//		}
	}

	private void createTaskAllocation(OrgEntity entity, ParticipantType type) {

		final TaskAllocation allocation = new TaskAllocation(entity, type);
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

	/**
	 * Drop Down of Users & Groups for selection
	 */
	public void setSelection(List<OrgEntity> entities) {
		allocatedToUsers.addItems(entities);

		addAllocations(new ArrayList<OrgEntity>());
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void clear() {
		selectedSet.clear();
		allocatedToUsers.clearSelection();
		divAllocations.clear();
	}

	@Override
	public TaskInfo getTaskInfo() {
		TaskInfo taskInfo = new TaskInfo();
		int count = divAllocations.getWidgetCount();
		for (int i = 0; i < count; i++) {
			TaskAllocation allocation = (TaskAllocation) divAllocations
					.getWidget(i);
			taskInfo.addParticipant(allocation.getOrgEntity(),
					allocation.getParticipantType());
		}

		taskInfo.setMessage("Kindly take care of this task, Thank you.");

		if (!txtMessage.getValue().isEmpty())
			taskInfo.setMessage(txtMessage.getValue());

		return taskInfo;
	}

	@Override
	public void setActivityId(Long activityId) {
		aCreateForm.setHref("#adminhome;page=formbuilder;create=" + activityId);
	}

	public void createCrumb(String text, String title, Long id, Boolean isActive) {
		BreadCrumbItem crumb = new BreadCrumbItem();
		crumb.setActive(isActive);
		crumb.setTitle(title);
		if (text.length() > 25) {
			text = text.substring(0, 25) + "...";
		}
		crumb.setLinkText(text);
		crumb.setHref("#home;page=activities;activity=" + id);
		crumbContainer.add(crumb);
	}

	public void setBreadCrumbs(List<ProgramSummary> summaries) {
		crumbContainer.clear();
		for (int i = summaries.size() - 1; i > -1; i--) {
			ProgramSummary summary = summaries.get(i);
			createCrumb(summary.getName(), summary.getDescription(),
					summary.getId(), i == 0);
		}

	}

	@Override
	public void setTaskInfo(TaskInfo taskInfo) {
		Map<ParticipantType, List<OrgEntity>> participants = taskInfo.getParticipants();
		if(!participants.isEmpty()){
			for(ParticipantType type: participants.keySet()){
				List<OrgEntity> entities = participants.get(type);
				for(OrgEntity entity: entities){
					if (!selectedSet.contains(entity)){
						selectedSet.add(entity);
						createTaskAllocation(entity, type);
					}
					
				}
			}
		}
	}

}
