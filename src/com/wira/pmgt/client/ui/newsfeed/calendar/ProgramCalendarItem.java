package com.wira.pmgt.client.ui.newsfeed.calendar;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.wira.pmgt.client.place.NameTokens;
import com.wira.pmgt.client.ui.component.ActionLink;
import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class ProgramCalendarItem extends Composite {

	private static ProgramCalendarItemUiBinder uiBinder = GWT
			.create(ProgramCalendarItemUiBinder.class);

	interface ProgramCalendarItemUiBinder extends
			UiBinder<Widget, ProgramCalendarItem> {
	}
	
	@UiField ActionLink aProgram;
	@UiField SpanElement spnDetail;
	
	public ProgramCalendarItem(ProgramSummary summary) {
		initWidget(uiBinder.createAndBindUi(this));
		
		if(summary.getEndDate().before(new Date())){
			spnDetail.setInnerText("Overdue: "+summary.getName()+" "+DateUtils.getTimeDifference(summary.getEndDate()));
		}else{
			spnDetail.setInnerText("Not started yet: "+summary.getName()+" "+DateUtils.getTimeDifference(summary.getStartDate()));
		}
		
		aProgram.setHref("#");
//				home;page=activities;activity="
//				+summary.getParentId()+"d"+summary.getId());
	}

}
