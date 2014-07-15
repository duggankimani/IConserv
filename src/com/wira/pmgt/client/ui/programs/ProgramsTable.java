package com.wira.pmgt.client.ui.programs;

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
import com.wira.pmgt.shared.model.program.IsProgramDetail;

public class ProgramsTable extends Composite {

	private static ActivitiesTableUiBinder uiBinder = GWT
			.create(ActivitiesTableUiBinder.class);

	interface ActivitiesTableUiBinder extends UiBinder<Widget, ProgramsTable> {
	}

	@UiField
	TableView tblView;
	CheckBox selected = null;
	boolean isSummaryTable = false;
	boolean isGoalsTable = false;
	List<FundDTO> funds = new ArrayList<FundDTO>();
	Long lastUpdatedId = null;
	private Long programId = null;

	public ProgramsTable() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setData(List<IsProgramDetail> programActivities) {
		tblView.clearRows();
		if (programActivities == null) {
			return;
		}
		setActivities(programActivities, -1);
	}

	private void setActivities(List<IsProgramDetail> programActivities,
			int level) {
		level++;
		sort(programActivities);
		for (IsProgramDetail activity : programActivities) {
			ProgramsTableRow row = new ProgramsTableRow(activity, funds,
					programId, isSummaryTable, isGoalsTable, level);
			if (activity.getId() == lastUpdatedId) {
				row.highlight();
			}

			row.setSelectionChangeHandler(handler);
			tblView.addRow(row);

			if (activity.getType() == ProgramDetailType.PROGRAM) {
				// this is data for the summary tab
				if (activity.getProgramOutcomes() != null) {
					sort(activity.getProgramOutcomes());
					setActivities(activity.getProgramOutcomes(), level);
				}
			}

			if (activity.getChildren() != null
					&& !activity.getChildren().isEmpty()) {
				setActivities(activity.getChildren(), level);
			}
		}
	}

	private void sort(List<IsProgramDetail> objectives) {
		Collections.sort(objectives, new Comparator<IsProgramDetail>() {
			@Override
			public int compare(IsProgramDetail o1, IsProgramDetail o2) {
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
		List<TableHeader> th = new ArrayList<TableHeader>();

		System.err.println("Goals Table >>>" + isGoalsTable);

		if (isSummaryTable) {
			th.add(new TableHeader("TITLE", 40.0));
			th.add(new TableHeader("TIMELINES", 10.0));
			th.add(new TableHeader("PROGRESS", 10.0));
			th.add(new TableHeader("BUDGET", 10.0));

		} else if (isGoalsTable) {
			th.add(new TableHeader("TITLE", 100.0));
		} else {
			th.add(new TableHeader("TITLE", 30.0));
			th.add(new TableHeader("TIMELINES", 10.0));
			th.add(new TableHeader("STATUS", 10.0));
			th.add(new TableHeader("PROGRESS", 10.0));
			th.add(new TableHeader("BUDGET", 10.0));
		}
		tblView.setTableHeaders(th);
	}

	private void setSummaryTable(boolean isSummaryTable) {
		this.isSummaryTable = isSummaryTable;
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
	 * 
	 * @param funds
	 */
	public void setFunds(List<FundDTO> funds) {
		this.funds = funds;
		Collections.sort(funds, new Comparator<FundDTO>() {
			@Override
			public int compare(FundDTO o1, FundDTO o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		if (!isGoalsTable) {
		createGrid();
			for (FundDTO fund : funds) {
				tblView.createHeader(fund.getName() + "(Ksh)", "10%");
			}
		}
	}

	public void setLastUpdatedId(Long lastUpdatedId) {
		this.lastUpdatedId = lastUpdatedId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
		setSummaryTable(programId == null);
	}

	public void setGoalsTable(boolean isGoalTable) {
		this.isGoalsTable = isGoalTable;
	}
}
