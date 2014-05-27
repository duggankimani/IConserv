package com.wira.pmgt.client.ui.document.activityview;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.programs.ProgramHeader;
import com.wira.pmgt.shared.model.program.IsProgramDetail;

public class ActivityDetailView extends ViewImpl implements
		ActivityDetailPresenter.MyView {

	private final Widget widget;
	@UiField
	ProgramHeader headerContainer;
	@UiField
	HTMLPanel panelCrumbs;

	public interface Binder extends UiBinder<Widget, ActivityDetailView> {
	}

	@Inject
	public ActivityDetailView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bind(IsProgramDetail singleActivity) {
		headerContainer.setTitle(singleActivity.getDisplayName());
		headerContainer.setDates("(" + singleActivity.getStartDate() + " to "
				+ singleActivity.getEndDate() + ")");
		//headerContainer.setBudget(Double.toString(singleActivity.getBudgetAmount()));
		
		BulletListPanel breadCrumbs = headerContainer.setBreadCrumbs(singleActivity.getProgramSummary());
		panelCrumbs.clear();
		panelCrumbs.add(breadCrumbs);
		
		
		
		
	}
}
