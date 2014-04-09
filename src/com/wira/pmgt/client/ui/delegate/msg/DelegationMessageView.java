package com.wira.pmgt.client.ui.delegate.msg;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.TextArea;
import com.wira.pmgt.client.ui.component.TextField;
import com.wira.pmgt.shared.model.HTUser;

public class DelegationMessageView extends Composite {

	private static DelegationMessageViewUiBinder uiBinder = GWT
			.create(DelegationMessageViewUiBinder.class);

	interface DelegationMessageViewUiBinder extends
			UiBinder<Widget, DelegationMessageView> {
	}

	@UiField InlineLabel lblEmailTo;
	@UiField TextField txtSubject;
	@UiField TextArea commentBox;
	HTUser user;
	
	public DelegationMessageView(HTUser user, String subject) {
		initWidget(uiBinder.createAndBindUi(this));
		this.user = user;
		lblEmailTo.setText(user.getFullName()+"<"+user.getEmail()+">");
		txtSubject.setValue(subject);
		commentBox.setText("Please Take care of this task for me please. Thank you");
	}
	
	public String getMessage(){
		return commentBox.getValue();
	}

}
