package com.wira.pmgt.client.ui.component.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.TableView;

public class AggregationGrid extends Composite {

	private static AggregationGridUiBinder uiBinder = GWT
			.create(AggregationGridUiBinder.class);

	interface AggregationGridUiBinder extends UiBinder<Widget, AggregationGrid> {
	}
	
	@UiField TableView tblView;
	@UiField SpanElement spnAggregate;
	
	Map<String, Number> summaries = new HashMap<String, Number>();

	List<ColumnConfig> columnConfigs = null;
	
	public AggregationGrid() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public AggregationGrid(List<ColumnConfig> configs){
		this();
		setColumnConfigs(configs);
	}	

	private void createHeaders(List<ColumnConfig> configs) {
		this.columnConfigs = configs;
		List<Widget> widgets = new ArrayList<Widget>();
		
		if(configs!=null)
		for(ColumnConfig config: configs){
			widgets.add(config.createHeaderWidget());
		}	
		tblView.setHeaderWidgets(widgets);
		createFooter();
	}

	public void setData(List<DataModel> models){
		for(DataModel row: models){
			addRowData(row);
		}
		createFooter();
	}
	
	public void addRowData(DataModel data){
		AggregationGridRow row = new AggregationGridRow(this, data, columnConfigs);	
		tblView.addRow(row);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addValueChangeHandler(ColumnConfig config, Widget widget) {
		Object value = ((HasValue)widget).getValue();
		Number initial= 0.0;
		if(value!=null){
			initial = (Number)value;
		}
		HasValueChangeHandlers hasValueChangeHandlers = (HasValueChangeHandlers)widget;
		hasValueChangeHandlers.addValueChangeHandler(
				new OnAggregationFieldChangedHandler(this,config,initial));
	}

	public void aggregate(ColumnConfig column, Number previous, Number newValue) {
		String key = column.getKey();
		Number sum = summaries.get(key);
		if(sum==null){
			sum=0.0;
		}		
		
		sum = sum.doubleValue()-previous.doubleValue()+newValue.doubleValue();
		summaries.put(key, sum);
		createFooter();		
	}

	public void createFooter() {
		List<Widget> widgets = new ArrayList<Widget>();
		
		if(columnConfigs!=null)
		for(ColumnConfig config: columnConfigs){
			if(config.isAggregationColumn()){
				
				Number value = summaries.get(config.getKey());
				widgets.add(new SummaryRenderer(value));
				
			}else{
				widgets.add(new InlineLabel());
			}
			
		}	
		tblView.setFooter(widgets);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createHeaders(columnConfigs);
	}
	
	public List<ColumnConfig> getColumnConfigs() {
		return columnConfigs;
	}

	public void setColumnConfigs(List<ColumnConfig> columnConfigs) {
		this.columnConfigs= columnConfigs;
	}
	
	public void setAutoNumber(boolean enable){
		tblView.setAutoNumber(enable);
	}
	
	public List<DataModel> getData(){
		List<DataModel> models = new ArrayList<DataModel>();
		int rows = tblView.getRowCount();
		if(rows>0){
			for(int row=0; row<rows; row++){
				Widget rowWidget = tblView.getRow(row);
				if(rowWidget instanceof AggregationGridRow){
					AggregationGridRow r = (AggregationGridRow)rowWidget;
					models.add(r.getData());	
				}
			}
		}
		
		return models;
	}
	
	class SummaryRenderer extends HTMLPanel{

		InlineLabel label = new InlineLabel();
		public SummaryRenderer(Object value) {
			super("");
			this.add(label);
			
			String text = "";
			if(value instanceof Number){
				text = NumberFormat.getCurrencyFormat().format((Number) value);
			}else if(value!=null){
				text = value.toString();
			}
			
			label.setText(text);
			this.getElement().getStyle().setTextAlign(TextAlign.RIGHT);
		}
				
	}
	
}
