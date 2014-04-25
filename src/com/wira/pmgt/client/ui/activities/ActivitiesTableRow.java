package com.wira.pmgt.client.ui.activities;

import com.google.gwt.core.client.GWT;
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
import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class ActivitiesTableRow extends RowWidget{

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, ActivitiesTableRow> {
	}


	@UiField HTMLPanel row;
	@UiField HTMLPanel divRowNo;
	@UiField HTMLPanel divName;
	@UiField HTMLPanel divStatus;
	@UiField HTMLPanel divProgress;
	@UiField HTMLPanel divRating;
	@UiField HTMLPanel divBudget;
	@UiField HTMLPanel divCheckbox;
	@UiField CheckBox chkSelect;
	
	IsProgramActivity activity;

	public ActivitiesTableRow(IsProgramActivity activity) {
		this.activity = activity;
		initWidget(uiBinder.createAndBindUi(this));
		setRow(row);
		
		divName.getElement().setInnerText(activity.getName());
		divStatus.getElement().setInnerText("CREATED");
		divProgress.getElement().setInnerText("0%");
		divRating.getElement().setInnerText("N/A");
		
		divBudget.getElement().setInnerText(activity.getBudgetAmount()==null? "":
				NumberFormat.getCurrencyFormat().format(activity.getBudgetAmount()));
		
		chkSelect.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				AppContext.fireEvent(new ActivitySelectionChangedEvent(ActivitiesTableRow.this.activity, event.getValue()));
			}
		});
	}
	
	public IsProgramActivity getActivity() {
		return activity;
	}
	
	public void setSelectionChangeHandler(ValueChangeHandler<Boolean> handler) {
		chkSelect.addValueChangeHandler(handler);
	}
	
	@Override
	public void setRowNumber(int number) {
		divRowNo.getElement().setInnerText(""+number);
	}

}
