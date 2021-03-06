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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.ProgressBar;
import com.wira.pmgt.client.ui.component.RowWidget;
import com.wira.pmgt.client.ui.events.ActivitySelectionChangedEvent;
import com.wira.pmgt.client.ui.events.AnalysisChangedEvent;
import com.wira.pmgt.client.ui.events.AnalysisChangedEvent.AnalysisChangedHandler;
import com.wira.pmgt.client.ui.events.MoveProgramEvent;
import com.wira.pmgt.client.ui.events.MoveProgramEvent.MoveProgramHandler;
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
		ProgramDetailSavedHandler, ProgramDeletedHandler, MoveProgramHandler, AnalysisChangedHandler {

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
	Anchor aRowCaret;

	@UiField
	Anchor aName;

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
	SpanElement spnDescPopUp;

	@UiField
	SpanElement spnAssigned;

	@UiField
	SpanElement spnLabel;

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
	
	@UiField HTMLPanel divBudgetLine;
	
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

	ValueChangeHandler<Boolean> selectionHandler;

	private AnalysisType type;

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

		aRowCaret.setVisible((children != null) && (children.size() > 0));

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

		// Set Assignment
		setAssignment();
		
		if(isSummaryRow || isGoalsTable){
			show(divBudgetLine, false);
		}else{
			divBudgetLine.getElement().setInnerText(activity.getBudgetLine());
		}
		

		// set widths for the tables
		// setRowWidths();

		// Show different cols based on whether this is a program summary
		// listing or program details
		if (isSummaryRow) {
			show(divStatus, false);
		} else if (isGoalsTable) {
			show(divStatus, false);
			show(divTimelines, false);
			show(divProgress, false);
			show(divBudget, false);
		} else {
			if (activity.getChildren() == null
					|| activity.getChildren().isEmpty()) {
				aRowCaret.addStyleName("hide");
			}
		}

		// Budgeting & Allocations information
		String budgetAmount = activity.getBudgetAmount() == null ? ""
				: CURRENCYFORMAT.format(activity.getBudgetAmount());

		if ((activity.getType() == ProgramDetailType.OBJECTIVE)
				|| (activity.getType() == ProgramDetailType.OUTCOME)) {
			divBudget.getElement().setInnerText("");

		} else {
			divBudget.getElement().setInnerText(budgetAmount);
		}

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

	private void show(HTMLPanel divPanel, boolean show) {
		if (show) {
			divPanel.removeStyleName("hide");
			divPanel.setWidth("10%");
		} else {
			divPanel.setStyleName("hide");
			divPanel.setWidth("0%");
		}
	}

	private void setTimeline() {
		if (activity.getType() == ProgramDetailType.OUTCOME) {
			return;
		}

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

		if (activity.getStatus() != ProgramStatus.CLOSED) {
			if (activity.isOverdue()) {
				divDates.setStyleName("text-danger");
				// divDates.addStyleName("text-danger");
				divDates.setTitle("This "
						+ activity.getType().getDisplayName()
						+ " is Overdue ("
						+ DateUtils.MONTHDAYFORMAT.format(activity.getEndDate())
						+ ")");
			} else if (activity.isNotStarted()) {
				divDates.setStyleName("text-info");
				divDates.setTitle("This "
						+ activity.getType().getDisplayName()
						+ " should have started by "
						+ DateUtils.MONTHDAYFORMAT.format(activity
								.getStartDate()));
				// divDates.addStyleName("text-warning");
			} else if (activity.isUpcoming()) {
				divDates.setStyleName("text-warning");
				divDates.setTitle("This "
						+ activity.getType().getDisplayName()
						+ " is coming up soon ("
						+ DateUtils.MONTHDAYFORMAT.format(activity
								.getStartDate()) + ")");
			} else {
				divDates.setStyleName("text-success");
				divDates.setTitle("This task is On-going");
			}
		} else {
			// spnStatus.setClassName("label label-success");

		}
	}

	private void setActivityName() {
		aName.getElement().setInnerText(activity.getName());

		if (activity.getStartDate() != null && activity.getEndDate() != null) {
			// divName.setTitle(DateUtils.HALFDATEFORMAT.format(activity
			// .getStartDate())
			// + " - "
			// + DateUtils.HALFDATEFORMAT.format(activity.getEndDate()));

			// divName.setTitle(activity.getDescription());
		}
		if (activity.getType() == ProgramDetailType.OBJECTIVE) {
			aName.getElement().removeAttribute("href");
			aName.addStyleName("no-link");
		} else if (activity.getType() == ProgramDetailType.OUTCOME) {

			if (activity.getDescription() != null) {
				aName.setTitle(activity.getDescription());
				divCheckbox.setTitle(activity.getDescription());
			}

			if (activity.getProgramId() != null) {
				aName.setHref("#home;page=activities;activity="
						+ activity.getProgramId()
						+ "O"
						+ activity.getId()
						+ (activity.getPeriod() != null ? ";period="
								+ activity.getPeriod().getId() : ""));
			} else {
				aName.getElement().removeAttribute("href");
				aName.addStyleName("no-link");
			}

		} else if (isSummaryRow
				&& activity.getType() == ProgramDetailType.PROGRAM) {
			// Summary table
			aName.setHref("#home;page=activities;activity="
					+ activity.getId()
					+ (activity.getPeriod() != null ? ";period="
							+ activity.getPeriod().getId() : ""));
		} else {
			aName.setHref("#home;page=activities;activity="
					+ programId
					+ "d"
					+ activity.getId()
					+ (activity.getPeriod() != null ? ";period="
							+ activity.getPeriod().getId() : ""));
		}

		if (activity.getType() == ProgramDetailType.OBJECTIVE) {
			aName.getElement().setInnerText(
					activity.getName() + " - " + activity.getDescription());
		}
		if (activity.getType() == ProgramDetailType.OUTCOME) {
			String text = activity.getDescription();
			// System.err.println("Length>>" + text.length());
			if (text.length() > 100) {
				String newText = text.substring(0, 90);
				divDesc.setText(newText + "...");
			} else {
				divDesc.setText(text);
			}

			divDesc.setTitle(text);
			spnDescPopUp.setInnerText(text);
		}
		if (level == 0) {
			aName.addStyleName("bold");
		}

	}

	public IsProgramDetail getActivity() {
		return activity;
	}

	public void setSelectionChangeHandler(ValueChangeHandler<Boolean> handler) {
		this.selectionHandler = handler;
		chkSelect.addValueChangeHandler(handler);
	}

	/*
	 * Sets the status var statusType: danger-red, warning-golden, info - bluish
	 */
	public void setStatus() {
		ProgramStatus status = activity.getStatus();
		if (activity.getType() == ProgramDetailType.OUTCOME) {
			spnStatus.addClassName("hide");
		}

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

		if (activity.getType() == ProgramDetailType.OUTCOME) {
			progressBar.addStyleName("hide");
		} else if (activity.getProgress() != null) {
			progressBar.setValue(activity.getProgress().intValue());
			progressBar.setText(activity.getProgress().intValue() + "%");
		}

		// spnProgress.setInnerText(activity.getProgress().intValue()+"%");
	}

	private boolean hasChildren() {
		return !(activity.getChildren() == null || activity.getChildren()
				.isEmpty());
	}

	@Override
	public void setRowNumber(int number) {
		// divRowNo.getElement().setInnerText(""+number);
	}

	private void setAssignment() {
		ProgramDetailType type = activity.getType();

		if ((type == ProgramDetailType.OUTCOME)
				|| (type == ProgramDetailType.OBJECTIVE)
				|| !activity.isAssigned()) {
			spnAssigned.addClassName("hide");
			divRowStrip.getStyle().setPaddingRight(0.0, Unit.PX);
		} else {
			divRowStrip.setTitle(type.getDisplayName() + " has been assigned.");
		}

	}

	private void setPadding() {
		switch (activity.getType()) {

		/* Set color for ProgramTypes */
		case PROGRAM:
			divRowStrip.addClassName("label-success");
			break;
		case OUTCOME:
			divRowStrip.addClassName("label-info");
			break;
		case OBJECTIVE:
			divRowStrip.addClassName("label-warning");
			break;
		case ACTIVITY:
			divRowStrip.addClassName("label-success");
			break;
		case TASK:
			divRowStrip.addClassName("label-default");
			break;
		default:
			divRowStrip.addClassName("label-default");
			break;
		}

		String firstName = activity.getType().getDisplayName().substring(0, 1);

		if (activity.getType() == ProgramDetailType.OBJECTIVE) {
			firstName = "B";
		}
		spnLabel.setInnerText(firstName);

		// Text Padding
		if (level > 0) {
			divCheckbox.getElement().getStyle()
					.setPaddingLeft(level * 40.0, Unit.PX);
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
				
				//Budget Amount
				Double budgetAmount = activityFund.getAmount(); 
				String amount = budgetAmount == null ? ""
						: NUMBERFORMAT.format(budgetAmount);
				amounts.add(new InlineLabel(amount));

				//Allocation Or Actual Amount
				Double allocationOrActual = null;
				
				if(type==AnalysisType.ACTUAL){
					allocationOrActual = activityFund.getActual();
				}else{
					allocationOrActual = activityFund.getAllocation();
				}
				
				if (allocationOrActual != null && allocationOrActual != 0.0
						&& activityFund.getAmount() != null) {

					Double diff = budgetAmount - allocationOrActual;
					if(diff!=null && diff.intValue()==0){
						diff=0.0;
					}
					
					HTMLPanel allocationPanel = new HTMLPanel(
							NUMBERFORMAT.format(diff));
					if(type==AnalysisType.ACTUAL){
						allocationPanel.setTitle("Remaining amount after actual expenditure");
					}else{
						allocationPanel.setTitle("Unallocated budget amount");
					}
					
					allocationPanel.getElement().getStyle()
							.setFontSize(0.8, Unit.EM);

					// allocationPanel.addStyleName("underline");
					if (allocationOrActual > budgetAmount) {
						allocationPanel.addStyleName("text-error bold");
					} else {
						allocationPanel.addStyleName("text-success bold");
					}
					allocationPanel.getElement().getStyle()
							.setFontSize(0.8, Unit.EM);
					amounts.add(allocationPanel);
					allocations.add(allocationPanel);
					//allocationPanel.setVisible(isSummaryRow);
					//allocationPanel.setVisible(showingChildren || isSummaryRow);
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
		addRegisteredHandler(MoveProgramEvent.TYPE, this);
		addRegisteredHandler(AnalysisChangedEvent.TYPE, this);
		aRowCaret.addClickHandler(new ClickHandler() {

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
			aRowCaret.removeStyleName("icon-caret-right");
			aRowCaret.addStyleName("icon-caret-down");
		} else {
			aRowCaret.removeStyleName("icon-caret-down");
			aRowCaret.addStyleName("icon-caret-right");
		}

//		if (level != 0)
//			showAllocations(hasChildren);
	}

	// default toggle
	private void toggle(boolean isShowChildren) {
		this.showingChildren = isShowChildren;

		FlowPanel panel = (FlowPanel) this.getParent();
		int idx = panel.getWidgetIndex(this);
		assert idx != -1;

		int childCount = activity.getChildren() == null ? 0 : activity
				.getChildren().size();

		ProgramDetailType activityType = activity.getType();
		if (activityType == ProgramDetailType.PROGRAM) {
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
			Long childParentId = row.getActivity().getParentId();
			Long childOutcomeId = row.getActivity().getActivityOutcomeId();
			Long parentProgramId = row.getActivity().getProgramId();

			boolean toggle = false;
			if (activityType.equals(ProgramDetailType.PROGRAM)
					&& parentProgramId != null
					&& parentProgramId.equals(activity.getId())) {
				toggle = true;
			} else if (activityType.equals(ProgramDetailType.OUTCOME)
					&& childOutcomeId != null
					&& childOutcomeId.equals(activity.getId())) {
				toggle = true;
			} else if (childParentId != null
					&& childParentId.equals(activity.getId())) {
				toggle = true;
			}

			if (toggle) {
				childrenCollapsed++;
				if (!showingChildren) {
					// toggle children of children only when collapsing
					row.toggle(showingChildren);
				}
				row.show(showingChildren);
			}

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

			updatedProgram.setProgramId(programId);
			addChild(updatedProgram);
			return;
		}

		// exists
		if (activity.getId().equals(updatedProgram.getId())) {
			clearFunds();
			List<IsProgramDetail> children = this.activity.getChildren();
			updatedProgram.setProgramId(programId);
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

	private void clearFunds() {
		int count = row.getWidgetCount();
		for (FundDTO programFund : donors) {
			Widget widgetToRemove = row.getWidget(--count);
			boolean removed = remove(widgetToRemove);
			if (!removed) {
				Window.alert("Cannot remove Widget in index >> " + count
						+ "; Row.isAttached=" + isAttached()
						+ ";  WidgetToRemove >>" + widgetToRemove);
			}
			// else{
			// Window.alert("Removed Widget in index >> "+count
			// +"; Row.isAttached="+isAttached()
			// +"; RemovedWidget >>"+widgetToRemove);
			//
			// }
		}

		allocations.clear();
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

	@Override
	public void onMoveProgram(MoveProgramEvent event) {
		Long previousParentId = event.getPreviousParentId();
		Long newParentId = event.getNewParentId();

		if (activity.getId().equals(previousParentId)) {
			// we need to remove moved item from this parent
			removeChild(event.getItemMoved());
		}

		if (activity.getId().equals(newParentId)) {
			// we need to add moved item to this parent
			addChild(event.getItemMoved());
		}

	}

	private void addChild(IsProgramDetail newItem) {

		List<IsProgramDetail> children = activity.getChildren();
		// check if this is the parent
		if (children == null) {
			children = new ArrayList<IsProgramDetail>();
		}

		children.add(newItem);

		activity.setChildren(children);
		activity.sort();

		int idx = children.indexOf(newItem);
		assert idx > -1;

		// insert this child at the end of the parent
		FlowPanel parent = ((FlowPanel) this.getParent());
		ProgramsTableRow newRow = new ProgramsTableRow(newItem, donors,
				programId, false, false, level + 1);
		newRow.setSelectionChangeHandler(selectionHandler);

		// Position the row below the parent
		parent.insert(newRow, parent.getWidgetIndex(this) + idx + 1);
		// Indexes start from zero, but we want children to be listed from
		// parents position+1

		toggle(true);
		// newRow.show(true);

	}

	private void removeChild(IsProgramDetail itemMoved) {
		FlowPanel parent = ((FlowPanel) this.getParent());
		int parentIdx = parent.getWidgetIndex(this);

		int idx = activity.getChildren().indexOf(itemMoved);
		assert idx > -1;
		// remove the item from activity
		activity.getChildren().remove(idx);

		int childIndex = parentIdx + idx + 1;
		removeChild(childIndex, itemMoved);
		removeRecursive(childIndex, itemMoved.getChildren());
	}

	private void removeRecursive(int parentIdx, List<IsProgramDetail> children) {
		if (children == null) {
			return;
		}

		for (IsProgramDetail child : children) {
			removeChild(parentIdx, child);
			removeRecursive(parentIdx, child.getChildren());
		}
	}

	private void removeChild(int widgetIndex, IsProgramDetail child) {
		FlowPanel parent = ((FlowPanel) this.getParent());
		ProgramsTableRow childRow = (ProgramsTableRow) parent
				.getWidget(widgetIndex);
		childRow.removeFromParent();
	}

	@Override
	public void onAnalysisChanged(AnalysisChangedEvent event) {
		type = event.getAnalysisType();
		if(activity.getType().equals(ProgramDetailType.OUTCOME)
				|| activity.getType().equals(ProgramDetailType.OBJECTIVE)){
			return;
		}
		
		clearFunds();
		init();
	}

}
