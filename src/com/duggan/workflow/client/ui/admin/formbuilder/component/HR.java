package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class HR extends FieldWidget {

	private static HRUiBinder uiBinder = GWT.create(HRUiBinder.class);

	interface HRUiBinder extends UiBinder<Widget, HR> {
	}
	
	private final Widget widget;
	
	@UiField Element lblTitle;

	public HR() {
		super();
		widget = uiBinder.createAndBindUi(this);		
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new HR();
	}

	@Override
	protected DataType getType() {
		return DataType.LAYOUTHR;
	}

	@Override
	protected void setCaption(String caption) {
		lblTitle.setInnerText(caption);
	}
}