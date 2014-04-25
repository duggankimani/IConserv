package com.wira.pmgt.client.ui.component.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.RowWidget;

public class AggregationGridRow extends RowWidget{

	private static AggrationGridRowUiBinder uiBinder = GWT
			.create(AggrationGridRowUiBinder.class);

	interface AggrationGridRowUiBinder extends
			UiBinder<Widget, AggregationGridRow> {
	}

	@UiField HTMLPanel row;
	
	private Long modelId;
	private DataModel model;
	private List<ColumnConfig> configs = null;
	private Map<ColumnConfig, HasValue> columnWigetMap = new HashMap<ColumnConfig, HasValue>();
	
	private AggregationGrid grid;
	private boolean isChanged=false;
	
	public AggregationGridRow() {
		initWidget(uiBinder.createAndBindUi(this));
		setRow(row);
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
		
		return model;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

}
