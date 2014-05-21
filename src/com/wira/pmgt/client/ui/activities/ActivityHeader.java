package com.wira.pmgt.client.ui.activities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ActivityHeader extends Composite {

	private static ActivityHeaderUiBinder uiBinder = GWT
			.create(ActivityHeaderUiBinder.class);

	interface ActivityHeaderUiBinder extends UiBinder<Widget, ActivityHeader> {
	}

	public ActivityHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
