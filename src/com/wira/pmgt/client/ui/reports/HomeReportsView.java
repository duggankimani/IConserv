package com.wira.pmgt.client.ui.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.admin.formbuilder.component.ColorWidget;
import com.wira.pmgt.client.ui.charts.PieChart;
import com.wira.pmgt.client.ui.component.TableView;
import com.wira.pmgt.client.ui.reports.Performance.PerformanceType;
import com.wira.pmgt.client.ui.util.NumberUtils;
import com.wira.pmgt.shared.model.dashboard.Data;
import com.wira.pmgt.shared.model.program.ProgramAnalysis;

public class HomeReportsView extends ViewImpl implements
		HomeReportsPresenter.MyView {

	public interface Binder extends UiBinder<Widget, HomeReportsView> {
	}

	private final Widget widget;

	@UiField SpanElement spnTotalFunding;
	@UiField SpanElement spnActual;
	@UiField SpanElement spnRemaining;
	
	@UiField
	TableView tableAnalysis;

	@UiField
	TableView tblBudgetAnalysis;

	@UiField
	PieChart pieChartBudget;
	// @UiField
	// PieChart pieChartConsumption;
	@UiField
	PieChart pieChartTimelines;
	@UiField
	PieChart pieChartTargets;

	@Inject
	public HomeReportsView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		tblBudgetAnalysis.setHeaders(Arrays.asList("Program Names", "Plan(Ksh)",
				"Actual(Ksh)", "Available(Ksh)"));

		tableAnalysis.setHeaders(Arrays.asList("PROGRAM NAME", "BUDGET",
				"MEETING TARGETS", "MEETING TIMELINES", "THROUGH PUT"));
		setData();
	}

	InlineLabel getInlineLabel(String amount, String color) {
		InlineLabel deficit = new InlineLabel(amount);
		if (!color.isEmpty())
			deficit.addStyleName(color);
		deficit.addStyleName("bold");
		return deficit;
	}

	InlineLabel getInlineLabel(String amount) {
		InlineLabel label = getInlineLabel(amount, "");
		return label;
	}

	public void setData() {
		String budgetMeasureTip = "Measure of activities accomplished within budgets";
		String budgetNoData = "Percentage of Activities without actual expenditure information";
		String targetMeasure = "Measure of activities that met their Targets";
		String targetNoData = "Percentage of Activities without actual outcome information";
		String timelinesMeasure = "Measure of ability to meet planned timelines.";
		String throughPut = "Average amount of documentation available compared to other programs";
		
		/* Other Analysis */
		tableAnalysis.addRow(
				new InlineLabel("Wildlife Program"),
				new ColorWidget(Arrays.asList(new Performance(budgetMeasureTip,
						80, PerformanceType.GOOD), new Performance(
						budgetNoData, 20, PerformanceType.NODATA))),
				new ColorWidget(Arrays.asList(new Performance(targetMeasure,
						55, PerformanceType.GOOD), new Performance(
						targetNoData, 45, PerformanceType.NODATA))),
				new ColorWidget(Arrays.asList(new Performance(timelinesMeasure,
						100, PerformanceType.GOOD, false))),
				new ColorWidget(Arrays.asList(new Performance(throughPut, 100,
						PerformanceType.AVERAGE))));

		tableAnalysis.addRow(
				new InlineLabel("Education & Ecoliteracy Program"),
				new ColorWidget(Arrays.asList(new Performance(budgetMeasureTip,
						60, PerformanceType.AVERAGE), new Performance(
						budgetNoData, 40, PerformanceType.NODATA))),
				new ColorWidget(Arrays.asList(new Performance(targetMeasure,
						78, PerformanceType.GOOD), new Performance(
						targetNoData, 23, PerformanceType.NODATA))),
				new ColorWidget(Arrays.asList(new Performance(timelinesMeasure,
						100, PerformanceType.POOR, false))),
				new ColorWidget(Arrays.asList(new Performance(throughPut, 100,
						PerformanceType.AVERAGE))));

		tableAnalysis.addRow(
				new InlineLabel("Forestry Program"),
				new ColorWidget(Arrays.asList(new Performance(budgetMeasureTip,
						36, PerformanceType.AVERAGE), new Performance(
						budgetNoData, 64, PerformanceType.NODATA))),
				new ColorWidget(Arrays.asList(new Performance(targetMeasure,
						78, PerformanceType.GOOD), new Performance(
						targetNoData, 23, PerformanceType.NODATA))),
				new ColorWidget(Arrays.asList(new Performance(timelinesMeasure,
						100, PerformanceType.AVERAGE, false))),
				new ColorWidget(Arrays.asList(new Performance(throughPut, 100,
						PerformanceType.AVERAGE))));


		pieChartTimelines.setData(Arrays.asList(new Data("Within deadlines",
				75, "60%"), new Data("Failed Deadlines", 25, "30%")));

		pieChartTargets.setData(Arrays.asList(new Data("Within Targets", 60,
				"60%"), new Data("Failed Targets", 30, "30%"), new Data(
				"No Data", 10, "10%")));

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void generate(List<ProgramAnalysis> list) {
		
		List<Data> pieChartData = new ArrayList<Data>();
		double totalFunding = 0.0;
		double totalActual = 0.0;
		double totalLeft = 0.0;
		
		for(ProgramAnalysis analysis: list){
			tblBudgetAnalysis.addRow(
					Arrays.asList("", "background-purple text-right", "text-right", "text-right"),
					new InlineLabel(analysis.getName()), new InlineLabel(formatShort(analysis.getBudgetAmount())),
					new InlineLabel(formatShort(analysis.getActual())),
					getInlineLabel(formatShort(analysis.getDiff()), analysis.isWithinBudget()? "text-success":"text-error"));
			
			totalFunding = totalFunding+analysis.getBudgetAmount();
			totalActual = totalActual+analysis.getActual();
			totalLeft = totalLeft+analysis.getDiff();
		}
		spnTotalFunding.setInnerText(format(totalFunding));
		spnActual.setInnerText(format(totalActual));
		spnRemaining.setInnerText(format(totalLeft));
		
		
		for(ProgramAnalysis analysis: list){
			if(analysis.getBudgetAmount()!=0){
				pieChartData.add(new Data(analysis.getName(), analysis.getBudgetAmount()/totalFunding,
						formatPerc(analysis.getBudgetAmount()*100/totalFunding)));
			}
		}
		pieChartBudget.setData(pieChartData);
	}

	private String formatPerc(Double d) {
		
		return d.intValue()+"%";
	}

	private String format(Double budgetAmount) {
		
		return NumberUtils.CURRENCYFORMAT.format(budgetAmount);
	}
	
	private String formatShort(Double budgetAmount) {
		
		return NumberUtils.NUMBERFORMAT.format(budgetAmount);
	}
}
