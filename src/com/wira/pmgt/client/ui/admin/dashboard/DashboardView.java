package com.wira.pmgt.client.ui.admin.dashboard;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.ActionLink;
import com.wira.pmgt.client.ui.component.Dropdown;
import com.wira.pmgt.shared.model.program.PeriodDTO;

import static com.wira.pmgt.client.ui.admin.dashboard.DashboardPresenter.*;

public class DashboardView extends ViewImpl implements
		DashboardPresenter.IDashboardView {

	public interface Binder extends UiBinder<Widget, DashboardView> {
	}

	private Widget widget;

	@UiField
	HTMLPanel panelTurnAroundTime;
	@UiField
	HTMLPanel panelTotalRequestsPerDoc;
	@UiField
	HTMLPanel panelRequestsApprovalsComp;
	@UiField
	HTMLPanel panelLongLastingProcesses;

	@UiField
	SpanElement spnReqCount;
	@UiField
	SpanElement spnActiveCount;
	@UiField
	SpanElement spnFailureCount;

	@Inject
	public DashboardView(final Binder binder) {
		widget = binder.createAndBindUi(this);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, Widget content) {

		if (slot == OVERALLTURNAROUND_SLOT) {
			panelTurnAroundTime.clear();
			if (content != null) {
				panelTurnAroundTime.add(content);
			}
		} else if (slot == REQUESTSPERDOC_SLOT) {
			panelTotalRequestsPerDoc.clear();
			if (content != null) {
				panelTotalRequestsPerDoc.add(content);
			}
		} else if (slot == LINEGRAPH_SLOT) {
			panelRequestsApprovalsComp.clear();
			if (content != null) {
				panelRequestsApprovalsComp.add(content);
			}
		} else if (slot == LONGLASTINGTASKS_SLOT) {
			panelLongLastingProcesses.clear();
			if (content != null) {
				panelLongLastingProcesses.add(content);
			}
		} else
			super.setInSlot(slot, content);
	}

	@Override
	public void setValues(Integer requestCount, Integer activeCount,
			Integer failureCount) {
		NumberFormat format = NumberFormat.getFormat("#,###");
		String reqC = format.format(requestCount);
		String activeC = format.format(activeCount);
		String failureC = format.format(failureCount);

		spnReqCount.setInnerText(reqC);
		spnActiveCount.setInnerText(activeC);
		spnFailureCount.setInnerText(failureC);
	}

}
