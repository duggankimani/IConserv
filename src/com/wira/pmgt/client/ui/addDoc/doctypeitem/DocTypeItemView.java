package com.wira.pmgt.client.ui.addDoc.doctypeitem;

import java.util.List;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.wira.pmgt.shared.model.DocumentType;

public class DocTypeItemView extends ViewImpl implements
		DocTypeItemPresenter.MyView {

	private final Widget widget;
	
	@UiField Anchor aDocAnchor;
	@UiField SpanElement docIcon;
	@UiField SpanElement spnIcon;
	
	List<DocumentType> items;

	public interface Binder extends UiBinder<Widget, DocTypeItemView> {
	}

	@Inject
	public DocTypeItemView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public Anchor getaDocAnchor() {
		return aDocAnchor;
	}

	@Override
	public void setValues(String displayName, String className) {
		spnIcon.setInnerText(displayName);
		docIcon.addClassName(className);
	}
}
