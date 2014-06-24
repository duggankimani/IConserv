package com.wira.pmgt.client.ui.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.wira.pmgt.client.ui.admin.formbuilder.component.ColorLabel;
import com.wira.pmgt.client.ui.charts.PieChart;
import com.wira.pmgt.client.ui.component.TableView;
import com.wira.pmgt.shared.model.dashboard.Data;

public class HomeReportsView extends ViewImpl implements
		HomeReportsPresenter.MyView {

	public interface Binder extends UiBinder<Widget, HomeReportsView> {
	}
	private final Widget widget;
	
	@UiField
	TableView tableAnalysis;
	
	@UiField PieChart pieChartBudget;
	@UiField PieChart pieChartConsumption;
	@UiField PieChart pieChartTimelines;
	@UiField PieChart pieChartTargets;
	
	@Inject
	public HomeReportsView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		tableAnalysis.setHeaders(Arrays.asList("PROGRAM NAME", "BUDGET",
				"MEETING TIMELINES", "THROUGH PUT"));
		setData();
	}

	public void setData() {
		tableAnalysis.addRow(new InlineLabel("Wildlife Program"),
							 new ColorLabel(70.0, 10.0, "success", "default"), 
							 new ColorLabel(70.0, 10.0, "success", "info"),
							 new ColorLabel(70.0, 10.0, "success", "info"));
		tableAnalysis.addRow(new InlineLabel("Wildlife Program"),
							new ColorLabel(70.0, 10.0, "success", "default"), 
							new ColorLabel(70.0, 10.0, "success", "info"),
							new ColorLabel(70.0, 10.0, "success", "info"));
		tableAnalysis.addRow(new InlineLabel("Wildlife Program"),
							 new ColorLabel(70.0, 10.0, "success", "default"), 
							 new ColorLabel(70.0, 10.0, "success"),
							 new ColorLabel(70.0, 10.0, "success", "info"));								
		tableAnalysis.addRow(new InlineLabel("Wildlife Program"),
							 new ColorLabel(70.0, 10.0, "success", "default"), 
							 new ColorLabel(70.0, 10.0, "success", "info"),
							 new ColorLabel(70.0, 10.0, "success", "info"));		
		
		pieChartBudget.setData(Arrays.asList(new Data("Within Budget", 60, "Ksh 6,000,000"),
				new Data("Exceed Budget", 30, "Ksh 3,000,000"),
				new Data("No Data", 10, "Ksh 1,000,000")));
		
		pieChartConsumption.setData(Arrays.asList(new Data("Available Budget", 50000000, "60%"),
				new Data("Consumed Budget", 45000000, "30%"),
				new Data("No Data", 5000000, "10%")));
		
		pieChartTimelines.setData(Arrays.asList(new Data("Within deadlines", 75, "60%"),
				new Data("Outside Deadlines", 25, "30%")));
		
		pieChartTargets.setData(Arrays.asList(new Data("Within Targets", 60, "60%"),
				new Data("Outside Targets", 30, "30%"),
				new Data("No Data", 10, "10%")));
																		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
