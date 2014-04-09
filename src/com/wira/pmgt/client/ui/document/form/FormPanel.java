package com.wira.pmgt.client.ui.document.form;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LegendElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.model.MODE;
import com.wira.pmgt.client.ui.admin.formbuilder.HasProperties;
import com.wira.pmgt.client.ui.admin.formbuilder.component.FieldWidget;
import com.wira.pmgt.client.ui.admin.formbuilder.component.TextArea;
import com.wira.pmgt.client.ui.component.IssuesPanel;
import com.wira.pmgt.client.ui.delegate.FormDelegate;
import com.wira.pmgt.client.util.ENV;
import com.wira.pmgt.shared.model.StringValue;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.Field;
import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.model.form.FormModel;
import com.wira.pmgt.shared.model.form.Property;

/**
 * Runtime form
 * 
 * @author duggan
 *
 */
public class FormPanel extends Composite {

	private static FormPanelUiBinder uiBinder = GWT
			.create(FormPanelUiBinder.class);

	interface FormPanelUiBinder extends UiBinder<Widget, FormPanel> {
	}

	@UiField HTMLPanel panelFields;
	//@UiField InlineLabel panelLabel;
	@UiField HTMLPanel panelItem;
	@UiField LegendElement divFormCaption;
	@UiField SpanElement divFormHelp;
	@UiField IssuesPanel issues;
	
	FormDelegate formDelegate = new FormDelegate();
	MODE mode = MODE.VIEW;
	
	public FormPanel(Form form){		
		this(form, MODE.VIEW);
	}
	
	public FormPanel(Form form,MODE mode) {
		initWidget(uiBinder.createAndBindUi(this));
		this.mode = mode;
		ENV.clear();
		
		form.getCaption();
		divFormHelp.setInnerText("");
		if(form.getProperties()!=null)
		for(Property prop: form.getProperties()){
			if(prop.getName()!=null){
				if(prop.getName().equals(HasProperties.CAPTION)){
					Value val = prop.getValue();
					if(val!=null){
						divFormCaption.setInnerText(((StringValue)val).getValue());
					}
					
				}
				if(prop.getName().equals(HasProperties.HELP)){
					Value val = prop.getValue();
					if(val!=null){
						divFormHelp.setInnerText(((StringValue)val).getValue());
					}
					
				}
			}
		}
		
		List<Field> fields = form.getFields();
		Collections.sort(fields, new Comparator<FormModel>() {
			public int compare(FormModel o1, FormModel o2) {
				Field field1 = (Field)o1;
				Field field2 = (Field)o2;
				
				Integer pos1 = field1.getPosition();
				Integer pos2 = field2.getPosition();
				
				return pos1.compareTo(pos2);
			};
			
		});
		
		
		for(Field field: fields){
			FieldWidget fieldWidget = FieldWidget.getWidget(field.getType(), field, false);
			if(mode==MODE.VIEW){
				//set read only 
				fieldWidget.setReadOnly(true);
			}
			
			if(fieldWidget instanceof TextArea){
				((TextArea) fieldWidget).getContainer().removeStyleName("hidden");
			}
			
			//System.err.println("||| "+field.getCaption()+" :: "+
			//(field.getValue()==null? "null" : field.getValue().getValue()));
			panelFields.add(fieldWidget);
		}
		
	}

	public boolean isValid(){
		boolean isValid = formDelegate.isValid(issues, panelFields);;
		
		if(!isValid){
			issues.getElement().scrollIntoView();
		}
		return isValid;
	}
	
	public Map<String, Value> getValues(){
		return formDelegate.getValues(panelFields);		
	}
	
	public void setReadOnly(boolean readOnly){
		formDelegate.setReadOnly(readOnly, (ComplexPanel)panelFields);
	}
}
