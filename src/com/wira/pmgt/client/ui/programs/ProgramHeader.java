package com.wira.pmgt.client.ui.programs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ProgramHeader extends Composite {

	private static ActivityHeaderUiBinder uiBinder = GWT
			.create(ActivityHeaderUiBinder.class);

	interface ActivityHeaderUiBinder extends UiBinder<Widget, ProgramHeader> {
	}

	public ProgramHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
