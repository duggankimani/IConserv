package com.wira.pmgt.client.ui.addDoc;

import java.util.List;

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
import com.wira.pmgt.client.ui.addDoc.doctypeitem.DocTypeItemPresenter;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.requests.GetDocumentTypesRequest;
import com.wira.pmgt.shared.responses.GetDocumentTypesResponse;

public class DocumentPopupPresenter extends
		PresenterWidget<DocumentPopupPresenter.MyView> {
	
	public static final Object DOCITEM_SLOT = new Object();
	
	IndirectProvider<DocTypeItemPresenter> docTypeFactory;
	
	@Inject
	DispatchAsync requestHelper;

	public interface MyView extends View {
	}
	
	@Inject
	public DocumentPopupPresenter(final EventBus eventBus, final MyView view, Provider<DocTypeItemPresenter> docTypeProvider) {
		super(eventBus, view);
		docTypeFactory = new StandardProvider<DocTypeItemPresenter>(docTypeProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
	}
	
	
	@Override
	protected void onReveal() {
		super.onReveal();
		GetDocumentTypesRequest req=new GetDocumentTypesRequest();
		requestHelper.execute(req,new TaskServiceCallback<GetDocumentTypesResponse>() {
			@Override
			public void processResult(GetDocumentTypesResponse result) {
				List<DocumentType> types=result.getDocumentTypes();
				setDocumentItems(types);
			}
		});
	}
	
	public void setDocumentItems(List<DocumentType> types){
		this.setInSlot(DOCITEM_SLOT, null);
		for(final DocumentType type : types) {
			docTypeFactory.get(new ServiceCallback<DocTypeItemPresenter>() {
				@Override
				public void processResult(DocTypeItemPresenter result) {
					result.setDocumentTypes(type);
					DocumentPopupPresenter.this.addToSlot(DOCITEM_SLOT, result);
				}
			});
		}
	}
}
