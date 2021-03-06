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
import com.wira.pmgt.client.ui.donor.save.DonorSaveView;
import com.wira.pmgt.client.ui.events.ActivitySavedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.events.ProgramsReloadEvent;
import com.wira.pmgt.client.ui.period.save.PeriodSaveView;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.requests.CreateDonorRequest;
import com.wira.pmgt.shared.requests.CreatePeriodRequest;
import com.wira.pmgt.shared.requests.CreateProgramRequest;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.CreatePeriodResponse;
import com.wira.pmgt.shared.responses.CreateProgramResponse;
import com.wira.pmgt.shared.responses.GetFundsResponse;
import com.wira.pmgt.shared.responses.GetPeriodsResponse;
import com.wira.pmgt.shared.responses.GetProgramsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class CreateProgramPresenter extends
		PresenterWidget<CreateProgramPresenter.ICreateDocView> {

	public interface ICreateDocView extends PopupView {

		HasClickHandlers getAddPeriodLink();

		HasClickHandlers getSave();

		HasClickHandlers getCancel();

		HasClickHandlers getEditPeriodLink();

		boolean isValid();

		void setPeriods(List<PeriodDTO> periods);

		ProgramDTO getProgram();

		void setFunds(List<FundDTO> funds);

		PeriodDTO getPeriod();

		void setProgram(IsProgramDetail program);

		HasClickHandlers getManageDonors();

		List<PeriodDTO> getPeriods();
		
		void setObjectives(List<IsProgramDetail> objectives);

	}

	@Inject
	DispatchAsync requestHelper;
	private Long programId;
	private IsProgramDetail program;
	private boolean navigateOnSave;
	

	@Inject
	public CreateProgramPresenter(final EventBus eventBus,
			final ICreateDocView view) {
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

		getView().getAddPeriodLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final PeriodSaveView periodSave = new PeriodSaveView(null, getView().getPeriods());

				AppManager.showPopUp("Add Period", periodSave,
						new OptionControl() {

							@Override
							public void onSelect(String name) {
								if ("Save".equals(name)) {
									// Save Period
									if (periodSave.isValid()) {
										PeriodDTO period = periodSave
												.getPeriod();
										savePeriod(period);
										hide();
									}
								} else {
									hide();
								}
							}

						}, "Save", "Cancel");
			}
		});

		getView().getEditPeriodLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final PeriodSaveView periodSave = new PeriodSaveView(getView()
						.getPeriod(),getView().getPeriods());
				AppManager.showPopUp("Add Period", periodSave,
						new OptionControl() {

							@Override
							public void onSelect(String name) {
								if ("Save".equals(name)) {
									// Save Period
									if (periodSave.isValid()) {
										PeriodDTO period = periodSave
												.getPeriod();
										savePeriod(period);
										hide();
									}
								} else {
									hide();
								}
							}

						}, "Save", "Cancel");
			}
		});
		
		

		getView().getManageDonors().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				requestHelper.execute(new GetFundsRequest(), new TaskServiceCallback<GetFundsResponse>() {
					@Override
					public void processResult(GetFundsResponse aResponse) {
						showDonorPopup(aResponse.getFunds());
					}

				});
			}
			
			void showDonorPopup(List<FundDTO> funds) {
				
				final DonorSaveView donorSave = new DonorSaveView(funds);
				
				AppManager.showPopUp("Manage Donors", donorSave,
						new OptionControl() {
							@Override
							public void onSelect(String name) {
								if(name.equals("Save")){
									if(donorSave.isValid()){
										saveDonor(donorSave.getDonor());
										hide();
									}									
								}else{
									hide();
								}
							}
						},"Save", "Cancel");
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
				if (getView().isValid()) {
					fireEvent(new ProcessingEvent("Saving Program.."));
					
					IsProgramDetail newProgram = getView().getProgram();
					newProgram.setId(programId);
					
					requestHelper.execute(new CreateProgramRequest(newProgram),
							new TaskServiceCallback<CreateProgramResponse>() {
								@Override
								public void processResult(
										CreateProgramResponse aResponse) {
									getView().hide();
									IsProgramDetail saved = aResponse.getProgram();
									fireEvent(new ActivitySavedEvent(
											"Program Successfully Changed"));
									if (navigateOnSave) {
										History.newItem(
												"home;page=activities;activity="
														+ saved.getId()+
														";period="+saved.getPeriod().getId(), true);
									} else {
										fireEvent(new ProgramsReloadEvent(saved.getPeriod().getId()));
									}
								}
							});
				}

			}
		});
	}

	private void savePeriod(PeriodDTO period) {
		requestHelper.execute(new CreatePeriodRequest(period),
				new TaskServiceCallback<CreatePeriodResponse>() {
					@Override
					public void processResult(CreatePeriodResponse aResponse) {
						loadList();
					}
				});
	}

	private void loadList() {

		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFundsRequest());
		action.addRequest(new GetPeriodsRequest());
		action.addRequest(new GetProgramsRequest(ProgramDetailType.OUTCOME, false));
		if (programId != null) {
			action.addRequest(new GetProgramsRequest(programId, false));
		}

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						GetFundsResponse getFunds = (GetFundsResponse) aResponse
								.get(0);
						getView().setFunds(getFunds.getFunds());

						GetPeriodsResponse getPeriods = (GetPeriodsResponse) aResponse
								.get(1);
						getView().setPeriods(getPeriods.getPeriods());

						GetProgramsResponse getPrograms = (GetProgramsResponse)aResponse.get(2);
						getView().setObjectives(getPrograms.getPrograms());
						
						if (programId != null) {
							GetProgramsResponse getProgram = (GetProgramsResponse) aResponse
									.get(3);
							program = getProgram.getSingleResult();
							getView().setProgram(program);
						}

					}
				});
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public void setNavigateOnSave(boolean navigateOnSave) {
		this.navigateOnSave = navigateOnSave;
	}
	
	private void saveDonor(FundDTO donor) {
	
		final IsProgramDetail detail = getView().getProgram();
		
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new CreateDonorRequest(donor));
		requests.addRequest(new GetFundsRequest());
		requestHelper.execute(requests, new TaskServiceCallback<MultiRequestActionResult>() {
			
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				GetFundsResponse response = (GetFundsResponse)aResponse.get(1);
				getView().setFunds(response.getFunds());
				getView().setProgram(detail);
			}
		});
	
	}

}
