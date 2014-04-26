package com.wira.pmgt.client.ui.component.grid;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.DoubleField;
import com.wira.pmgt.client.ui.component.DropDownList;
import com.wira.pmgt.client.ui.component.IntegerField;
import com.wira.pmgt.client.ui.component.TextArea;
import com.wira.pmgt.client.ui.component.TextField;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.Listable;

public class ColumnConfig {

	private String key;
	private String displayName;
	private boolean isAggregationColumn;
	private DataType type;
	private List<Listable> dropDownItems = new ArrayList<Listable>();
	
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
		if(type==DataType.INTEGER){
			IntegerField field= new IntegerField();
			 //field.setStyleName("input-medium");
			 widget = field;
		}else if(type==DataType.DOUBLE){
			 DoubleField field= new DoubleField();
			 //field.setStyleName("input-medium");
			 widget = field;
		}else if(type==DataType.SELECTBASIC){
			DropDownList dropDown = new DropDownList();
			dropDown.setItems(dropDownItems);
			widget=dropDown;
		}else if(type==DataType.STRINGLONG){
			widget = new TextArea();
		}
		else{
			widget = new TextField();
			
		}
		
		widget.setValue(value);		
		return (Widget)widget;
	}
	
	public void setDropDownItems(List<Listable> items){
		this.dropDownItems.clear();
		this.dropDownItems.addAll(items);
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
