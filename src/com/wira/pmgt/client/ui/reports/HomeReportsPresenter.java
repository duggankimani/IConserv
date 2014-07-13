package com.wira.pmgt.client.ui.reports;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.program.ProgramAnalysis;
import com.wira.pmgt.shared.requests.GetAnalysisDataRequest;
import com.wira.pmgt.shared.responses.GetAnalysisDataResponse;

public class HomeReportsPresenter extends
		PresenterWidget<HomeReportsPresenter.MyView> {

	public interface MyView extends View {

		void generate(List<ProgramAnalysis> list);
	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> PROGRAM_ANALYSIS = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> OVERALLTURNAROUND_SLOT = new Type<RevealContentHandler<?>>();
	
	@Inject DispatchAsync requestHelper;
	
	@Inject
	public HomeReportsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	public void loadData(){
		requestHelper.execute(new GetAnalysisDataRequest(null),
				new TaskServiceCallback<GetAnalysisDataResponse>() {
			@Override
			public void processResult(GetAnalysisDataResponse aResponse) {
				List<ProgramAnalysis> list = aResponse.getData();
				generateViews(list);
			}
		});
	}

	protected void generateViews(List<ProgramAnalysis> list) {
		getView().generate(list);
	}
}
