package com.duggan.workflow.test.formbuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.dao.FormDaoHelper;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.Property;

public class TestFormBuilderHelper {

	
	
	@Before
	public void setup() {
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	

	@Test
	public void createForm(){
		Form form = new Form();
		form.setCaption("Caption Test");
		form.setFields(getFields());
		form.setProperties(getProperties(null, null));
		form.setName("Test Form");
		
		form = FormDaoHelper.createForm(form, true);
		
		Assert.assertNotNull(form.getId());
		Assert.assertNotNull(form.getCaption());
		Assert.assertNotNull(form.getName());
		
		Assert.assertNotNull(form.getFields());
		Assert.assertEquals(1, form.getFields().size());
		
		Field field = form.getFields().get(0);
		Assert.assertNotNull(field.getId());
		Assert.assertNotNull(field.getCaption());
		Assert.assertNotNull(field.getFormId());
		Assert.assertNotNull(field.getType());
		Assert.assertNotNull(field.getValue());
		Assert.assertEquals(2, field.getProperties().size());
		
		//Field Properties
		Property prop = field.getProperties().get(0);
		Assert.assertNotNull(prop.getCaption());
		Assert.assertNotNull(prop.getFieldId());
		//Assert.assertNotNull(prop.getFormId());
		Assert.assertNotNull(prop.getId());
		Assert.assertNotNull(prop.getName());
		Assert.assertNotNull(prop.getType());
		Assert.assertNotNull(prop.getValue());
		
		
		//From Properties
		
		Assert.assertEquals(2, form.getProperties().size());
		Property prop1 = form.getProperties().get(0);
		Assert.assertNotNull(prop1.getId());
		Assert.assertNotNull(prop1.getCaption());
		//Assert.assertNotNull(prop1.getFieldId());
		Assert.assertNotNull(prop1.getFormId());
		Assert.assertNotNull(prop1.getName());
		Assert.assertNotNull(prop1.getType());
		Assert.assertNotNull(prop1.getValue());
		
		//createField(form);
	}
	
	public void createField(Form form){
		
		Field field = new Field();
		field.setCaption("Test Field");
		field.setFormId(form.getId());
		field.setId(null);
		field.setName("Test");
		field.setProperties(getProperties(null, null));
		field.setType(DataType.DATE);
		field.setValue(new DateValue(null, "", new Date()));
		
		field = FormDaoHelper.createField(field);
		
		Assert.assertNotNull(field.getId());
		Assert.assertNotNull(field.getCaption());
		Assert.assertNotNull(field.getFormId());
		Assert.assertNotNull(field.getType());
		Assert.assertNotNull(field.getValue());
		
	}
		
	private List<Property> getProperties(Long formId, Long fieldId) {
		
		List<Property> properties = new ArrayList<>();
		
		Property prop = new Property();
		prop.setCaption("Mama Mia");
		prop.setFieldId(fieldId);
		prop.setFormId(formId);
		prop.setId(null);
		prop.setName("Mama Mia");
		prop.setType(DataType.STRING);
		prop.setValue(new StringValue(null, "", "Mama Mia, this is awesome"));
		properties.add(prop);
		
		Property prop2 = new Property();
		prop2.setCaption("Test Prop");
		prop2.setFieldId(fieldId);
		prop2.setFormId(formId);
		prop2.setId(null);
		prop2.setName("Test Me");
		prop2.setType(DataType.STRING);
		prop2.setValue(new StringValue(null, "", "Testing is awesome"));
		properties.add(prop2);
		
		return properties;
	}


	private List<Field> getFields() {
		
		List<Field> fields = new ArrayList<>();
		
		Field field = new Field();
		field.setCaption("Test Field");
		field.setFormId(null);
		field.setId(null);
		field.setName("Test");
		field.setProperties(getProperties(null, null));
		field.setType(DataType.DATE);
		field.setValue(new DateValue(null, "", new Date()));
		fields.add(field);
		
		return fields;
	}


	@After
	public void tearDown() {
		DB.rollback();
		//DB.commitTransaction();
		DBTrxProvider.close();
	}
}