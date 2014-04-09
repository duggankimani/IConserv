package com.wira.pmgt.client.ui.admin.formbuilder;

import java.util.List;

import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.Property;

public interface HasProperties {

	/*Common Names*/
	public static final String NAME="NAME";
	public static final String CAPTION="CAPTION";
	public static final String HELP="HELP";
	public static final String MANDATORY="MANDATORY";
	public static final String SELECTIONTYPE="SELECTIONTYPE";
	public static final String READONLY="READONLY";
	public static final String PLACEHOLDER="PLACEHOLDER";
	public static final String ALIGNMENT="ALIGNMENT";
	public static final String CURRENCY="CURRENCY";
	public static final String SQLDS="SQLDS";
	public static final String SQLSELECT="SQLSELECT";
	public static final String FORMULA="FORMULA";
	void addProperty(Property property);
	
	String getPropertyValue(String propertyName);
	
	Object getValue(String propertyName);
	
	Value getFieldValue();
	
	List<Property> getProperties();
}
