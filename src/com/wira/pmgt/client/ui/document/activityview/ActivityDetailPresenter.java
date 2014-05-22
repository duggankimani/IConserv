package com.wira.pmgt.client.ui.document.activityview;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.events.LoadActivitiesEvent;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.GetTaskInfoRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.GetTaskInfoResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class ActivityDetailPresenter extends
		PresenterWidget<ActivityDetailPresenter.MyView> {

	public interface MyView extends View {

		void bind(IsProgramDetail singleActivity);
	}

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public ActivityDetailPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	public void loadData(Long activityId) {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProgramsRequest(activityId, false));
		action.addRequest(new GetTaskInfoRequest(activityId));

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {

					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						GetProgramsResponse progResponse = (GetProgramsResponse) aResponse
								.get(0);
						getView().bind(progResponse.getSingleResult());
						GetTaskInfoResponse taskResponse = (GetTaskInfoResponse) aResponse
								.get(1);

						if (taskResponse.getDocumentId() != null) {
							fireEvent(new LoadActivitiesEvent(taskResponse
									.getDocumentId()));
						}
					}
				});
	}
}
