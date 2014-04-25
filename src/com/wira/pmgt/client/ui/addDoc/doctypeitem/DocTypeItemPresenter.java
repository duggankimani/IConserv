package com.wira.pmgt.client.ui.addDoc.doctypeitem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.ui.events.CreateProgramEvent;
import com.wira.pmgt.shared.model.DocumentType;

public class DocTypeItemPresenter extends
		PresenterWidget<DocTypeItemPresenter.MyView> {
	
	public interface MyView extends View {
		public Anchor getaDocAnchor();
		public void setValues(String displayName, String className);
	}
	@Inject
	public DocTypeItemPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}
	
	DocumentType type;

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getaDocAnchor().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
//				fireEvent(new CreateProgramEvent(type));			
				}
		});
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
	}
	
	public void setDocumentTypes(DocumentType type) {
		this.type=type;
		getView().setValues(type.getDisplayName(), type.getClassName());
	}
}
