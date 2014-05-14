package com.wira.pmgt.client.ui.assign;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.TaskInfo;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.requests.GetGroupsRequest;
import com.wira.pmgt.shared.requests.GetUsersRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetGroupsResponse;
import com.wira.pmgt.shared.responses.GetUsersResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class AssignActivityPresenter extends
		PresenterWidget<AssignActivityPresenter.MyView> {

	public interface MyView extends View {
		void setSelection(List<OrgEntity> entities);

		void clear();

		TaskInfo getTaskInfo();
		
		void setActivityId(Long activityId);

		void addAllItems();
	}

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public AssignActivityPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	/*
	 * Add Items the Autocomplete List
	 */
	public void addItems() {
		getView().addAllItems();
	}
	
	public void load(Long activityId){
		getView().setActivityId(activityId);
		getView().clear();
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetUsersRequest());
		action.addRequest(new GetGroupsRequest());

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						int i = 0;
						List<OrgEntity> entities = new ArrayList<OrgEntity>();
						GetUsersResponse getUsers = (GetUsersResponse) aResponse
								.get(i++);
						for (HTUser user : getUsers.getUsers()) {
							entities.add(user);
						}

						GetGroupsResponse getGroups = (GetGroupsResponse) aResponse
								.get(i++);
						for (UserGroup group : getGroups.getGroups()) {
							entities.add(group);
						}

						getView().setSelection(entities);

					}

				});
	}

	public TaskInfo getTaskInfo() {
		return getView().getTaskInfo();
	}
}
