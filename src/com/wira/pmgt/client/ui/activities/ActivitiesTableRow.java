package com.wira.pmgt.client.ui.activities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.RowWidget;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.Priority;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class ActivitiesTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, ActivitiesTableRow> {
	}

	@UiField
	HTMLPanel row;
	// @UiField HTMLPanel divRowNo;
	@UiField
	HTMLPanel divName;
	@UiField
	SpanElement divStatus;
	@UiField
	HTMLPanel divProgress;
	@UiField
	HTMLPanel divRating;
	@UiField
	HTMLPanel divBudget;
	@UiField
	HTMLPanel divCheckbox;
	@UiField
	CheckBox chkSelect;

	IsProgramActivity activity;

	public ActivitiesTableRow(IsProgramActivity activity) {
		this.activity = activity;
		initWidget(uiBinder.createAndBindUi(this));
		setRow(row);
		setStatus("created", "info");

		divName.getElement().setInnerText(activity.getName());
		setActivityPadding(activity.getType()); // set Padding

		divProgress.getElement().setInnerText("0%");
		divRating.getElement().setInnerText("N/A");

		divBudget.getElement()
				.setInnerText(
						activity.getBudgetAmount() == null ? "" : NumberFormat
								.getCurrencyFormat().format(
										activity.getBudgetAmount()));

		chkSelect.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				AppContext.fireEvent(new ActivitySelectionChangedEvent(
						ActivitiesTableRow.this.activity, event.getValue()));
			}
		});

		divBudget.getElement().getStyle().setTextAlign(TextAlign.RIGHT);
	}

	private void setActivityPadding(ProgramDetailType type) {
		if (type != null) {
			ProgramDetailType passedType = type;

			switch (passedType) {
			case ACTIVITY:
				setisPadded(true);
				break;
			case OBJECTIVE:
				setisPadded(true);
				break;
			default:
				setisPadded(false);
			}

		}
	}

	public IsProgramActivity getActivity() {
		return activity;
	}

	public void setSelectionChangeHandler(ValueChangeHandler<Boolean> handler) {
		chkSelect.addValueChangeHandler(handler);
	}

	/*
	 * Sets the status var statusType: danger-red, warning-golden, info - bluish
	 */
	public void setStatus(String text, String statusType) {
		divStatus.setInnerText(text);
		divStatus.addClassName("label-" + statusType);
	}

	@Override
	public void setRowNumber(int number) {
		// divRowNo.getElement().setInnerText(""+number);
	}

	public void setisPadded(Boolean isPadded) {
		if (isPadded) {
			String padding = divName.getElement().getStyle().getPaddingLeft();
			divName.getElement().getStyle().setPaddingLeft(40.0, Unit.PX);
		}
	}

}
