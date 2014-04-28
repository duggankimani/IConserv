package com.wira.pmgt.client.ui.detailedActivity;

import java.util.List;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetGroupsRequest;
import com.wira.pmgt.shared.requests.GetPeriodRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.GetUsersRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.wira.pmgt.shared.responses.GetGroupsResponse;
import com.wira.pmgt.shared.responses.GetPeriodResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.GetUsersResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class CreateActivityPresenter extends
		PresenterWidget<CreateActivityPresenter.MyView> {

	public interface MyView extends View {
		void setFunds(List<FundDTO> funds);
		void setPeriod(PeriodDTO period);
		void setObjectives(List<IsProgramActivity> objectives);
		void setParentProgram(IsProgramActivity outcome);
		boolean isValid();
		void setActivity(IsProgramActivity activity);
		void clear();
		IsProgramActivity getActivity();
		void setGroups(List<UserGroup> groups);
		void setUsers(List<HTUser> users);
	}

	@Inject DispatchAsync requestHelper;
	
	private IsProgramActivity activity;
	private Long parentId;
	
	@Inject
	public CreateActivityPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	public IsProgramActivity getActivity(){
		IsProgramActivity viewactivity= getView().getActivity();
		viewactivity.setParentId(parentId);
		if(activity!=null){
			//update
			viewactivity.setId(activity.getId());
			System.err.println("Activity: "+activity.getId()+": Parent="+parentId+":: "+activity.getParentId());
			viewactivity.setParentId(activity.getParentId());
			viewactivity.setPeriod(activity.getPeriod());
		}
		
		return viewactivity;
	}

	public void load(Long outcomeId) {
		
		this.parentId = outcomeId;
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFundsRequest());
		action.addRequest(new GetPeriodRequest());
		action.addRequest(new GetProgramsRequest(ProgramDetailType.OBJECTIVE, false));
		action.addRequest(new GetProgramsRequest(outcomeId, false));
		action.addRequest(new GetGroupsRequest());
		action.addRequest(new GetUsersRequest());
				
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				int i=0;
				GetFundsResponse getFunds = (GetFundsResponse)aResponse.get(i++);
				getView().setFunds(getFunds.getFunds());
				
				GetPeriodResponse getPeriod = (GetPeriodResponse)aResponse.get(i++);
				getView().setPeriod(getPeriod.getPeriod());
			
				GetProgramsResponse getPrograms = (GetProgramsResponse)aResponse.get(i++);
				getView().setObjectives(getPrograms.getPrograms());
				
				GetProgramsResponse getProgram = (GetProgramsResponse)aResponse.get(i++);
				getView().setParentProgram(getProgram.getSingleResult());
				getView().setActivity(activity);
				
				GetGroupsResponse getGroups = (GetGroupsResponse)aResponse.get(i++);
				getView().setGroups(getGroups.getGroups());
				
				GetUsersResponse getUsers = (GetUsersResponse)aResponse.get(i++);
				getView().setUsers(getUsers.getUsers());
				
			}
		});
	}

	public void setActivity(IsProgramActivity selected) {
		this.activity = selected;	
		getView().clear();
	}
}
