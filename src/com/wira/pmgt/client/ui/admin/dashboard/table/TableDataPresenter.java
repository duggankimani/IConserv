package com.wira.pmgt.client.ui.admin.dashboard.table;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.dashboard.ChartType;
import com.wira.pmgt.shared.model.dashboard.LongTask;
import com.wira.pmgt.shared.requests.GetLongTasksRequest;
import com.wira.pmgt.shared.responses.GetLongTasksResponse;

public class TableDataPresenter extends
		PresenterWidget<TableDataPresenter.ITableDataView> {

	public interface ITableDataView extends View {

		void setTasks(List<LongTask> longTasks);
	}
	
	@Inject DispatchAsync requestHelper;
	ChartType type;
	
	boolean loaded = false;
	
	@Inject
	public TableDataPresenter(final EventBus eventBus, final ITableDataView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		loadData();
		
	}

	public void loadData(){
		if(loaded){
			return;
		}
		loaded=true;
		requestHelper.execute(new GetLongTasksRequest(), new TaskServiceCallback<GetLongTasksResponse>() {
			@Override
			public void processResult(GetLongTasksResponse aResponse) {
				getView().setTasks(aResponse.getLongTasks());
			}
		});
	}

}
