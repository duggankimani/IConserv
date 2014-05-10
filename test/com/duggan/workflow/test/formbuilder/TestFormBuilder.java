package com.duggan.workflow.test.formbuilder;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.wira.pmgt.server.dao.FormDaoImpl;
import com.wira.pmgt.server.dao.helper.FormDaoHelper;
import com.wira.pmgt.server.dao.model.ADField;
import com.wira.pmgt.server.dao.model.ADForm;
import com.wira.pmgt.server.dao.model.ADProperty;
import com.wira.pmgt.server.dao.model.ADValue;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.form.Field;
import com.wira.pmgt.shared.model.form.KeyValuePair;

public class TestFormBuilder {

	FormDaoImpl dao;
	
	@Before
	public void setup() {
		DBTrxProvider.init();
		DB.beginTransaction();
		dao = DB.getFormDao();
	}
	
	@Ignore
	public void getField(){
		Field field = FormDaoHelper.getField(108L);
		
		List<KeyValuePair> pairs = field.getSelectionValues(); 
		
		Assert.assertNotNull(pairs);
		
		Assert.assertEquals(pairs.size(),3);
		
		System.err.println("Values = "+pairs);
	}
	
	@Test
	public void getForm(){
		FormDaoHelper.getForm(1L, true);
	}
	
	@Ignore
	public void createForm(){
		ADForm form = new ADForm();
		//form.setId(5L);
		form.setCaption("Create Customer Form1");
		form.setName("customer.create");
		
		ADProperty property = new ADProperty();
		property.setCaption("Height");
		property.setType(DataType.STRING);
		property.setName("height");
		form.addProperty(property);
		
		ADValue value = new ADValue();
		value.setStringValue("200px");
		property.setValue(value);
		
		ADField field = new ADField();
		field.setCaption("Title");
		field.setName("title");
		
		ADValue fieldValue = new ADValue();
		fieldValue.setStringValue("Invoice no/ Unique doc Identifier");
		field.setValue(fieldValue);
		
		form.addField(field);
		
		dao.save(form);
		
		Assert.assertNotNull(form.getId());

	}
	
	@After
	public void tearDown() {
		//DB.rollback();
		DB.commitTransaction();
		DBTrxProvider.close();
	}
}
