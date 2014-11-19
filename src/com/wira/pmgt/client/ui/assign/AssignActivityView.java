package com.wira.pmgt.client.ui.assign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.tabs.TabContent;
import com.wira.pmgt.client.ui.component.tabs.TabHeader;
import com.wira.pmgt.client.ui.component.tabs.TabPanel;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.PermissionType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class AssignActivityView extends ViewImpl implements
		AssignActivityPresenter.MyView {

	private final Widget widget;

	@UiField
	BulletListPanel crumbContainer;
	@UiField
	TabPanel divTabs;

	private UserAssign assignWidget;
	private FormAssign formWidget;

	public interface Binder extends UiBinder<Widget, AssignActivityView> {
	}

	@Inject
	public AssignActivityView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		assignWidget = new UserAssign();
		formWidget = new FormAssign();

		assignWidget.getaddButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAllItems();
			}
		});

		assignWidget.getSelectedSet().add(AppContext.getContextUser());

		setTabPanel();
	}

	public void setTabPanel() {
		divTabs.setHeaders(Arrays.asList(new TabHeader(
				"Allocated User(s)", true, "users"), new TabHeader(
				"Allocated Form(s)", false, "forms")));

		divTabs.setContent(Arrays.asList(
							new TabContent(assignWidget,"users",true),
							new TabContent(formWidget,"forms",false)));

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void clear() {
		assignWidget.clear();
	}

	@Override
	public TaskInfo getTaskInfo() {
		TaskInfo taskInfo = new TaskInfo();
		int count = assignWidget.getDivAllocations().getWidgetCount();
		for (int i = 0; i < count; i++) {
			TaskAllocation allocation = (TaskAllocation) assignWidget
					.getDivAllocations().getWidget(i);
			taskInfo.addParticipant(allocation.getOrgEntity(),
					allocation.getParticipantType());
		}

		taskInfo.setMessage("Kindly take care of this task, Thank you.");

		if (!assignWidget.getMessage().isEmpty())
			taskInfo.setMessage(assignWidget.getMessage());

		return taskInfo;
	}

	@Override
	public void setActivityId(Long activityId, ProgramDetailType type) {
		assignWidget.setActivityId(activityId, type);
		formWidget.setActivityId(activityId, type);
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
		Map<PermissionType, List<OrgEntity>> participants = taskInfo
				.getParticipants();
		if (!participants.isEmpty()) {
			for (PermissionType type : participants.keySet()) {
				List<OrgEntity> entities = participants.get(type);
				for (OrgEntity entity : entities) {
					if (!assignWidget.getSelectedSet().contains(entity)) {
						assignWidget.getSelectedSet().add(entity);
						assignWidget.createTaskAllocation(entity, type,true);
					}

				}
			}
		}
	}

	/**
	 * Drop Down of Users & Groups for selection
	 */
	@Override
	public void setSelection(List<OrgEntity> entities) {
		assignWidget.getAllocatedToUsers().addItems(entities);
		assignWidget.addAllocations(new ArrayList<OrgEntity>());
	}

	@Override
	public void addAllItems() {
		List<OrgEntity> selected = assignWidget.getAllocatedToUsers()
				.getSelectedItems();
		if (selected != null && !selected.isEmpty()) {
			assignWidget.addAllocations(selected);
		}
		assignWidget.getAllocatedToUsers().clearSelection();
	}

}
