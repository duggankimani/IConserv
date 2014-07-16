package com.wira.pmgt.client.ui.detailedActivity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.TargetAndOutcomeDTO;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetPeriodRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.wira.pmgt.shared.responses.GetPeriodResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class CreateActivityPresenter extends
		PresenterWidget<CreateActivityPresenter.MyView> {

	public interface MyView extends View {
		void setFunds(List<FundDTO> funds);
		void setPeriod(PeriodDTO period);
		void setObjectives(List<IsProgramDetail> objectives);
		void setParentProgram(IsProgramDetail outcome);
		boolean isValid();
		void setActivity(IsProgramDetail activity);
		void clear();
		IsProgramDetail getActivity();
		void setGroups(List<UserGroup> groups);
		void setUsers(List<HTUser> users);
		void setType(ProgramDetailType type);
		HasClickHandlers getCopyTargetsLink();
		void setTargetsAndOutComes(List<TargetAndOutcomeDTO> targetsAndOutComes);
	}

	@Inject DispatchAsync requestHelper;
	
	private IsProgramDetail activity;
	private Long parentId;
	private List<TargetAndOutcomeDTO> parentTargetsAndOutcomes=null;
	private PeriodDTO period;

	private Long outcomeId;
		
	@Inject
	public CreateActivityPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		getView().getCopyTargetsLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				assert parentId!=null;
				List<TargetAndOutcomeDTO> clones = new ArrayList<TargetAndOutcomeDTO>();
				
				if(clones!=null)
				for(TargetAndOutcomeDTO dto: parentTargetsAndOutcomes){
					TargetAndOutcomeDTO clone = dto.clone();
					clone.setActualOutcome(0.0);
					clone.setProgramDetailId(null);
					clone.setId(null);
					clones.add(clone);
				}
				getView().setTargetsAndOutComes(clones);					
				
			}
		});
	}

	public IsProgramDetail getActivity(){
		IsProgramDetail viewactivity= getView().getActivity();
		viewactivity.setParentId(parentId);
		viewactivity.setPeriod(period);
		viewactivity.setActivityOutcomeId(outcomeId);
		if(activity!=null){
			//update
			viewactivity.setId(activity.getId());
			//System.err.println("Activity: "+activity.getId()+": Parent="+parentId+":: "+activity.getParentId());
			viewactivity.setParentId(activity.getParentId());
			viewactivity.setPeriod(activity.getPeriod());
		}
		
		return viewactivity;
	}

	public void load(Long programId,Long outcomeId) {
		
		this.parentId = programId;
		this.outcomeId = outcomeId;
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFundsRequest());
		action.addRequest(new GetPeriodRequest());
		action.addRequest(new GetProgramsRequest(ProgramDetailType.OBJECTIVE, false));
		action.addRequest(new GetProgramsRequest(programId, false));
				
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				int i=0;
				//List of funds to choose from
				GetFundsResponse getFunds = (GetFundsResponse)aResponse.get(i++);
				getView().setFunds(getFunds.getFunds());
				
				//List of periods to choose from
				GetPeriodResponse getPeriod = (GetPeriodResponse)aResponse.get(i++);
				getView().setPeriod(getPeriod.getPeriod());
				period = getPeriod.getPeriod();
			
				//List of Objectives for this activity
				GetProgramsResponse getPrograms = (GetProgramsResponse)aResponse.get(i++);
				getView().setObjectives(getPrograms.getPrograms());
				
				//Parent Program (for pop up additional information)
				GetProgramsResponse getProgram = (GetProgramsResponse)aResponse.get(i++);
				getView().setParentProgram(getProgram.getSingleResult());
				parentTargetsAndOutcomes = getProgram.getSingleResult().getTargetsAndOutcomes();
				
				/**Activity selected from the grid see see {#setActivity}*/  
				getView().setActivity(activity);
				
			}
		});
	}

	public void setActivity(IsProgramDetail selected) {
		this.activity = selected;	
		getView().clear();
	}

	public void setType(ProgramDetailType type) {
		getView().setType(type);
	}
}
