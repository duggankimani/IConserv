package com.wira.pmgt.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AggregationGrid extends Composite {

	private static AggregationGridUiBinder uiBinder = GWT
			.create(AggregationGridUiBinder.class);

	interface AggregationGridUiBinder extends UiBinder<Widget, AggregationGrid> {
	}
	
	@UiField TableView tblView;

	public AggregationGrid() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void init(){
		//table
	}

}
