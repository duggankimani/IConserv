package com.wira.pmgt.client.ui.activities;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
	
	CheckBox selected = null;
	public void setData(List<IsProgramActivity> programActivities) {
		tblView.clearRows();
		for(IsProgramActivity activity: programActivities){
			CheckBox box = new CheckBox();
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					boolean isSelected = event.getValue();
					if(isSelected){
						if(selected!=null){
							selected.setValue(false);
						}
						
						selected = (CheckBox)event.getSource();
					}else{
						selected=null;
					}
				}
			});
			
			tblView.addRow(box, new InlineLabel(activity.getName()),new InlineLabel("CREATED"),
					new InlineLabel("0%"), new InlineLabel("N/A"), new InlineLabel(activity.getBudgetAmount()==null? null: activity.getBudgetAmount()+""));
		}
	}

	private void createGrid() {
		List<String> names = new ArrayList<String>();
		names.add("");
		names.add("TITLE");
		names.add("STATUS");
		names.add("PROGRESS");
		names.add("RATING");
		names.add("BUDGET");
		tblView.setHeaders(names);

		
	}

}
