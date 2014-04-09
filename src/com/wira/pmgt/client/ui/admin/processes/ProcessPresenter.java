package com.wira.pmgt.client.ui.admin.processes;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.ServiceCallback;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.admin.processes.save.ProcessSavePresenter;
import com.wira.pmgt.client.ui.admin.processitem.ProcessItemPresenter;
import com.wira.pmgt.client.ui.events.EditProcessEvent;
import com.wira.pmgt.client.ui.events.LoadProcessesEvent;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.events.EditProcessEvent.EditProcessHandler;
import com.wira.pmgt.client.ui.events.LoadProcessesEvent.LoadProcessesHandler;
import com.wira.pmgt.shared.model.ProcessDef;
import com.wira.pmgt.shared.requests.GetProcessesRequest;
import com.wira.pmgt.shared.requests.StartAllProcessesRequest;
import com.wira.pmgt.shared.responses.GetProcessesResponse;
import com.wira.pmgt.shared.responses.StartAllProcessesResponse;

public class ProcessPresenter extends
		PresenterWidget<ProcessPresenter.IProcessView> implements LoadProcessesHandler,
		EditProcessHandler{

	public interface IProcessView extends View {

		HasClickHandlers getaNewProcess();

		HasClickHandlers getStartAllProcesses();
	}
	
	public static final Object TABLE_SLOT = new Object();
	
	@Inject DispatchAsync requestHelper;
	
	IndirectProvider<ProcessSavePresenter> processFactory;
	IndirectProvider<ProcessItemPresenter> processItemFactory;

	@Inject
	public ProcessPresenter(final EventBus eventBus, final IProcessView view,
			Provider<ProcessSavePresenter> addprocessProvider, Provider<ProcessItemPresenter> columnProvider) {
		super(eventBus, view);
		processFactory = new StandardProvider<ProcessSavePresenter>(addprocessProvider);
		processItemFactory= new StandardProvider<ProcessItemPresenter>(columnProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(LoadProcessesEvent.TYPE, this);
		addRegisteredHandler(EditProcessEvent.TYPE, this);
		
		getView().getaNewProcess().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showAddProcessPopup();
			}
		});
		
		getView().getStartAllProcesses().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent("Starting processes"));
				requestHelper.execute(new StartAllProcessesRequest(), 
						new TaskServiceCallback<StartAllProcessesResponse>() {
					@Override
					public void processResult(
							StartAllProcessesResponse aResponse) {
						loadProcesses();
					}
				});
			}
		});
		
	}
	
	private void showAddProcessPopup(){
		showAddProcessPopup(null);
	}
	private void showAddProcessPopup(final Long processDefId) {
		processFactory.get(new ServiceCallback<ProcessSavePresenter>() {
			@Override
			public void processResult(ProcessSavePresenter result) {
				result.setProcessDefId(processDefId);
				addToPopupSlot(result,false);
			}
		});
			
	}

	public void loadProcesses(){
		
		fireEvent(new ProcessingEvent());
		requestHelper.execute(new GetProcessesRequest(),new TaskServiceCallback<GetProcessesResponse>() {
			@Override
			public void processResult(GetProcessesResponse result) {
				List<ProcessDef> processDefinitions = result.getProcesses();
				setInSlot(TABLE_SLOT, null);
				
				if(processDefinitions!=null){
					for(final ProcessDef def: processDefinitions){
						processItemFactory.get(new ServiceCallback<ProcessItemPresenter>() {
							@Override
							public void processResult(
									ProcessItemPresenter result) {
								
								result.setProcess(def);
								addToSlot(TABLE_SLOT, result);
								
							}
						});
					}
				}
				
				fireEvent(new ProcessingCompletedEvent());
				
			}
		});
	}

	@Override
	public void onLoadProcesses(LoadProcessesEvent event) {
		loadProcesses();
	}
	
	@Override
	public void onEditProcess(EditProcessEvent event) {
		Long processDefId = event.getProcessId();
		showAddProcessPopup(processDefId);
	}
	
}