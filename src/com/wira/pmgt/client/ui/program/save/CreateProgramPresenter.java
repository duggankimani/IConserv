package com.wira.pmgt.client.ui.program.save;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.OptionControl;
import com.wira.pmgt.client.ui.events.ActivitiesReloadEvent;
import com.wira.pmgt.client.ui.period.save.PeriodSaveView;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.requests.CreatePeriodRequest;
import com.wira.pmgt.shared.requests.CreateProgramRequest;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetGroupsRequest;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.CreatePeriodResponse;
import com.wira.pmgt.shared.responses.CreateProgramResponse;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.wira.pmgt.shared.responses.GetGroupsResponse;
import com.wira.pmgt.shared.responses.GetPeriodsResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class CreateProgramPresenter extends
		PresenterWidget<CreateProgramPresenter.ICreateDocView> {

	public interface ICreateDocView extends PopupView {

		HasClickHandlers getAddPeriodLink();
		HasClickHandlers getSave();
		HasClickHandlers getCancel();
		boolean isValid();
		void setPeriods(List<PeriodDTO> periods);
		void setGroups(List<UserGroup> groups);
		ProgramDTO getProgram();
		void setFunds(List<FundDTO> funds);
		void setProgram(IsProgramActivity program);
	}

	@Inject DispatchAsync requestHelper;
	private Long programId;
	private IsProgramActivity program;
	private boolean navigateOnSave;
	
	@Inject
	public CreateProgramPresenter(final EventBus eventBus, final ICreateDocView view) {
		super(eventBus, view);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		loadList();
	}
	
	@Override
	protected void onBind() {
		super.onBind();

		final PeriodSaveView periodSave = new PeriodSaveView();
		
		getView().getAddPeriodLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				AppManager.showPopUp("Add Period",periodSave, new OptionControl() {
					
					@Override
					public void onSelect(String name) {
						if("Save".endsWith(name)){
							//Save Period
							if(periodSave.isValid()){
								PeriodDTO period= periodSave.getPeriod();
								savePeriod(period);
								hide();
							}
						}else{
							hide();
						}
					}
					
				}, "Save", "Cancel");
			}
		});
		
		getView().getCancel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getView().hide();
			}
		});

		getView().getSave().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					IsProgramActivity newProgram = getView().getProgram();
					newProgram.setId(programId);					
					requestHelper.execute(new CreateProgramRequest(newProgram),
							new TaskServiceCallback<CreateProgramResponse>() {
							@Override
							public void processResult(
									CreateProgramResponse aResponse) {
								getView().hide();
								
								if(navigateOnSave){
									History.newItem("home;page=activities;activity="+aResponse.getProgram().getId(),
											true);
								}else{
									fireEvent(new ActivitiesReloadEvent());
								}
							}
						});
				}
				
			}
		});
	}
	

	private void savePeriod(PeriodDTO period) {
		requestHelper.execute(new CreatePeriodRequest(period), new TaskServiceCallback<CreatePeriodResponse>() {
			@Override
			public void processResult(CreatePeriodResponse aResponse) {
				loadList();
			}
		});
	}

	private void loadList() {
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFundsRequest());
		action.addRequest(new GetGroupsRequest());
		action.addRequest(new GetPeriodsRequest());
		if(programId!=null){
			action.addRequest(new GetProgramsRequest(programId, false));
		}
		
		
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				GetFundsResponse getFunds = (GetFundsResponse)aResponse.get(0);
				getView().setFunds(getFunds.getFunds());
				
				GetGroupsResponse getGroups = (GetGroupsResponse)aResponse.get(1);
				getView().setGroups(getGroups.getGroups());
				
				GetPeriodsResponse getPeriods = (GetPeriodsResponse)aResponse.get(2);
				getView().setPeriods(getPeriods.getPeriods());
				
				if(programId!=null){
					GetProgramsResponse getProgram = (GetProgramsResponse)aResponse.get(3);
					program = getProgram.getSingleResult();
					getView().setProgram(program);
				}
				
			}
		});
	}

	public void setProgramId(Long programId) {
		this.programId=programId;
	}

	public void setNavigateOnSave(boolean navigateOnSave) {
		this.navigateOnSave = navigateOnSave;
	}
}
