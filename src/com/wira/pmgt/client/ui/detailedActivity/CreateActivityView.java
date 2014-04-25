package com.wira.pmgt.client.ui.detailedActivity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.grid.AggregationGrid;
import com.wira.pmgt.client.ui.component.grid.ColumnConfig;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.Listable;
import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class CreateActivityView extends ViewImpl implements
		CreateActivityPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateActivityView> {
	}

	IsProgramActivity Outcome; //The outcome under which this activity is created
	@UiField AggregationGrid budgetGrid;
	@UiField AggregationGrid targetGrid;
	@UiField AggregationGrid outputGrid;
	@UiField AggregationGrid indicatorGrid;
	
	@Inject
	public CreateActivityView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		createBudgetGrid();
		createIndicatorGrid();
		createOutputGrid();
		createTargetGrid();
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public IsProgramActivity getActivity() {
		return null;
	}
	
	public void createBudgetGrid(){
		budgetGrid.refresh();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		ColumnConfig budgetItem = new ColumnConfig("budgetItem", "Budget Item", DataType.STRING);
		configs.add(budgetItem);
		
		ColumnConfig config = new ColumnConfig("amount", "Amount", DataType.DOUBLE);
		config.setAggregationColumn(true);
		configs.add(config);
		
		budgetGrid.setColumnConfigs(configs);
		budgetGrid.setAutoNumber(true);
	}
	
	
	public void createTargetGrid(){
		targetGrid.refresh();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig measureItem = new ColumnConfig("measure", "Measure", DataType.STRING);
		configs.add(measureItem);
		
		ColumnConfig unitItem = new ColumnConfig("Units", "Units", DataType.DOUBLE);
		unitItem.setAggregationColumn(true);
		configs.add(unitItem);
		
		targetGrid.setColumnConfigs(configs);
		targetGrid.setAutoNumber(true);
	}
	
	public void createOutputGrid(){
		outputGrid.refresh();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig config = new ColumnConfig("measure", "Measure", DataType.STRING);
		configs.add(config);
		
		ColumnConfig unitItem = new ColumnConfig("Units", "Units", DataType.DOUBLE);
		unitItem.setAggregationColumn(true);
		configs.add(unitItem);
		
		outputGrid.setColumnConfigs(configs);
		outputGrid.setAutoNumber(true);
	}
	
	
	public void createIndicatorGrid(){
		indicatorGrid.refresh();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		

		ColumnConfig config = new ColumnConfig("measure", "Measure", DataType.STRING);
		configs.add(config);
		
		ColumnConfig unitItem = new ColumnConfig("Units", "Units", DataType.DOUBLE);
		unitItem.setAggregationColumn(true);
		configs.add(unitItem);
		
		indicatorGrid.setColumnConfigs(configs);
		indicatorGrid.setAutoNumber(true);
	}
	
	
}
