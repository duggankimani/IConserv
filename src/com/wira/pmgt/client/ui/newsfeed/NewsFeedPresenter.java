package com.wira.pmgt.client.ui.newsfeed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.newsfeed.components.CommentActivity;
import com.wira.pmgt.client.ui.newsfeed.components.TaskActivity;
import com.wira.pmgt.shared.model.Activity;
import com.wira.pmgt.shared.model.Comment;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.requests.GetActivitiesRequest;
import com.wira.pmgt.shared.requests.GetProgramCalendarRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetActivitiesResponse;
import com.wira.pmgt.shared.responses.GetCommentsResponse;
import com.wira.pmgt.shared.responses.GetProgramCalendarResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class NewsFeedPresenter extends
		PresenterWidget<NewsFeedPresenter.MyView> {

	public interface MyView extends View {
		void bind();

		BulletListPanel getPanelActivity();

		void setCalendar(List<ProgramSummary> summary);
	}

	@Inject DispatchAsync requestHelper;

	@Inject
	public NewsFeedPresenter(final EventBus eventBus, MyView view){
		super(eventBus, view);
	}
	 
	public void loadActivities() {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetActivitiesRequest(null));
		requests.addRequest(new GetProgramCalendarRequest());
		
		fireEvent(new ProcessingEvent());
		requestHelper.execute(requests, 
				new TaskServiceCallback<MultiRequestActionResult>() {
			
			public void processResult(MultiRequestActionResult results) {
				GetActivitiesResponse getActivities = (GetActivitiesResponse)results.get(0);
				bindActivities(getActivities);
				
				GetProgramCalendarResponse calendar = (GetProgramCalendarResponse)results.get(1);
				getView().setCalendar(calendar.getSummary());
				fireEvent(new ProcessingCompletedEvent());
			}
		});
		
		
	}

	protected void bindActivities(GetActivitiesResponse response) {
		//getView().getPanelActivity().clear();
		
		//Map<Activity, List<Activity>> activitiesMap = response.getActivityMap();
		//setInSlot(ACTIVITY_SLOT, null);
		Map<Activity, List<Activity>> activitiesMap = response.getActivityMap();
		//System.out.println(activitiesMap.size());
		Set<Activity> keyset = activitiesMap.keySet();
		List<Activity> activities= new ArrayList<Activity>();
		
		activities.addAll(keyset);
		Collections.sort(activities);
		Collections.reverse(activities);
		
		for(Activity activity: activities){
			bind(activity,false);	
			//System.err.println(activity);
			List<Activity> children = activitiesMap.get(activity);	
			if(children!=null){
				for(Activity child: children){
					//System.err.println(child);
					bind(child, true);
				}
				
			}
		}
	}

	private void bind(Activity activity, boolean b) {
		if(activity instanceof Notification){
			TaskActivity activityView = new TaskActivity((Notification)activity);
			getView().getPanelActivity().add(activityView);
		}else{
			CommentActivity activityView = new CommentActivity((Comment)activity);
			getView().getPanelActivity().add(activityView);
		}
	}

	protected void bindCommentsResult(GetCommentsResponse commentsResult) {
		
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().bind();
	}
}
