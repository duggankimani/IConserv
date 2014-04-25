package com.wira.pmgt.client.ui.component;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public abstract class RowWidget extends Composite {

	private boolean isAutoNumber;
	private int rowNum=0;
	HTMLPanel row;
	
	public void setRow(HTMLPanel row){
		this.row = row;
	}
	
	public void setRowNumber(int number){
		this.rowNum=number;
		if(isAttached())
		if(this.isAutoNumber){
			row.getWidget(0).getElement().setInnerText(number+"");
		}
	}
	
	public boolean isAutoNumber() {
		return isAutoNumber;
	}

	public void setAutoNumber(boolean isAutoNumber) {
		this.isAutoNumber = isAutoNumber;
	}
	
	public void createRow(List<Widget> widgets){
		if(isAutoNumber){
			row.add(getTd(new InlineLabel(rowNum+"")));
		}
		
		for(Widget widget: widgets){
			row.add(getTd(widget));
		}
	}
	
	private Widget getTd(Widget widget) {
		HTMLPanel td = new HTMLPanel("");
		td.addStyleName("td");
		td.add(widget);				
		return td;
	}

}
