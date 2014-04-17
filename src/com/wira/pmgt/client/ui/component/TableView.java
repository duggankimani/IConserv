package com.wira.pmgt.client.ui.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.grid.AggregationGridRow;

public class TableView extends Composite {

	private static TableViewUiBinder uiBinder = GWT
			.create(TableViewUiBinder.class);

	interface TableViewUiBinder extends UiBinder<Widget, TableView> {
	}
	
	@UiField HTMLPanel panelHeader;
	@UiField HTMLPanel panelBody;
	@UiField HTMLPanel panelFooter;
	
	private boolean isAutoNumber=true;
	private int count=0;
	
	public TableView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setHeaders(List<String> names){
		List<Widget>widgets = new ArrayList<Widget>();
				
		for(String name: names){
			InlineLabel label = new InlineLabel(name);
			widgets.add(label);
		}
		
		setHeaderWidgets(widgets);
	}
	
	@Override
	public Widget asWidget() {
		return super.asWidget();
	}

	public void setHeaderWidgets(List<Widget> widgets) {

		if(isAutoNumber){
			InlineLabel label = new InlineLabel("#");
			widgets.add(0, label);
		}
		
		for(Widget widget: widgets){
			HTMLPanel th = new HTMLPanel("");
			th.setStyleName("th");
			th.add(widget);
			panelHeader.add(th);
		}
	}
	
	public void addRow(Widget ...widgets ){
		addRow(Arrays.asList(widgets));
	}

	public void addRow(List<Widget> widgets){
		HTMLPanel row = new HTMLPanel("");
		row.addStyleName("tr");
			
		if(isAutoNumber){
			row.add(getTd(new InlineLabel((++count)+"")));
		}
		
		for(Widget widget: widgets){
			row.add(getTd(widget));
		}
		panelBody.add(row);
	}
	
	public void addRow(AggregationGridRow rowWidget){
		rowWidget.setAutoNumber(isAutoNumber());
		rowWidget.setRowNumber(++count);
		panelBody.add(rowWidget);		
	}

	private Widget getTd(Widget widget) {
		HTMLPanel td = new HTMLPanel("");
		td.addStyleName("td");
		td.add(widget);				
		return td;
	}

	public void clearRows() {
		panelBody.clear();
	}

	public boolean isAutoNumber() {
		return isAutoNumber;
	}

	public void setAutoNumber(boolean isAutoNumber) {
		this.isAutoNumber = isAutoNumber;
	}
	
	public void setFooter(List<Widget> widgets){
		panelFooter.clear();
		HTMLPanel row = new HTMLPanel("");
		row.addStyleName("tr");
			
		if(isAutoNumber){
			row.add(getTd(new InlineLabel()));
		}
		
		for(Widget widget: widgets){
			row.add(getTd(widget));
		}
		panelFooter.add(row);
	}
}
