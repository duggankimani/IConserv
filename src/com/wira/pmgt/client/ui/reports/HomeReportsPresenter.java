package com.wira.pmgt.client.ui.reports;

import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.component.Dropdown;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.program.Metric;
import com.wira.pmgt.shared.model.program.PerformanceModel;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramAnalysis;
import com.wira.pmgt.shared.requests.GetAnalysisDataRequest;
import com.wira.pmgt.shared.requests.GetPerformanceDataRequest;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetAnalysisDataResponse;
import com.wira.pmgt.shared.responses.GetPerformanceDataResponse;
import com.wira.pmgt.shared.responses.GetPeriodsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class HomeReportsPresenter extends
		PresenterWidget<HomeReportsPresenter.MyView> {

	public interface MyView extends View {

		void generate(List<ProgramAnalysis> list);

		void setAnalysis(List<PerformanceModel> budgetsPerfomance,
				List<PerformanceModel> targetsPerfomance,
				List<PerformanceModel> timelinesPerfomance,
				List<PerformanceModel> throughputPerfomance);

		void setPeriods(List<PeriodDTO> periods, Long periodId);
		
		Dropdown<PeriodDTO> getPeriodDropdown();

		void clear();
	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> PROGRAM_ANALYSIS = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> OVERALLTURNAROUND_SLOT = new Type<RevealContentHandler<?>>();
	
	@Inject DispatchAsync requestHelper;
	
	@Inject PlaceManager placeManager;
	
	@Inject
	public HomeReportsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getPeriodDropdown().addValueChangeHandler(new ValueChangeHandler<PeriodDTO>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PeriodDTO> event) {
				getView().clear();
				PeriodDTO period = event.getValue();
				PlaceRequest request = new PlaceRequest("home")
				.with("page", "reports")
				.with("period", period.getId()+"");
				
				placeManager.revealPlace(request);
				
			}
		});
	}
	
	public void loadData(final Long periodId){
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetPeriodsRequest());
		action.addRequest(new GetAnalysisDataRequest(periodId));
		action.addRequest(new GetPerformanceDataRequest(Metric.BUDGET,periodId));
		action.addRequest(new GetPerformanceDataRequest(Metric.TARGETS,periodId));
		action.addRequest(new GetPerformanceDataRequest(Metric.TIMELINES,periodId));
		action.addRequest(new GetPerformanceDataRequest(Metric.THROUGHPUT,periodId));
		
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				int i=0;
				
				getView().setPeriods(((GetPeriodsResponse)aResponse.getReponses().get(i++)).getPeriods(),periodId);
				
				List<ProgramAnalysis> list = ((GetAnalysisDataResponse)aResponse.get(i++)).getData();
				generateViews(list);
				
				List<PerformanceModel> budgetsPerfomance = ((GetPerformanceDataResponse)aResponse.get(i++)).getData();
				List<PerformanceModel> targetsPerfomance = ((GetPerformanceDataResponse)aResponse.get(i++)).getData();
				List<PerformanceModel> timelinesPerfomance = ((GetPerformanceDataResponse)aResponse.get(i++)).getData();
				List<PerformanceModel> throughputPerfomance = ((GetPerformanceDataResponse)aResponse.get(i++)).getData();
				
				
				getView().setAnalysis(budgetsPerfomance, targetsPerfomance, timelinesPerfomance, throughputPerfomance);
				
			}
		});
	}

	protected void generateViews(List<ProgramAnalysis> list) {
		getView().generate(list);
	}
}
