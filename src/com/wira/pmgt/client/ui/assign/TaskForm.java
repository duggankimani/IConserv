package com.wira.pmgt.client.ui.assign;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.shared.model.program.ProgramTaskForm;

public class TaskForm extends Composite {

	private static TaskFormUiBinder uiBinder = GWT
			.create(TaskFormUiBinder.class);

	interface TaskFormUiBinder extends UiBinder<Widget, TaskForm> {
	}

	@UiField Anchor aFormLink;
	@UiField SpanElement spnName;
	ProgramTaskForm taskForm;
	
	public TaskForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public TaskForm(ProgramTaskForm taskForm) {
		this();
		this.taskForm = taskForm;
		spnName.setInnerText(taskForm.getFormName());
		aFormLink.setHref("#adminhome;page=formbuilder;formid="+taskForm.getFormId());
		
	}

}
