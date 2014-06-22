package com.wira.pmgt.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.events.PropertyChangedEvent;
import com.wira.pmgt.client.ui.util.StringUtils;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.StringValue;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.KeyValuePair;
import com.wira.pmgt.shared.model.form.Property;

public class TextField extends FieldWidget {

	private static TextFieldUiBinder uiBinder = GWT
			.create(TextFieldUiBinder.class);

	interface TextFieldUiBinder extends UiBinder<Widget, TextField> {
	}

	@UiField Element lblEl;
	@UiField com.wira.pmgt.client.ui.component.TextField txtComponent;
	@UiField InlineLabel lblReadOnly;
	@UiField HTMLPanel panelControls;
	@UiField SpanElement spnMandatory;
	
	private final Widget widget;
	
	public TextField() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, id));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING, id));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(ALIGNMENT, "Alignment", DataType.SELECTBASIC, 
				new KeyValuePair("left", "Left"),
				new KeyValuePair("center", "Center"),
				new KeyValuePair("right", "Right")));
		
		//Bind UI
		widget = uiBinder.createAndBindUi(this);
		
		add(widget);
		//testing auto complete
		txtComponent.setAutoComplete(true);
		UIObject.setVisible(spnMandatory, false);
	}
	
	public TextField(final Property property) {
		this();
		lblEl.setInnerHTML(property.getCaption());
		txtComponent.setName(property.getName());
		
		Value textValue = property.getValue();
		String text = textValue==null? "": 
			textValue.getValue()==null? "": textValue.getValue().toString();
		
		txtComponent.setText(text);
		txtComponent.setClass("input-large"); //Smaller TextField
		designMode=true;
		
		final String name = property.getName();
		
		txtComponent.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String value  = event.getValue();
				Value previousValue = property.getValue();
				if(previousValue==null){
					previousValue = new StringValue();
				}else{
					if(event.getValue().equals(previousValue.getValue())){
						return;
					}
				}
				
				
				previousValue.setValue(value);
				property.setValue(previousValue);
				
				/**
				 * This allows visual properties including Caption, Place Holder, help to be 
				 * Synchronised with the form field, so that the changes are observed immediately
				 * 
				 * All other Properties need not be synched this way 
				 */
				if(name.equals(CAPTION) || name.equals(PLACEHOLDER) || name.equals(HELP)){				
					firePropertyChanged(property, value);
				}
				
			}
		});
		
		if(property.getName().equals(NAME)){
			addRegisteredHandler(PropertyChangedEvent.TYPE,
					new PropertyChangedEvent.PropertyChangedHandler(){
				@Override
				public void onPropertyChanged(PropertyChangedEvent event) {
					if(event.getPropertyName().equals(CAPTION)){
						String propertyValue = event.getPropertyValue()==null? null: event.getPropertyValue().toString();
						
						if(propertyValue==null || propertyValue.isEmpty()){
							return;
						}
						
						txtComponent.setValue(StringUtils.camelCase(propertyValue));
						Value value = property.getValue();
						if(value==null){
							value = new StringValue(null, NAME,propertyValue);
						}else{
							value.setValue(propertyValue);
						}
						
						property.setValue(value);
						
					}
				}
			});
		}
		//name.equals()
	}

	@Override
	public FieldWidget cloneWidget() {
		return new TextField();
	}
	
	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerHTML(caption);
	}
	
	@Override
	protected void setPlaceHolder(String placeHolder) {
		txtComponent.setPlaceholder(placeHolder);
	}
	
	@Override
	protected void setHelp(String help) {
		txtComponent.setTitle(help);
	}
	
	@Override
	protected DataType getType() {
		return DataType.STRING;
	}
	
	@Override
	public Value getFieldValue() {
		String value = txtComponent.getValue();
		
		if(value==null || value.isEmpty())
			return null;
		
		
		return new StringValue(field.getLastValueId(),field.getName(),value);
	}
	
	@Override
	public void setValue(Object value) {
		if(value!=null){
			if(!(value instanceof String)){
				value = value.toString();
			}
				
			txtComponent.setValue((String)value);
			lblReadOnly.setText((String)value);
		}else{
			txtComponent.setValue(null);
			lblReadOnly.setText(null);
		}
	}
	
	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		
		UIObject.setVisible(txtComponent.getElement(),!this.readOnly);
		UIObject.setVisible(lblReadOnly.getElement(), this.readOnly);
		
		UIObject.setVisible(spnMandatory, (!this.readOnly && isMandatory()));
	}

	@Override
	public Widget getComponent(boolean small) {
				
		if(!readOnly)
		if(small){
			txtComponent.setClass("input-medium");
		}
		return panelControls;
	}
	
	@Override
	protected void setAlignment(String alignment) {		
		txtComponent.getElement().getStyle().setTextAlign(TextAlign.valueOf(alignment.toUpperCase()));		
		panelControls.getElement().getStyle().setTextAlign(TextAlign.valueOf(alignment.toUpperCase()));
		
	}
	
}
