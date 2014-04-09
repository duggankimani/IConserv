package com.wira.pmgt.client.ui.admin.ds;

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
import com.wira.pmgt.client.ui.admin.ds.item.DSItemPresenter;
import com.wira.pmgt.client.ui.admin.ds.save.DSSavePresenter;
import com.wira.pmgt.client.ui.events.EditDSConfigEvent;
import com.wira.pmgt.client.ui.events.LoadDSConfigsEvent;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.client.ui.events.EditDSConfigEvent.EditDSConfigHandler;
import com.wira.pmgt.client.ui.events.LoadDSConfigsEvent.LoadDSConfigsHandler;
import com.wira.pmgt.shared.model.DSConfiguration;
import com.wira.pmgt.shared.requests.GetDSConfigurationsRequest;
import com.wira.pmgt.shared.requests.GetDSStatusRequest;
import com.wira.pmgt.shared.responses.GetDSConfigurationsResponse;
import com.wira.pmgt.shared.responses.GetDSStatusResponse;

public class DataSourcePresenter extends
		PresenterWidget<DataSourcePresenter.IDataSourceView> implements LoadDSConfigsHandler, EditDSConfigHandler{

	public interface IDataSourceView extends View {

		HasClickHandlers getNewDatasourceButton();
		HasClickHandlers getTestAllDatasources();
	}
	
	public static final Object TABLE_SLOT = new Object();
	
	@Inject DispatchAsync requestHelper;
	
	IndirectProvider<DSSavePresenter> dsSaveFactory;
	IndirectProvider<DSItemPresenter> dsItemFactory;

	@Inject
	public DataSourcePresenter(final EventBus eventBus, final IDataSourceView view,
			Provider<DSSavePresenter> dsSaveProvider, 
			Provider<DSItemPresenter> dsItemProvider) {
		super(eventBus, view);
		dsSaveFactory = new StandardProvider<DSSavePresenter>(dsSaveProvider);
		dsItemFactory = new StandardProvider<DSItemPresenter>(dsItemProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(LoadDSConfigsEvent.TYPE, this);
		addRegisteredHandler(EditDSConfigEvent.TYPE, this);
		
		getView().getNewDatasourceButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showConfigSavePopup();
			}
		});
		
		getView().getTestAllDatasources().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent("Starting processes"));
				requestHelper.execute(new GetDSStatusRequest(), 
						new TaskServiceCallback<GetDSStatusResponse>() {
					@Override
					public void processResult(
							GetDSStatusResponse aResponse) {
						bindValues(aResponse.getConfigs());
						fireEvent(new ProcessingCompletedEvent());
					}

				});
			}
		});
		
	}
	
	private void showConfigSavePopup(){
		showConfigSavePopup(null);
	}
	private void showConfigSavePopup(final DSConfiguration config) {
		
		dsSaveFactory.get(new ServiceCallback<DSSavePresenter>() {
			@Override
			public void processResult(DSSavePresenter aResponse) {
				aResponse.setConfiguration(config);
				addToPopupSlot(aResponse,false);
			}
		});
			
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		loadConfigurations();
	}

	public void loadConfigurations(){
		
		fireEvent(new ProcessingEvent());
		requestHelper.execute(new GetDSConfigurationsRequest(),
				new TaskServiceCallback<GetDSConfigurationsResponse>() {
			@Override
			public void processResult(GetDSConfigurationsResponse result) {
				List<DSConfiguration> configs = result.getConfigurations();
				bindValues(configs);
				fireEvent(new ProcessingCompletedEvent());
				
			}
		});
	}
	
	private void bindValues(List<DSConfiguration> configs) {
		setInSlot(TABLE_SLOT, null);
		if(configs!=null){
			for(final DSConfiguration config: configs){
				dsItemFactory.get(new ServiceCallback<DSItemPresenter>() {
					@Override
					public void processResult(
							DSItemPresenter result) {
						result.setConfiguration(config);
						addToSlot(TABLE_SLOT, result);
					}
				});
			}
		}
	}


	@Override
	public void onLoadDSConfigs(LoadDSConfigsEvent event) {
		loadConfigurations();
	}

	@Override
	public void onEditDSConfig(EditDSConfigEvent event) {
		showConfigSavePopup(event.getConfiguration());
	}
}