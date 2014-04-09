package com.wira.pmgt.client.ui.admin.formbuilder.component;

import java.util.List;

import com.wira.pmgt.shared.model.form.KeyValuePair;

public interface IsSelectionField {

	public void setSelectionValues(List<KeyValuePair> values);
	
	public List<KeyValuePair> getValues();
}
