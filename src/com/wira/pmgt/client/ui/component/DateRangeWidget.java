package com.wira.pmgt.client.ui.component;

import static com.wira.pmgt.client.ui.util.DateUtils.DATEFORMAT;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateRangeWidget extends Composite {

	private static DateRangeWidgetUiBinder uiBinder = GWT
			.create(DateRangeWidgetUiBinder.class);

	interface DateRangeWidgetUiBinder extends UiBinder<Widget, DateRangeWidget> {
	}


	@UiField DateBox dateInput1;
	@UiField DateBox dateInput2;
	@UiField InlineLabel spnCalendar1;
	@UiField InlineLabel spnCalendar2;
	
	
	public DateRangeWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		createWidget();
	}


	private void createWidget() {
		dateInput1.getElement().setAttribute("Placeholder", "Start Date");	
		dateInput1.setFormat(new DateBox.DefaultFormat(DATEFORMAT));
		dateInput2.setFormat(new DateBox.DefaultFormat(DATEFORMAT));
		dateInput2.getElement().setAttribute("Placeholder", "End Date");
		spnCalendar1.getElement().setInnerHTML("<i class='icon-calendar'/>");
		spnCalendar2.getElement().setInnerHTML("<i class='icon-calendar'/>");
		spnCalendar1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dateInput1.showDatePicker();
				
			}
		});
		
		spnCalendar2.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dateInput2.showDatePicker();
			}
		});
		
	}
	
	public Date getStartDate(){
		return dateInput1.getValue();
	}
	
	public Date getEndDate(){
		return dateInput2.getValue();
	}
	
	public void addValueChangeHandler(ValueChangeHandler<Date> valueChangeHandler){
		dateInput1.addValueChangeHandler(valueChangeHandler);
		dateInput2.addValueChangeHandler(valueChangeHandler);
	}
	
	public void setDates(Date startDate, Date endDate){
		dateInput1.setValue(startDate);
		dateInput2.setValue(endDate);
	}
 
}
