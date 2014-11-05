package com.wira.pmgt.client.ui.assign;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.ProgramTaskForm;
import com.wira.pmgt.shared.requests.GetProgramTaskForm;
import com.wira.pmgt.shared.responses.GetProgramTaskFormResponse;

public class FormAssign extends Composite {

	private static FormAssignUiBinder uiBinder = GWT
			.create(FormAssignUiBinder.class);
	
	@UiField Anchor aCreateForm;
	@UiField Anchor  aReload;
	@UiField HTMLPanel divAllocations;
	@UiField SpanElement spnRefresh;
	
	Long programId=null;
	

	interface FormAssignUiBinder extends UiBinder<Widget, FormAssign> {
	}

	public FormAssign() {
		initWidget(uiBinder.createAndBindUi(this));
		
		aReload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadTaskForms();
			}
		});
	}
	
	public void setActivityId(Long activityId, ProgramDetailType type) {
		aCreateForm.setHref("#adminhome;page=formbuilder;create=" + activityId);
		this.programId = activityId;
	}
	
	protected void setTaskForms(List<ProgramTaskForm> taskForms) {
		divAllocations.clear();
		for(ProgramTaskForm taskForm : taskForms){
			divAllocations.add(new TaskForm(taskForm));
		}
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		loadTaskForms();
	}
	
	
	public void loadTaskForms(){
		spnRefresh.setClassName("icon-spinner icon-spin");
		if(programId!=null){
			AppContext.getDispatcher().execute(new GetProgramTaskForm(programId), 
					new TaskServiceCallback<GetProgramTaskFormResponse>() {
				@Override
				public void processResult(
						GetProgramTaskFormResponse aResponse) {
					spnRefresh.setClassName("icon-refresh");
					setTaskForms(aResponse.getTaskForms());
				}
			});
			}
	}
}
