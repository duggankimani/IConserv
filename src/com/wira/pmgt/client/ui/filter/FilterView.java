package com.wira.pmgt.client.ui.filter;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.DropDownList;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.SearchFilter;

public class FilterView extends ViewImpl implements FilterPresenter.MyView {

	private final Widget widget;
	@UiField
	FocusPanel filterDialog;
	@UiField
	HTMLPanel divFilter;
	
	@UiField
	Button btnSearch;
	@UiField
	Anchor aClose;
	@UiField
	DropDownList<DocumentType> lstDocType;

	public interface Binder extends UiBinder<Widget, FilterView> {
	}

	@Inject
	public FilterView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//divFilter.removeStyleName("is-visible");
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getSearchButton() {
		return btnSearch;
	}

	@Override
	public SearchFilter getSearchFilter() {
		SearchFilter filter = new SearchFilter();
		// filter.setSubject(txtSubject.getValue());
		// filter.setPhrase(txtPhrase.getValue());
		// filter.setStartDate(dateInput1.getValue());
		// filter.setEndDate(dateInput2.getValue());
		filter.setDocType(lstDocType.getValue());
		return filter;
	}

	@Override
	public HasClickHandlers getCloseButton() {
		return aClose;
	}

	@Override
	public HasBlurHandlers getFilterDialog() {
		return filterDialog;
	}

	@Override
	public void setDocTypes(List<DocumentType> documentTypes) {
		lstDocType.setItems(documentTypes);
	}

}
