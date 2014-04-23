package com.wira.pmgt.client.ui.activities;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.TableView;
import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class ActivitiesTable extends Composite {

	private static ActivitiesTableUiBinder uiBinder = GWT
			.create(ActivitiesTableUiBinder.class);

	interface ActivitiesTableUiBinder extends UiBinder<Widget, ActivitiesTable> {
	}

	@UiField TableView tblView;
	
	public ActivitiesTable() {
		initWidget(uiBinder.createAndBindUi(this));		
		createGrid();
	}

	public void setData(List<IsProgramActivity> programActivities) {
		for(IsProgramActivity activity: programActivities){
			tblView.addRow(new CheckBox(), new InlineLabel(activity.getName()),new InlineLabel("CREATED"),
					new InlineLabel("0%"), new InlineLabel("N/A"), new InlineLabel(activity.getBudgetAmount()==null? null: activity.getBudgetAmount()+""));
		}
	}

	private void createGrid() {
		List<String> names = new ArrayList<String>();
		names.add("Checkbox");
		names.add("TITLE");
		names.add("STATUS");
		names.add("PROGRESS");
		names.add("RATING");
		names.add("BUDGET");
		tblView.setHeaders(names);

		
	}

}
