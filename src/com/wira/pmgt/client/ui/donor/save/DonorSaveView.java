package com.wira.pmgt.client.ui.donor.save;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DonorSaveView extends Composite {

	private static DonorSaveViewUiBinder uiBinder = GWT
			.create(DonorSaveViewUiBinder.class);

	interface DonorSaveViewUiBinder extends UiBinder<Widget, DonorSaveView> {
	}

	public DonorSaveView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
