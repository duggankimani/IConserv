package com.wira.pmgt.client.ui.programs;

import static com.wira.pmgt.client.ui.programs.ProgramsPresenter.FILTER_SLOT;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.model.UploadContext;
import com.wira.pmgt.client.model.UploadContext.UPLOADACTION;
import com.wira.pmgt.client.ui.component.ActionLink;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.BulletPanel;
import com.wira.pmgt.client.ui.component.Dropdown;
import com.wira.pmgt.client.ui.component.MyHTMLPanel;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.PermissionType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;

public class ProgramsView extends ViewImpl implements
		ProgramsPresenter.IActivitiesView {

	private final Widget widget;

	@UiField
	ActionLink aExport;

	@UiField
	ActionLink aProgram;

	@UiField
	ActionLink aDeleteProgram;

	@UiField
	ActionLink aMove;

	@UiField
	HTMLPanel divMainContainer;

	@UiField
	HTMLPanel divMiddleContent;

	@UiField
	HTMLPanel divContentBottom;

	@UiField
	HTMLPanel divContentTop;

	@UiField
	HTMLPanel divNoContent;
	@UiField
	ProgramsTable tblView;

	@UiField
	HTMLPanel divFilterBox;

	@UiField
	Anchor iFilterdropdown;

	@UiField
	MyHTMLPanel divProgramsTable;

	@UiField
	ActionLink aNewOutcome;
	@UiField
	HTMLPanel panelCrumbs;
	@UiField
	ActionLink aNewActivity;
	@UiField
	ActionLink aNewObjective;
	@UiField
	ActionLink aNewTask;
	@UiField
	ActionLink aEdit;
	@UiField
	ActionLink aBack;
	@UiField
	ActionLink aAssign;
	@UiField
	ActionLink aDetail;

	@UiField
	ProgramHeader headerContainer;

	@UiField
	BulletListPanel listPanel;

	@UiField
	ActionLink aLeft;
	@UiField
	ActionLink aRight;
	Long lastUpdatedId;

	List<IsProgramDetail> programs = null;

	public interface Binder extends UiBinder<Widget, ProgramsView> {
	}

	Timer scrollTimer = new Timer() {
		@Override
		public void run() {
			listPanel.getElement().setScrollLeft(
					listPanel.getElement().getScrollLeft() + scrollDistancePX);
		}
	};

	int scrollDistancePX = 6; // 6px at a time
	private Long programId;

	protected boolean isNotDisplayed;

	private boolean isCurrentPlaceObjectivesPage = false;
	private List<IsProgramDetail> activities;
	private Long periodId;

	@Inject
	public ProgramsView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		listPanel.setId("mytab");
		// imgExport.setResource(ICONS.INSTANCE.excel());

		// registerEditFocus()
		MouseDownHandler downHandler = new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				scrollTimer.cancel();
				if (event.getSource() == aLeft) {
					scrollDistancePX = -6;
				} else {
					scrollDistancePX = 6;

				}
				scrollTimer.scheduleRepeating(20);
			}
		};

		MouseUpHandler upHandler = new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				scrollTimer.cancel();
			}
		};

		aRight.addMouseDownHandler(downHandler);
		aLeft.addMouseDownHandler(downHandler);

		aRight.addMouseUpHandler(upHandler);
		aLeft.addMouseUpHandler(upHandler);

		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scrollTimer.cancel();
				if (event.getSource() == aLeft) {
					scrollDistancePX = -20;
				} else {
					scrollDistancePX = 20;
				}
				listPanel.getElement().setScrollLeft(
						listPanel.getElement().getScrollLeft()
								+ scrollDistancePX);
			}
		};
		aRight.addClickHandler(clickHandler);
		aLeft.addClickHandler(clickHandler);

		show(aBack, false);
		show(aDetail, false);
		show(aAssign, false);
		show(aDeleteProgram, false);

		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.back();
			}
		});

		iFilterdropdown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (isNotDisplayed) {
					divFilterBox.removeStyleName("hide");
					isNotDisplayed = false;
				} else {
					divFilterBox.addStyleName("hide");
					isNotDisplayed = true;
				}
			}
		});

		aExport.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(downloadUrl, "_blank", null);
			}
		});

	}

	public void setMiddleHeight() {
		int totalHeight = divMainContainer.getElement().getOffsetHeight();
		int topHeight = divContentTop.getElement().getOffsetHeight();
		int middleHeight = totalHeight - topHeight - 43;

		if (middleHeight > 0) {
			divProgramsTable.setHeight(middleHeight + "px");
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getNewOutcome() {
		return aNewOutcome;
	}

	public HasClickHandlers getNewActivityLink() {
		return aNewActivity;
	}

	private void showContent(boolean status) {
		if (status) {
			divMiddleContent.removeStyleName("hidden");
			divNoContent.addStyleName("hidden");
			headerContainer.setTitle("Programs Management");
		} else {
			divMiddleContent.addStyleName("hidden");
			divNoContent.removeStyleName("hidden");
		}
	}

	public void createDefaultTabs() {
		listPanel.clear();
		createTab("Objectives", (periodId==null? "#home;page=objectives":"#home;page=objectives;period="+periodId), true);
		createTab("Summary", 0, true);
	}

	public void createTab(String text, long id, boolean active) {
		createTab(text, createUrl(id), active);
	}

	private String createUrl(long id) {

		return "#home;page=activities;activity=" + id
				+ (periodId == null ? "" : ";period=" + periodId);
	}

	public void createTab(String text, String url, boolean active) {
		BulletPanel li = new BulletPanel();
		ActionLink a = new ActionLink(text);

		a.setHref(url);
		li.add(a);

		if (active) {
			li.addStyleName("active");
		} else {
			li.removeStyleName("active");
		}
		listPanel.add(li);
	}

	/**
	 * Bind Table data 
	 * Programs Summary binding [Only list of programs bound
	 * here]
	 * 
	 */
	@Override
	public void setData(List<IsProgramDetail> activities) {

		if (programId == null) {
			Double totalBudget = 0.0;
			Double totalActual = 0.0;
			for (IsProgramDetail activity : activities) {
				totalBudget += activity.getBudgetAmount();
				totalActual += activity.getActualAmount();
			}
			headerContainer.setFunding(totalBudget, totalActual,
					ProgramDetailType.PROGRAM);
		}

		this.activities = activities;
		tblView.setLastUpdatedId(lastUpdatedId);
		tblView.setData(activities);
		lastUpdatedId = null;
	}

	@Override
	public void createProgramTabs(List<IsProgramDetail> programs) {
		// showContent(!(programs == null || programs.isEmpty()));
		showContent(true);
		this.programs = programs;
		/*
		 * listPanel.clear(); if (programs == null) { return; }
		 */

		createDefaultTabs();

		// System.err.println("Size = " + programs.size());
		if (programs != null)
			for (IsProgramDetail activity : programs) {
				// boolean first = programs.indexOf(activity) == 0;
				createTab(activity.getName(), activity.getId(), false);
			}
	}

	/**
	 * Sets Parent Activity
	 */
	public void setActivity(IsProgramDetail singleResult) {
		if (singleResult == null) {
			return;
		}
		setSelection(singleResult.getType(), false, canEdit(singleResult));

		if (singleResult.getPeriod() != null) {
			headerContainer.getPeriodDropdown().setValue(
					singleResult.getPeriod());
		}

		if (singleResult.getProgramSummary() != null) {
			BulletListPanel breadCrumbs = headerContainer
					.setBreadCrumbs(singleResult.getProgramSummary());
			panelCrumbs.clear();
			panelCrumbs.add(breadCrumbs);
			showBackButton(true);
		}

		if (singleResult.getType() == ProgramDetailType.OBJECTIVE) {
			headerContainer.setText("Objectives & Goals");
		}

		if (singleResult.getType() == ProgramDetailType.PROGRAM) {

			if (singleResult.getBudgetAmount() == null
					|| singleResult.getBudgetAmount() == 0l) {
				headerContainer.setTitle("No budget allocated to '"
						+ singleResult.getName() + "'");
			} else {
				headerContainer.setTitle("Total Budget for '"
						+ singleResult.getName() + "'");
			}

			// Set Funds
			headerContainer.setFunding(singleResult.getBudgetAmount(),
					singleResult.getActualAmount(), singleResult.getType());
			showBudgets(true);

			if (!tblView.isSummaryTable) {
				// select tab
				selectTab(singleResult.getId());
				headerContainer.setText(singleResult.getName());

				if (singleResult.getType() == ProgramDetailType.PROGRAM) {
					Map<Long, IsProgramDetail> outcomeActivityMap = new HashMap<Long, IsProgramDetail>();
					//this is a list of outcomes under a program
					if (singleResult.getProgramOutcomes() != null) {
						for (IsProgramDetail outcome : singleResult
								.getProgramOutcomes()) {
							//Temporarily assign program period to outcome
							outcome.setPeriod(singleResult.getPeriod());
							outcomeActivityMap.put(outcome.getId(), outcome);
						}

						List<IsProgramDetail> activities = singleResult
								.getChildren();
						if (activities != null) {
							for (IsProgramDetail activity : activities) {
								if (outcomeActivityMap.get(activity
										.getActivityOutcomeId()) != null) {
									outcomeActivityMap.get(
											activity.getActivityOutcomeId())
											.addChild(activity);
								}
							}
						}
					}
					setData(singleResult.getProgramOutcomes());
				} else {
					setData(singleResult.getChildren());
				}

				// summary Table called here
			} else {
				setData(Arrays.asList(singleResult));
			}

		} else {

			setData(Arrays.asList(singleResult));
		}

	}

	private void show(Anchor aAnchor, boolean show) {
		if (show) {
			aAnchor.getElement().getParentElement().removeClassName("hide");
		} else {
			aAnchor.getElement().getParentElement().addClassName("hide");
		}
	}

	public void selectTab(Long id) {
		selectTab(createUrl(id));
	}

	public void selectTab(String href) {
		isCurrentPlaceObjectivesPage = href.equals("#home;page=objectives");

		if (isCurrentPlaceObjectivesPage) {
			headerContainer.setText("Objectives");
		}

		int size = listPanel.getWidgetCount();
		for (int i = 0; i < size; i++) {
			BulletPanel li = (BulletPanel) listPanel.getWidget(i);

			Anchor a = (Anchor) li.getWidget(0);
			boolean active = a.getHref().endsWith(href);

			if (active) {
				li.addStyleName("active");
				li.getElement().scrollIntoView();
			} else {
				li.removeStyleName("active");
			}
		}
	}

	@Override
	public void removeTab(Long id) {
		int size = listPanel.getWidgetCount();
		for (int i = 0; i < size; i++) {
			BulletPanel li = (BulletPanel) listPanel.getWidget(i);

			Anchor a = (Anchor) li.getWidget(0);
			String href = createUrl(id);

			if (a.getHref().endsWith(href)) {
				// hide this
				li.getElement().getStyle().setDisplay(Display.NONE);
			}
		}
	}

	@Override
	public HasClickHandlers getNewObjectiveLink() {

		return aNewObjective;
	}

	@Override
	public void setSelection(ProgramDetailType type) {
		setSelection(type, false, false);
	}

	/**
	 * Programs, objectives, activities etc can be selected from the Programs
	 * table (by ticking the checkbox beside them) or by drilling down on any of
	 * them
	 * 
	 * <p>
	 * Some actions are available based on whether the element selected is
	 * actually part of the details (rows of the table) or it is the parent
	 * element whose details are displayed on the table.
	 * 
	 * <p>
	 * e.g. Creation of Objectives is only provided when a program is selected
	 * in the Summary Tab since the summary tab shows The program and its
	 * objectives, whereas, Creation of outcomes is not permitted; again since
	 * outcomes are not included here.
	 * 
	 * <p>
	 * 
	 * @param type
	 *            {@link ProgramDetailType} Type of ProgramDetail (Program/
	 *            Activity/ Outcome/Task etc)
	 * @param isRowData
	 *            true if the element was selected from one of the rows in the
	 *            table or not
	 */
	public void setSelection(ProgramDetailType type, boolean isRowData,
			boolean canEdit) {
		// System.err.println("Can Edit::" + canEdit);
		show(aProgram, false);
		show(aNewOutcome, false);
		show(aNewObjective,
				isCurrentPlaceObjectivesPage && AppContext.isCurrentUserAdmin()
						&& !isRowData);
		show(aNewActivity, false);
		show(aNewTask, false);
		show(aEdit, canEdit);
		show(aDeleteProgram, isRowData && canEdit);
		show(aAssign, false);
		show(aDetail, !isCurrentPlaceObjectivesPage && isRowData);
		show(aMove, false);

		if (type == ProgramDetailType.PROGRAM) {
			// Program can be selected from the SummaryTab == isRowData
			// or When A Program Tab e.g Wildlife Program is selected
			show(aEdit, AppContext.isCurrentUserAdmin());
			show(aDeleteProgram, isRowData && AppContext.isCurrentUserAdmin());
			show(aAssign, isRowData && AppContext.isCurrentUserAdmin());
		} else if (type == ProgramDetailType.OBJECTIVE) {
			show(aAssign, false);
			show(aNewOutcome, AppContext.isCurrentUserAdmin() && isRowData);
			show(aEdit,
					isCurrentPlaceObjectivesPage && isRowData
							&& AppContext.isCurrentUserAdmin());
		} else if (type == ProgramDetailType.OUTCOME) {
			show(aNewActivity, AppContext.isCurrentUserAdmin() && isRowData
					&& !isCurrentPlaceObjectivesPage
					&& !(programId == null || programId == 0));
			show(aAssign, false);
			show(aDetail, false);
			show(aEdit,
					isCurrentPlaceObjectivesPage && isRowData
							&& AppContext.isCurrentUserAdmin());
			show(aDeleteProgram, isRowData && AppContext.isCurrentUserAdmin()
					&& isCurrentPlaceObjectivesPage);
		} else if (type == ProgramDetailType.ACTIVITY) {
			show(aNewTask, canEdit);
			show(aAssign, false);
			show(aMove, isRowData && canEdit);
		} else if (type == ProgramDetailType.TASK) {
			// System.err.println(isRowData && canEdit);
			show(aNewTask, isRowData && canEdit);
			show(aAssign, isRowData && canEdit);
			show(aMove, isRowData && canEdit);
		} else {
			show(aDetail, false);
			show(aAssign, false);
			show(aProgram,
					!isCurrentPlaceObjectivesPage
							&& AppContext.isCurrentUserAdmin());
			show(aEdit, false);
			show(aDeleteProgram, false);
		}
	}

	@Override
	public HasClickHandlers getEditLink() {
		return aEdit;
	}

	public void setPeriods(List<PeriodDTO> periods) {
		Collections.sort(periods, new Comparator<PeriodDTO>() {
			@Override
			public int compare(PeriodDTO o1, PeriodDTO o2) {
				return -o1.getStartDate().compareTo(o2.getStartDate());
			}
		});
		headerContainer.setPeriodDropdown(periods);

		// if (periods != null && !periods.isEmpty()) {
		// setDates("(" + periods.get(0).getDescription() + ")");
		// }
	}

	public HasClickHandlers getDetailButton() {
		return aDetail;
	}

	public HasClickHandlers getDeleteButton() {
		return aDeleteProgram;
	}

	public HasClickHandlers getaMove() {
		return aMove;
	}

	@Override
	public void setFunds(List<FundDTO> funds) {
		tblView.setFunds(funds);
	}

	@Override
	public HasClickHandlers getNewTaskLink() {
		return aNewTask;
	}

	public HasClickHandlers getaAssign() {
		return aAssign;
	}

	@Override
	public void setLastUpdatedId(Long lastUpdatedId) {
		this.lastUpdatedId = lastUpdatedId;
	}

	public HasClickHandlers getAddProgramButton() {
		return aProgram;
	}

	public void showBackButton(boolean status) {
		if (status) {
			show(aBack, true);
		} else {
			show(aBack, false);
		}
	}

	@Override
	public Dropdown<PeriodDTO> getPeriodDropDown() {
		return headerContainer.getPeriodDropdown();
	}

	@Override
	public void setActivePeriod(Long activePeriod) {
		this.periodId = activePeriod;
		headerContainer.setActivePeriod(activePeriod);
		
		if (activePeriod == null) {
			List<PeriodDTO> periods = getPeriodDropDown().getSelectionValues();

			if (periods != null) {
				for (PeriodDTO period : periods) {
					if (period.isCurrentPeriod()) {
						headerContainer.getPeriodDropdown().setValue(period);
						headerContainer.setDates("(" + period.getDescription()
								+ ")");
						break;
					}
				}
			}
		} else {
			if (getPeriodDropDown().getSelectionValues() != null)
				for (PeriodDTO dto : getPeriodDropDown().getSelectionValues()) {
					if (dto.getId().equals(activePeriod)) {
						headerContainer.getPeriodDropdown().setValue(dto);
						headerContainer.setDates("(" + dto.getDescription()
								+ ")");
						break;
					}
				}

		}
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == FILTER_SLOT) {
			divFilterBox.clear();
			if (content != null) {
				divFilterBox.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}

	}

	@Override
	public void setProgramId(Long programId) {
		this.programId = programId;
		tblView.setProgramId(programId);
		headerContainer.setProgramId(programId);
	}

	@Override
	public void setGoalsTable(boolean isGoalsTable) {
		tblView.setGoalsTable(isGoalsTable);
	}

	String downloadUrl = null;

	private HashMap<Long, PermissionType> permissions;

	@Override
	public void setDownloadUrl(Long programid, Long outcomeid, Long activityId,Long period,
			String programType) {
		final UploadContext context = new UploadContext(AppContext.getBaseURL()
				+ "/getreport");
		context.setAction(UPLOADACTION.EXPORTPROGRAMS);

		if (programid != null)
			context.setContext("programid", programid + "");

		if (activityId != null)
			context.setContext("activityid", activityId + "");

		if (outcomeid != null)
			context.setContext("outcomeid", outcomeid + "");

		if (programType != null) {
			context.setContext("programType", programType);
		}
		
		if(period!=null){
			context.setContext("periodid", period+"");
		}

		downloadUrl = context.toUrl();

	}

	@Override
	public void setPermissions(HashMap<Long, PermissionType> permissions) {
		this.permissions = permissions;
		// System.err.println("Permissions = " + permissions);
	}

	@Override
	public boolean canEdit(IsProgramDetail isProgramDetail) {
		Long programId = null;
		ProgramDetailType type = isProgramDetail.getType();
		if (type == ProgramDetailType.PROGRAM) {
			programId = isProgramDetail.getId();
		} else if ((type == ProgramDetailType.ACTIVITY)
				|| (type == ProgramDetailType.TASK)) {
			programId = isProgramDetail.getProgramId();
		} else {
			return false;
		}

		PermissionType permission = permissions.get(programId);
		// System.err.println("Program Id>>" + programId + "Permission>>"
		// + permission);
		if ((permission == null || permission == PermissionType.CAN_VIEW)
				&& (!AppContext.isCurrentUserAdmin())) {
			return false;
		} else {
			return true;
		}
	}

	public void showBudgets(boolean status) {
		headerContainer.showBudgets(status);
	}
}