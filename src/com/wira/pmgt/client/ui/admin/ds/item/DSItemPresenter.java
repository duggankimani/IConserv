package com.wira.pmgt.client.ui.admin.ds.item;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.OnOptionSelected;
import com.wira.pmgt.client.ui.events.EditDSConfigEvent;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.ProcessingEvent;
import com.wira.pmgt.shared.model.DSConfiguration;
import com.wira.pmgt.shared.model.RDBMSType;
import com.wira.pmgt.shared.model.Status;
import com.wira.pmgt.shared.requests.DeleteDSConfigurationEvent;
import com.wira.pmgt.shared.requests.GetDSStatusRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDSStatusResponse;

public class DSItemPresenter extends
		PresenterWidget<DSItemPresenter.MyView> {

	public interface MyView extends View {

		HasClickHandlers getTestButton();
		HasClickHandlers getEditButton();
		HasClickHandlers getDeleteButton();
		void setValues(Long dsConfigId,RDBMSType rdbms,String name,
				String jndiName,String driverName,String url, 
				String user, Date lastModified, Status status);
	}
	
	@Inject DispatchAsync requestHelper;

	DSConfiguration configuration;
	
	@Inject
	public DSItemPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getTestButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent("Starting processes"));
				requestHelper.execute(new GetDSStatusRequest(configuration.getName()), 
						new TaskServiceCallback<GetDSStatusResponse>() {
					@Override
					public void processResult(
							GetDSStatusResponse aResponse) {
						setConfiguration(aResponse.getConfigs().get(0));
						fireEvent(new ProcessingCompletedEvent());
					}

				});
			}
		});
		

		getView().getEditButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new EditDSConfigEvent(configuration));
			}
		});
		
		
		getView().getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Confirm Delete", 
						"Do you want to delete config \""+configuration.getName()+"\"?",
						new OnOptionSelected() {
							@Override
							public void onSelect(String name) {
								if(name.equals("Yes")){
									requestHelper.execute(new DeleteDSConfigurationEvent(configuration.getId()),
											new TaskServiceCallback<BaseResponse>() {
										public void processResult(BaseResponse aResponse) {
											getView().asWidget().removeFromParent();
										};
									});
								}
							}
				}, "Yes", "Cancel");
			}
		});
		
	}

	public void setConfiguration(DSConfiguration config) {
		this.configuration = config;
		getView().setValues(config.getId(),config.getRDBMS(),config.getName(),
				config.getJNDIName(), config.getDriver()
				,config.getURL(),config.getUser(),config.getLastModified(), config.getStatus());
	}
}
