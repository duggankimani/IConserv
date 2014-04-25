package com.wira.pmgt.client.ui.activities;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.DoubleField;
import com.wira.pmgt.client.ui.component.TableView;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class ActivitiesTable extends Composite {

	private static ActivitiesTableUiBinder uiBinder = GWT
			.create(ActivitiesTableUiBinder.class);

	interface ActivitiesTableUiBinder extends UiBinder<Widget, ActivitiesTable> {
	}

	@UiField TableView tblView;
	CheckBox selected = null;
	
	
	public ActivitiesTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setStriped(true);
		createGrid();
	}
	
	public void setData(List<IsProgramActivity> programActivities) {
		tblView.clearRows();
		for(IsProgramActivity activity: programActivities){
			
			CheckBox box = new CheckBox();
			box.addValueChangeHandler(handler);
			
			HTMLPanel budgetPanel = new HTMLPanel("");
			InlineLabel budget = new InlineLabel(activity.getBudgetAmount()==null? null: NumberFormat.getCurrencyFormat().format(activity.getBudgetAmount()));
			budgetPanel.add(budget);
			
			budgetPanel.getElement().getStyle().setTextAlign(TextAlign.RIGHT);
			
			tblView.addRow(box, new InlineLabel(activity.getName()),new InlineLabel("CREATED"),
					new InlineLabel("0%"), new InlineLabel("N/A"), budgetPanel);
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

	
	ValueChangeHandler<Boolean> handler = new ValueChangeHandler<Boolean>() {
		
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			
			boolean isSelected = event.getValue();
			if(isSelected){
				if(selected!=null){
					selected.setValue(false);
				}
				
				selected = (CheckBox)event.getSource();
				//AppContext.fireEvent(event);
			}else{
				selected=null;
			}
		}
	};
}
