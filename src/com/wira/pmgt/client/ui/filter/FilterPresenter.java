package com.wira.pmgt.client.ui.filter;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.events.SearchEvent;
import com.wira.pmgt.client.ui.home.HomeView;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.SearchFilter;
import com.wira.pmgt.shared.requests.GetDocumentTypesRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.GetDocumentTypesResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class FilterPresenter extends PresenterWidget<FilterPresenter.MyView> {

	public interface MyView extends View {
		HasClickHandlers getCloseButton();
		HasClickHandlers getSearchButton();
		HasBlurHandlers getFilterDialog();
		SearchFilter getSearchFilter();
		void setDocTypes(List<DocumentType> documentTypes);
	}
	
	@Inject HomeView homeview;
	private String subject;
	
	@Inject DispatchAsync requestHelper;

	@Inject
	public FilterPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getSearchButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				SearchFilter filter = getView().getSearchFilter();
				
				if(!filter.isEmpty())
				fireEvent(new SearchEvent(filter));
				subject=filter.getSubject();
				if(subject!=""){
					//homeview.setSearchBox(subject);
				}
				
				//homeview.hideFilterDialog();
			}
		});
		
		
		getView().getFilterDialog().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				//homeview.hideFilterDialog();
			}
		});
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		
		loadDocTypes();
	}

	private void loadDocTypes() {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetDocumentTypesRequest());
		
		requestHelper.execute(requests, 
					new TaskServiceCallback<MultiRequestActionResult>() {
				
				public void processResult(MultiRequestActionResult responses) {
				
					GetDocumentTypesResponse response = (GetDocumentTypesResponse)responses.get(0);
					getView().setDocTypes(response.getDocumentTypes());
				}
		});
	}
}
