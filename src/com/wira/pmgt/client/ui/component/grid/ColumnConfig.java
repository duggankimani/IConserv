package com.wira.pmgt.client.ui.component.grid;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.DoubleField;
import com.wira.pmgt.client.ui.component.TextField;
import com.wira.pmgt.shared.model.DataType;

public class ColumnConfig {

	private String key;
	private String displayName;
	private boolean isAggregationColumn;
	private DataType type;
	
	public ColumnConfig(String key, String displayName, DataType type){
		this.key = key;
		this.displayName = displayName;
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Widget createWidget(Object value){
	
		HasValue widget = null;
		if(type==DataType.DOUBLE){
			 DoubleField field= new DoubleField();
			 widget = field;
		}else{
			widget = new TextField();
		}
		
		widget.setValue(value);		
		return (Widget)widget;
	}

	public Widget createHeaderWidget() {		
		return new InlineLabel(displayName);
	}

	public boolean isAggregationColumn() {
		return isAggregationColumn;
	}

	public void setAggregationColumn(boolean isAggregationColumn) {
		this.isAggregationColumn = isAggregationColumn;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}
	
}
