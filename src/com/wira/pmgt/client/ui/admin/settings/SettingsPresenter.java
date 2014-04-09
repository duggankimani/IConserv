package com.wira.pmgt.client.ui.admin.settings;

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
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.settings.Setting;
import com.wira.pmgt.shared.requests.GetSettingsRequest;
import com.wira.pmgt.shared.requests.SaveSettingsRequest;
import com.wira.pmgt.shared.responses.GetSettingsResponse;
import com.wira.pmgt.shared.responses.SaveSettingsResponse;

public class SettingsPresenter extends
		PresenterWidget<SettingsPresenter.ISettingsView> {

	public interface ISettingsView extends View {
		public void setValues(List<Setting> settings);
		public List<Setting> getSettings();
		boolean isValid();
		HasClickHandlers getSaveLink();
	}
	
	@Inject DispatchAsync requestHelper;

	@Inject
	public SettingsPresenter(final EventBus eventBus, final ISettingsView view) {
		super(eventBus, view);
	}


	@Override
	protected void onBind() {
		super.onBind();
		getView().getSaveLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					
					requestHelper.execute(new SaveSettingsRequest(getView().getSettings()),
							new TaskServiceCallback<SaveSettingsResponse>() {
						@Override
						public void processResult(
								SaveSettingsResponse aResponse) {
							getView().setValues(aResponse.getSettings());
							AppContext.reloadContext();
						}
					}); 
				}
			}
		});
		
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		loadSettings();
	}

	boolean loaded = false;
	private void loadSettings() {
		if(loaded){
			return;
		}
		loaded=true;
		requestHelper.execute(new GetSettingsRequest(), new TaskServiceCallback<GetSettingsResponse>() {
			@Override
			public void processResult(GetSettingsResponse aResponse) {
				getView().setValues(aResponse.getSettings());
			}
		});
	}
}
