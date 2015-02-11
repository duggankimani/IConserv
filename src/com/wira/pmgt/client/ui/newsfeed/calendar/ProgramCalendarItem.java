package com.wira.pmgt.client.ui.newsfeed.calendar;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.ActionLink;
import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class ProgramCalendarItem extends Composite {

	private static ProgramCalendarItemUiBinder uiBinder = GWT
			.create(ProgramCalendarItemUiBinder.class);

	interface ProgramCalendarItemUiBinder extends
			UiBinder<Widget, ProgramCalendarItem> {
	}

	@UiField
	ActionLink aProgram;
	@UiField
	SpanElement spnDetail;
	@UiField 
	SpanElement spnLabel;
	
	@UiField 
	SpanElement spnDate;

	public ProgramCalendarItem(ProgramSummary program) {
		initWidget(uiBinder.createAndBindUi(this));

		if(program.isOverdue()){
			spnLabel.addClassName("label-danger");
			spnLabel.setInnerText(" Overdue");
			spnDetail.setInnerText(program.getName());
			spnDate.setInnerText(DateUtils.getTimeDifference(program.getEndDate()));
		}else if(program.isNotStarted()){
			spnLabel.addClassName("label-warning");
			spnLabel.setInnerText(" Late");
			spnDetail.setInnerText(program.getName());
			spnDate.setInnerText(DateUtils.getTimeDifference(program.getStartDate()));
		}else if(program.isUpcoming()){
			spnLabel.addClassName("label-success");
			spnLabel.setInnerText(" Upcoming");
			spnDetail.setInnerText(program.getName());
			spnDate.setInnerText(DateUtils.getTimeDifference(program.getStartDate()));
		}else if(program.isOnGoing()){
			spnLabel.addClassName("label-primary");
			spnLabel.setInnerText(" OnGoing");
			spnDetail.setInnerText(program.getName());
			spnDate.setInnerText(DateUtils.getTimeDifference(program.getStartDate()));
		}else if(program.isCompleted()){
			spnLabel.addClassName("label-success");
			spnLabel.setInnerText("Completed");
			spnDetail.setInnerText(program.getName());
			spnDate.setInnerText(DateUtils.getTimeDifference(program.getDateCompleted()));
		}

		aProgram.setHref("#home;page=activities;activity="
				+ program.getProgramId()
				+ (program.getType() == ProgramDetailType.PROGRAM ? "" : "d"
						+ program.getId()));
	}

}
