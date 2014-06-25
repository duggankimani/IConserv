package com.wira.pmgt.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.StarRating;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.DoubleValue;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.Field;
import com.wira.pmgt.shared.model.form.Property;

public class RatingField extends FieldWidget {

	private static RatingFieldUiBinder uiBinder = GWT
			.create(RatingFieldUiBinder.class);

	interface RatingFieldUiBinder extends UiBinder<Widget, RatingField> {
	}

	@UiField Element lblEl;
	@UiField StarRating ratingComponent;
	@UiField InlineLabel lblReadOnly;
	@UiField HTMLPanel panelControls;
	
	private final Widget widget;
	
	public RatingField() {
		super();
		addProperty(new Property(MAXVALUE, "Max Value", DataType.INTEGER));
		
		widget = uiBinder.createAndBindUi(this);
		add(widget);
		ratingComponent.setStyleName("rating");
		ratingComponent.setMaxValue(5);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new RatingField();
	}

	@Override
	public void setField(Field field) {
		super.setField(field);
		Property maxValueProp = field.getProperty(MAXVALUE);
		if(maxValueProp!=null){
			Value value = maxValueProp.getValue();
			if(value!=null && value.getValue()!=null){
				Integer val= Integer.parseInt(value.getValue().toString());
				ratingComponent.setMaxValue(val);
			}
		}
	}
	
	@Override
	protected DataType getType() {
		return DataType.RATING;
	}
	
	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerHTML(caption);
	}
	
	@Override
	protected void setHelp(String help) {
		ratingComponent.setTitle(help);
	}
	
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		
//		UIObject.setVisible(ratingComponent.getElement(),!this.readOnly);
//		UIObject.setVisible(lblReadOnly.getElement(), this.readOnly);
		
	}
	
	@Override
	public Widget getComponent(boolean small) {
				
		if(!readOnly)
		if(small){
			//ratingComponent.setSty("input-medium");
		}
		return panelControls;
	}
	
	@Override
	public Value getFieldValue() {
		Integer value = ratingComponent.getValue();
		
		if(value==null)
			return null;
		
		assert value!=0;
		return new DoubleValue(field.getLastValueId(), field.getName(),new Double(value));
	}
	
	@Override
	public void setValue(Object value) {
		if(value!=null){
			System.err.println("Rating: >> "+value);
			if(!(value instanceof Double)){
				try{
					value = new Double(value.toString());
				}catch(Exception e){return;}
			}
			
			ratingComponent.setValue(((Double)value).intValue());
		}

	}

}
