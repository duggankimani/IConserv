package com.wira.pmgt.client.ui.component.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class AggregationGridRow extends Composite {

	private static AggrationGridRowUiBinder uiBinder = GWT
			.create(AggrationGridRowUiBinder.class);

	interface AggrationGridRowUiBinder extends
			UiBinder<Widget, AggregationGridRow> {
	}

	@UiField HTMLPanel row;
	
	private boolean isAutoNumber;
	private Long modelId;
	private DataModel model;
	private List<ColumnConfig> configs = null;
	private Map<ColumnConfig, HasValue> columnWigetMap = new HashMap<ColumnConfig, HasValue>();
	
	private AggregationGrid grid;
	private int rowNum=0;
	private boolean isChanged=false;
	
	public AggregationGridRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public AggregationGridRow(AggregationGrid aggregationGrid, DataModel data,
			List<ColumnConfig> columnConfigs) {
		this();
		this.configs = columnConfigs;
		this.grid = aggregationGrid;
		this.model = data;
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		List<Widget> widgets = new ArrayList<Widget>();
		
		if(configs!=null)
		for(ColumnConfig config: configs){
			Widget widget = config.createWidget(model.get(config.getKey()));
			columnWigetMap.put(config, (HasValue)widget);
			if(config.isAggregationColumn()){
				grid.addValueChangeHandler(config, widget);
			}
			widgets.add(widget);
		}
		
		createRow(widgets);
	}

	public void createRow(List<Widget> widgets){
		if(isAutoNumber){
			row.add(getTd(new InlineLabel(rowNum+"")));
		}
		
		for(Widget widget: widgets){
			row.add(getTd(widget));
		}
	}
	
	public void setRowNumber(int number){
		this.rowNum=number;
		
		if(isAttached())
		if(this.isAutoNumber){
			((InlineLabel)row.getWidget(0)).setText(number+"");
		}
	}
	
	private Widget getTd(Widget widget) {
		HTMLPanel td = new HTMLPanel("");
		td.addStyleName("td");
		td.add(widget);				
		return td;
	}

	public boolean isAutoNumber() {
		return isAutoNumber;
	}

	public void setAutoNumber(boolean isAutoNumber) {
		this.isAutoNumber = isAutoNumber;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public DataModel getModel() {
		return model;
	}

	public void setModel(DataModel model) {
		
		this.model = model;
	}

	public DataModel getData(){
		if(model==null){
			model = new DataModel();
		}
		
		for(ColumnConfig column: columnWigetMap.keySet()){
			String key = column.getKey();
			model.set(key, columnWigetMap.get(column).getValue());
		}
		
		return getData();
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

}
