package com.wira.pmgt.client.ui.document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.TextArea;

public class ConfirmAction extends Composite {

	private static ComfirmActionUiBinder uiBinder = GWT
			.create(ComfirmActionUiBinder.class);

	interface ComfirmActionUiBinder extends UiBinder<Widget, ConfirmAction> {
	}

	@UiField InlineLabel spnConfirm;
	@UiField TextArea txtComment;
	
	public ConfirmAction(String msg, String placeHolder) {
		initWidget(uiBinder.createAndBindUi(this));
		spnConfirm.setText(msg);
		txtComment.setPlaceholder(placeHolder);
	}
	
	public String getComment(){
		return txtComment.getValue();
	}
}
