package com.wira.pmgt.client.ui.programs;

import static com.wira.pmgt.client.ui.util.NumberUtils.CURRENCYFORMAT;
import static com.wira.pmgt.client.ui.util.NumberUtils.NUMBERFORMAT;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.ProgressBar;
import com.wira.pmgt.client.ui.component.RowWidget;
import com.wira.pmgt.client.ui.events.ActivitySelectionChangedEvent;
import com.wira.pmgt.client.ui.events.ProgramDeletedEvent;
import com.wira.pmgt.client.ui.events.ProgramDeletedEvent.ProgramDeletedHandler;
import com.wira.pmgt.client.ui.events.ProgramDetailSavedEvent;
import com.wira.pmgt.client.ui.events.ProgramDetailSavedEvent.ProgramDetailSavedHandler;
import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramStatus;

public class ProgramsTableRow extends RowWidget implements
		ProgramDetailSavedHandler, ProgramDeletedHandler {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, ProgramsTableRow> {
	}

	@UiField
	HTMLPanel row;

	@UiField
	SpanElement divRowStrip;
	@UiField
	Anchor divRowCaret;

	@UiField
	Anchor divName;

	// @UiField HTMLPanel divRowNo;
	@UiField
	HTMLPanel divStatus;
	@UiField
	HTMLPanel divProgress;

	@UiField
	HTMLPanel divDates;

	@UiField
	ProgressBar progressBar;

	@UiField
	HTMLPanel divTimelines;

	@UiField
	DivElement spanStartMonth;
	@UiField
	DivElement spanStartDay;

	@UiField
	DivElement spanEndMonth;
	@UiField
	DivElement spanEndDay;

	@UiField
	HTMLPanel divBudget;
	@UiField
	HTMLPanel divCheckbox;
	
	@UiField
	InlineLabel divDesc;
	
	@UiField
	CheckBox chkSelect;

	@UiField
	HTMLPanel popOverDiv;

	@UiField
	SpanElement spnStatus;

	// @UiField SpanElement spnProgress;

	int level = 0;
	long programId = 0;
	IsProgramDetail activity;
	List<FundDTO> donors = null;

	// Is the current status showing children or not
	boolean showingChildren = true;

	// Programs Summary Grid or Program Details
	boolean isSummaryRow = false;

	List<HTMLPanel> allocations = new ArrayList<HTMLPanel>();

	Timer timer = new Timer() {

		@Override
		public void run() {
			highlight(false);
		}
	};

	private boolean isGoalsTable;

	public ProgramsTableRow(IsProgramDetail activity,
			List<FundDTO> sortedListOfFunding, Long programId,
			boolean isSummaryRow, boolean isGoalsTable, int level) {

		initWidget(uiBinder.createAndBindUi(this));
		this.isGoalsTable = isGoalsTable;
		this.isSummaryRow = isSummaryRow;
		this.activity = activity;
		this.programId = (programId == null ? 0 : programId);
		this.level = level;
		this.donors = sortedListOfFunding;

		this.showingChildren = (level == 0);
		// Show/ hide this details based on its level on load

		List<IsProgramDetail> children = activity.getChildren();
		if (activity.getType() == ProgramDetailType.PROGRAM) {
			children = activity.getProgramOutcomes();
		}

		this.showingChildren = false;// Programs shouldnt initially show

		// objectives
		if (level > 0) {
			// Only show level 0 and level 1 items - Hide all the rest
			show(false);
			setShowingChildren(false);
		}

		divRowCaret.setVisible((children != null) && (children.size() > 0));

		// Bind Row to Table
		setRow(row);

		// Initialize table details
		init();

	}

	public void init() {
		// rating.setValue(3);
		// Program/Task status - Created, Started, Done, Closed etc
		setStatus();

		// Name - Highlighting provided based on child level
		setActivityName();

		// Padding based on the child level
		setPadding();

		// set widths for the tables
		// setRowWidths();

		// Show different cols based on whether this is a program summary
		// listing or program details
		if (isSummaryRow) {
			// divProgress.setStyleName("hide");
			hide(divStatus, true);
		} else if (isGoalsTable) {
			hide(divStatus, true);
			hide(divTimelines, true);
			hide(divProgress, true);
			hide(divBudget, true);
		} else {
			if (activity.getChildren() == null
					|| activity.getChildren().isEmpty()) {
				divRowCaret.addStyleName("hide");
			}
		}

		// Budgeting & Allocations information
		String budgetAmount = activity.getBudgetAmount() == null ? ""
				: CURRENCYFORMAT.format(activity.getBudgetAmount());
		divBudget.getElement().setInnerText(
				activity.getType() == ProgramDetailType.OBJECTIVE ? ""
						: budgetAmount);

		if (activity.getType() == ProgramDetailType.OUTCOME && (isSummaryRow)) {
			chkSelect.addStyleName("hide");
		}

		chkSelect.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				AppContext.fireEvent(new ActivitySelectionChangedEvent(
						ProgramsTableRow.this.activity, event.getValue()));
			}
		});

		// set Timeline
		setTimeline();

		// Set Funding
		setFunding();
	}

	private void hide(HTMLPanel divPanel, boolean show) {
		if (show) {
			divPanel.setStyleName("hide");
			divPanel.setWidth("0%");
		} else {
			divPanel.removeStyleName("hide");
			divPanel.setWidth("10%");
		}
	}

	private void setTimeline() {
		if (activity.getStartDate() != null) {
			divTimelines.removeStyleName("hide");
			divDates.removeStyleName("hide");
			spanStartDay.setInnerText(DateUtils.DAYSHORTFORMAT.format(activity
					.getStartDate()));
			spanStartMonth.setInnerText(DateUtils.MONTHSHORTFORMAT
					.format(activity.getStartDate()));

			spanEndDay.setInnerText(DateUtils.DAYSHORTFORMAT.format(activity
					.getEndDate()));
			spanEndMonth.setInnerText(DateUtils.MONTHSHORTFORMAT
					.format(activity.getEndDate()));
		}
	}

	private void setActivityName() {
		divName.getElement().setInnerText(activity.getName());

		if (activity.getStartDate() != null && activity.getEndDate() != null) {
			// divName.setTitle(DateUtils.HALFDATEFORMAT.format(activity
			// .getStartDate())
			// + " - "
			// + DateUtils.HALFDATEFORMAT.format(activity.getEndDate()));

			// divName.setTitle(activity.getDescription());
		}
		if (activity.getType() == ProgramDetailType.OBJECTIVE
				|| activity.getType() == ProgramDetailType.OUTCOME) {
			// System.err.println("This is an objective");
			if (activity.getProgramId() != null) {
				divName.setHref("#home;page=activities;activity="
						+ activity.getProgramId() + "d" + activity.getId());
			} else {
				divName.getElement().removeAttribute("href");
				divName.addStyleName("no-link");
			}

		} else if (isSummaryRow
				&& activity.getType() == ProgramDetailType.PROGRAM) {
			// Summary table
			divName.setHref("#home;page=activities;activity="
					+ activity.getId());
		} else {
			divName.setHref("#home;page=activities;activity=" + programId + "d"
					+ activity.getId());
		}

		divRowStrip.addClassName("label-info");

		if (activity.getType() == ProgramDetailType.OBJECTIVE) {
			divName.getElement().setInnerText(
					activity.getName() + " - " + activity.getDescription());
		}
		if (activity.getType() == ProgramDetailType.OUTCOME) {
			divDesc.setText(activity.getDescription());
		}
		if (level == 0) {
			divName.addStyleName("bold");
		}

	}

	public IsProgramDetail getActivity() {
		return activity;
	}

	ValueChangeHandler<Boolean> selectionHandler;

	public void setSelectionChangeHandler(ValueChangeHandler<Boolean> handler) {
		this.selectionHandler = handler;
		chkSelect.addValueChangeHandler(handler);
	}

	/*
	 * Sets the status var statusType: danger-red, warning-golden, info - bluish
	 */
	public void setStatus() {
		ProgramStatus status = activity.getStatus();

		if (status == null) {
			status = ProgramStatus.CREATED;
		}

		spnStatus.setInnerText(status.getDisplayName());

		String type = "info";
		switch (status) {
		case CREATED:
			type = "default";
			break;
		case OPENED:
			type = "info";
			if (hasChildren()) {
				spnStatus.setInnerText("In Progress");
			}
			break;
		case COMPLETED:
			type = "info";
			break;
		case REOPENED:
			type = "info";
			break;
		case CLOSED:
			type = "success";
			break;

		}
		spnStatus.addClassName("label-" + type);
		if (activity.getProgress() != null) {
			progressBar.setValue(activity.getProgress().intValue());
			progressBar.setText(activity.getProgress().intValue() + "%");
		}
		// spnProgress.setInnerText(activity.getProgress().intValue()+"%");

		if (status != ProgramStatus.CLOSED) {
			if (activity.isOverdue()) {
				spnStatus.setClassName("label label-danger");
				// divDates.addStyleName("text-danger");
				spnStatus
						.setTitle("This "
								+ activity.getType().getDisplayName()
								+ " is Overdue ("
								+ DateUtils.MONTHDAYFORMAT.format(activity
										.getEndDate()) + ")");
			} else if (activity.isNotStarted()) {
				spnStatus.setClassName("label label-warning");
				spnStatus.setTitle("This "
						+ activity.getType().getDisplayName()
						+ " should have started by "
						+ DateUtils.MONTHDAYFORMAT.format(activity
								.getStartDate()));
				// divDates.addStyleName("text-warning");
			} else if (activity.isUpcoming()) {
				spnStatus.setTitle("This "
						+ activity.getType().getDisplayName()
						+ " is coming up soon ("
						+ DateUtils.MONTHDAYFORMAT.format(activity
								.getStartDate()) + ")");
			} else {
				// its ongoing - Work in progress (CREATED, OPEN, REOPENED)
				// spnStatus.setClassName("label label-info");
				// divDates.addStyleName("text-success");
			}
		} else {
			spnStatus.setClassName("label label-success");

		}

	}

	private boolean hasChildren() {
		return !(activity.getChildren() == null || activity.getChildren()
				.isEmpty());
	}

	@Override
	public void setRowNumber(int number) {
		// divRowNo.getElement().setInnerText(""+number);
	}

	private void setPadding() {
		if (level > 0) {

			if (level == 2) {
				divRowStrip.addClassName("label-info");
			}

			divCheckbox.getElement().getStyle()
					.setPaddingLeft(level * 40.0, Unit.PX);
			divRowStrip.removeClassName("label-info");
			divRowStrip.addClassName("label-default");
		}
	}

	private void setFunding() {
		List<ProgramFundDTO> activityFunding = activity.getFunding();

		List<FundDTO> activitySourceOfFunds = new ArrayList<FundDTO>();
		for (ProgramFundDTO dto : activityFunding) {
			activitySourceOfFunds.add(dto.getFund());
		}

		for (FundDTO programFund : donors) {
			int idx = activitySourceOfFunds.indexOf(programFund);

			if (idx == -1) {
				// Add empty to table td
				createTd(new InlineLabel(""), "10%");

			} else {
				ProgramFundDTO activityFund = activityFunding.get(idx);
				HTMLPanel amounts = new HTMLPanel("");
				String amount = activityFund.getAmount() == null ? ""
						: NUMBERFORMAT.format(activityFund.getAmount());
				amounts.add(new InlineLabel(amount));

				Double allocation = activityFund.getAllocation();

				if (allocation != null && allocation != 0.0) {
					Double diff = activityFund.getAmount() - allocation;
					HTMLPanel allocationPanel = new HTMLPanel(
							NUMBERFORMAT.format(diff));
					allocationPanel.setTitle("Remaining amount");
					allocationPanel.getElement().getStyle()
							.setFontSize(0.8, Unit.EM);

					// allocationPanel.addStyleName("underline");
					if (allocation > activityFund.getAmount()) {
						allocationPanel.addStyleName("text-error bold");
					} else {
						allocationPanel.addStyleName("text-success bold");
					}
					allocationPanel.getElement().getStyle()
							.setFontSize(0.8, Unit.EM);
					amounts.add(allocationPanel);
					allocations.add(allocationPanel);
					allocationPanel.setVisible(showingChildren || isSummaryRow);
				}
				// Add to table td
				createTd(amounts, "10%");
			}

		}
	}

	public void highlight() {
		// spnName.getElement().getStyle().setBackgroundColor("green");
		highlight(true);
	}

	private void highlight(boolean status) {
		if (status) {
			timer.schedule(2000);
			row.addStyleName("hovered");
		} else {
			row.removeStyleName("hovered");
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		addRegisteredHandler(ProgramDetailSavedEvent.TYPE, this);
		addRegisteredHandler(ProgramDeletedEvent.TYPE, this);
		divRowCaret.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Toggle
				toggle(!showingChildren);
			}
		});
	}

	private void setShowingChildren(boolean hasChildren) {
		// divRowCaret.setHref("#home;page=activities;activity="+programId+"d"+activity.getId());
		if (hasChildren) {
			divRowCaret.removeStyleName("icon-caret-right");
			divRowCaret.addStyleName("icon-caret-down");
		} else {
			divRowCaret.removeStyleName("icon-caret-down");
			divRowCaret.addStyleName("icon-caret-right");
		}

		if (level != 0)
			showAllocations(hasChildren);
	}

	// default toggle
	private void toggle(boolean isShowChildren) {
		this.showingChildren = isShowChildren;

		FlowPanel panel = (FlowPanel) this.getParent();
		int idx = panel.getWidgetIndex(this);
		assert idx != -1;

		int childCount = activity.getChildren() == null ? 0 : activity
				.getChildren().size();

		if (activity.getType() == ProgramDetailType.PROGRAM) {
			// summary table
			childCount = activity.getProgramOutcomes() == null ? 0 : activity
					.getProgramOutcomes().size();
		}

		setShowingChildren(isShowChildren);
		if (childCount == 0) {
			return;
		}

		int childrenCollapsed = 0;
		// loop until you count n children
		for (int i = idx + 1; (i < panel.getWidgetCount() && childrenCollapsed < childCount); i++) {
			ProgramsTableRow row = (ProgramsTableRow) panel.getWidget(i);
			System.err.println("Showing child : " + showingChildren);
			// if (row.getActivity().getParentId() == activity.getId()) {
			childrenCollapsed++;
			if (!showingChildren) {
				// toggle children of children only when collapsing
				row.toggle(showingChildren);
			}
			row.show(showingChildren);
			// }

		}
	}

	/**
	 * Might be called directly by parent
	 * 
	 * @param hide
	 */
	private void show(boolean show) {
		// showChildren=show; //Synchronize states with caller
		row.setStyleName(show ? "tr" : "hide");
	}

	private void showAllocations(boolean showChildren) {
		for (HTMLPanel widget : allocations) {
			widget.setVisible(showChildren);
		}
	}

	/**
	 * This method is called on Task or Activity save/update to update the user
	 * interface. {@link ProgramsPresenter#save}
	 * <p>
	 * Notes:<br/>
	 * This event is fired twice to update the child and parent row<br/>
	 * i.e.<br/>
	 * --Activity 10<br/>
	 * ----Task 11<br/>
	 * <br>
	 * If Task 10 is updated(e.g by changing budgets), Activity 10 && Task 11
	 * will both be updated, therefore the UI would need to reflect this new
	 * change
	 * 
	 * @param event
	 *            ProgramDetailSavedEvent
	 */
	@Override
	public void onProgramDetailSaved(ProgramDetailSavedEvent event) {
		IsProgramDetail updatedProgram = event.getProgram();

		if (event.isNew()
				&& ((updatedProgram.getParentId() != null && updatedProgram
						.getParentId().equals(activity.getId())) || (updatedProgram
						.getActivityOutcomeId() != null && updatedProgram
						.getActivityOutcomeId().equals(activity.getId())))) {

			List<IsProgramDetail> children = activity.getChildren();
			if (updatedProgram.getType() == ProgramDetailType.ACTIVITY) {
				children = activity.getProgramOutcomes();
			}

			// check if this is the parent
			if (children == null) {
				children = new ArrayList<IsProgramDetail>();
			}

			children.add(updatedProgram);

			if (updatedProgram.getType() == ProgramDetailType.ACTIVITY) {
				activity.setProgramOutcomes(children);
			} else {
				activity.setChildren(children);
			}
			activity.sort();

			int idx = children.indexOf(updatedProgram);
			assert idx > -1;

			// insert this child at the end of the parent
			FlowPanel parent = ((FlowPanel) this.getParent());
			ProgramsTableRow newRow = new ProgramsTableRow(updatedProgram,
					donors, programId, false, false, level + 1);
			newRow.setSelectionChangeHandler(selectionHandler);

			// Position the row below the parent
			parent.insert(newRow, parent.getWidgetIndex(this) + idx + 1);
			// Indexes start from zero, but we want children to be listed from
			// parents position+1

			newRow.show(true);
			return;
		}

		// exists
		if (activity.getId().equals(updatedProgram.getId())) {
			int count = row.getWidgetCount();

			for (FundDTO programFund : donors) {
				assert remove(row.getWidget(--count));
			}

			allocations.clear();

			List<IsProgramDetail> children = this.activity.getChildren();
			this.activity = updatedProgram;
			this.activity.setChildren(children);
			init();
			highlight();
			if (event.isUpdateSource()) {
				AppContext.fireEvent(new ActivitySelectionChangedEvent(
						updatedProgram, true));
			}
		}
	}

	@Override
	public void onProgramDeleted(ProgramDeletedEvent event) {
		if (activity.getId().equals(event.getProgramId())) {
			if (activity.getChildren() != null
					&& !activity.getChildren().isEmpty()) {
				// remove children
				toggle(false);
			}

			// Widget parent = getParent();
			// System.err.println("Parent >> "+parent.getClass());
			removeFromParent();

		}
	}

}
