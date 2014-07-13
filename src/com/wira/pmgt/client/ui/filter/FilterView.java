package com.wira.pmgt.client.ui.filter;

import java.util.Arrays;

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
import com.wira.pmgt.shared.model.Listable;
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
	DropDownList<ProgressFilter> lstProgressType;
	@UiField
	DropDownList<TargetsFilter> lstTargetsType;
	@UiField
	DropDownList<TimelinesFilter> lstTimelinesType;
	@UiField
	DropDownList<BudgetFilter> lstBudgetType;
	
	public enum ProgressFilter implements Listable{
		COMPLETED("Completed"),
		ONGOING("On Going"),
		NOTSTARTED("Not Started");
		
		String name;
		private ProgressFilter(String text) {
			this.name = text;
		}
		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getDisplayName() {
			return name;
		}
		
	}
	
	public enum BudgetFilter implements Listable{
		WITHIN("Within Budget"),
		OUTSIDE("Outside Budget"),
		NODATA("Not Data");
		
		String name;
		private BudgetFilter(String text) {
			this.name = text;
		}
		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getDisplayName() {
			return name;
		}
		
	}

	public enum TimelinesFilter implements Listable{
		WITHIN("Within Timelines"),
		OUTSIDE("Outside Timelines"),
		NODATA("No Data");
		
		String name;
		private TimelinesFilter(String text) {
			this.name = text;
		}
		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getDisplayName() {
			return name;
		}
	}
	
	public enum TargetsFilter implements Listable{
		MET("Within Targets"),
		NOTMET("Outside Targets"),
		NODATA("No Data");
		
		String name;
		private TargetsFilter(String text) {
			this.name = text;
		}
		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getDisplayName() {
			return name;
		}
		
	}
	
	

	public interface Binder extends UiBinder<Widget, FilterView> {
	}

	@Inject
	public FilterView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			}
		});
		
		lstProgressType.setItems(Arrays.asList(ProgressFilter.values()));
		lstTimelinesType.setItems(Arrays.asList(TimelinesFilter.values()));
		lstBudgetType.setItems(Arrays.asList(BudgetFilter.values()));
		lstTargetsType.setItems(Arrays.asList(TargetsFilter.values()));
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

}
