package com.wira.pmgt.client.ui.assign;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.shared.model.ProgramDetailType;

public class FormAssign extends Composite {

	private static FormAssignUiBinder uiBinder = GWT
			.create(FormAssignUiBinder.class);
	
	@UiField
	Anchor aCreateForm;

	interface FormAssignUiBinder extends UiBinder<Widget, FormAssign> {
	}

	public FormAssign() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setActivityId(Long activityId, ProgramDetailType type) {
		aCreateForm.setHref("#adminhome;page=formbuilder;create=" + activityId);
	}

}
