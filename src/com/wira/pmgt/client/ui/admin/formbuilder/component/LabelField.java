package com.wira.pmgt.client.ui.admin.formbuilder.component;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.shared.model.DataType;

public class LabelField extends FieldWidget {

	private static LabelFieldUiBinder uiBinder = GWT
			.create(LabelFieldUiBinder.class);

	private final Widget widget;
	
	interface LabelFieldUiBinder extends UiBinder<Widget, LabelField> {
	}


	@UiField Element lblEl;
	@UiField InlineLabel lblComponent;
	
	public LabelField() {
		super();
		
		widget = uiBinder.createAndBindUi(this);
		add(widget);
		
		lblComponent.getElement().setAttribute("type","text");
	}

	@Override
	public FieldWidget cloneWidget() {
		return new LabelField();
	}

	@Override
	protected DataType getType() {
		return DataType.LABEL;
	}
	
	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerText(caption);
	}
	
	@Override
	public void setValue(Object value) {
		//System.err.println(">>>>>>"+field.getName()+" : "+field.getCaption()+" :: "+value);
		if(value==null){
			lblComponent.setText("");
			return;
		}
		
		if(value instanceof String){
			lblComponent.setText(value.toString());
		}else if(value instanceof Date){
			lblComponent.setText(DateUtils.DATEFORMAT.format((Date)value));
		}else{
			lblComponent.setText(value.toString());
		}
		
	}
	
	@Override
	public void setTitle(String title) {
		if(title!=null)
			lblEl.setTitle(title);
	}
	
	@Override
	public boolean isMandatory() {
		return false;
	}
	
	@Override
	public boolean isReadOnly() {
		return true;
	}
	
	public Widget getComponent() {
		return lblComponent;
	}
}
