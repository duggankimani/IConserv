package com.wira.pmgt.client.ui.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.TableView;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class ActivitiesTable extends Composite {

	private static ActivitiesTableUiBinder uiBinder = GWT
			.create(ActivitiesTableUiBinder.class);

	interface ActivitiesTableUiBinder extends UiBinder<Widget, ActivitiesTable> {
	}

	@UiField TableView tblView;
	CheckBox selected = null;
	boolean isSummaryTable;
	List<FundDTO> funds = new ArrayList<FundDTO>();
	
	public ActivitiesTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setStriped(true);
	}

	public void setData(List<IsProgramActivity> programActivities) {
		tblView.clearRows();
		setActivities(programActivities);
	}
	
	private void setActivities(List<IsProgramActivity> programActivities){
		sort(programActivities);
		for (IsProgramActivity activity : programActivities) {
			ActivitiesTableRow row = new ActivitiesTableRow(activity,isSummaryTable);
			row.setFunding(funds);
			row.setSelectionChangeHandler(handler);
			tblView.addRow(row);
			
			if(activity.getType()==ProgramDetailType.PROGRAM){
				//this is data for the summary tab
				if(activity.getObjectives()!=null){
					sort(activity.getObjectives());
					setActivities(activity.getObjectives());
				}
			}
			
			if(activity.getChildren()!=null)
				setActivities(activity.getChildren());
		}
	}

	private void sort(List<IsProgramActivity> objectives) {
		Collections.sort(objectives, new Comparator<IsProgramActivity>(){
			@Override
			public int compare(IsProgramActivity o1, IsProgramActivity o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createGrid();
	}
	
	private void createGrid() {
		List<String> names = new ArrayList<String>();
		if(isSummaryTable){
			names.add("");
			names.add("TITLE");
			names.add("BUDGET");
		}else{
			names.add("");
			names.add("TITLE");
			names.add("STATUS");
			names.add("PROGRESS");
			names.add("RATING");
			names.add("BUDGET");
		}
		tblView.setHeaders(names);
	}
	
	public void setSummaryTable(boolean isSummaryTable){
		this.isSummaryTable=isSummaryTable;
	}

	ValueChangeHandler<Boolean> handler = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			boolean isSelected = event.getValue();
			if (isSelected) {
				if (selected != null) {
					selected.setValue(false);
				}

				selected = (CheckBox) event.getSource();
			} else {
				selected = null;
			}
		}
	};

	/**
	 * Dynamically add fund names
	 * @param funds
	 */
	public void setFunds(List<FundDTO> funds) {
		this.funds= funds;
		createGrid();
		for(FundDTO fund: funds){
			tblView.createHeader(fund.getName());
		}
	}
}
