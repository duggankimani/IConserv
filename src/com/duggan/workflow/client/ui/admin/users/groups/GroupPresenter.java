package com.duggan.workflow.client.ui.admin.users.groups;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.EditGroupEvent;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.requests.SaveGroupRequest;
import com.duggan.workflow.shared.responses.SaveGroupResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class GroupPresenter extends PresenterWidget<GroupPresenter.MyView>{

	public interface MyView extends View {
		void setValues(String code, String name);
		
		HasClickHandlers getEdit();
		
		HasClickHandlers getDelete();
	}

	UserGroup group;
	
	@Inject DispatchAsync requestHelper;
	
	@Inject
	public GroupPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getEdit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new EditGroupEvent(group));
			}
		});
		
		getView().getDelete().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				SaveGroupRequest request = new SaveGroupRequest(group);
				request.setDelete(true);
				requestHelper.execute(request, new TaskServiceCallback<SaveGroupResponse>() {
					@Override
					public void processResult(SaveGroupResponse result) {
						getView().asWidget().removeFromParent();
					}
				});
			}
		});
	}
	
	public void setGroup(UserGroup group){
		this.group = group;
		getView().setValues(group.getName(), group.getFullName());
	}

}
