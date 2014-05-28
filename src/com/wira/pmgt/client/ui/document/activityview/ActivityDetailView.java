package com.wira.pmgt.client.ui.document.activityview;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.programs.ProgramHeader;
import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.client.ui.util.NumberUtils;
import com.wira.pmgt.shared.model.program.IsProgramDetail;

public class ActivityDetailView extends ViewImpl implements
		ActivityDetailPresenter.MyView {

	private final Widget widget;
	@UiField
	ProgramHeader headerContainer;
	@UiField
	HTMLPanel panelCrumbs;
	
	@UiField
	HTMLPanel divContentTop;
	
	@UiField
	SpanElement spnBudget;
	
	@UiField
	SpanElement spnAssigned;
	
	@UiField
	SpanElement spnDescription;
	
	@UiField
	SpanElement spnProgress;
	
	@UiField
	SpanElement spnRatings;
	
	@UiField
	SpanElement spnTargets;

	public interface Binder extends UiBinder<Widget, ActivityDetailView> {
	}

	@Inject
	public ActivityDetailView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		divContentTop.getElement().getStyle().setProperty("minHeight", "0px");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bind(IsProgramDetail singleActivity) {
		headerContainer.setTitle(singleActivity.getDisplayName());
		headerContainer.setDates("(" + DateUtils.MONTHDAYFORMAT.format(singleActivity.getStartDate()) + " to "
				+ DateUtils.MONTHDAYFORMAT.format(singleActivity.getEndDate()) + ")");
		//headerContainer.setBudget(Double.toString(singleActivity.getBudgetAmount()));
		
		BulletListPanel breadCrumbs = headerContainer.setBreadCrumbs(singleActivity.getProgramSummary());
		panelCrumbs.clear();
		panelCrumbs.add(breadCrumbs);
		
		
		spnBudget.setInnerText(NumberUtils.CURRENCYFORMAT.format(singleActivity.getBudgetAmount()));
		spnDescription.setInnerText(singleActivity.getDescription());
		//spnAssigned.setInnerText(singleActivity.geta)
		
	}
}
