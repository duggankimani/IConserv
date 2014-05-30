package com.wira.pmgt.client.ui.programs;

import java.util.Arrays;
import java.util.List;

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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.ActionLink;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.BulletPanel;
import com.wira.pmgt.client.ui.component.Dropdown;
import com.wira.pmgt.client.ui.util.NumberUtils;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;

public class ProgramsView extends ViewImpl implements
		ProgramsPresenter.IActivitiesView {

	private final Widget widget;

	@UiField
	ActionLink aProgram;

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
	com.wira.pmgt.client.ui.component.HTMLPanel divProgramsTable;

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

	@Inject
	public ProgramsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		listPanel.setId("mytab");

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

		// BreadCrumbItem item = headerContainer.createCrumb("Home", "Home", 0L,
		// false);
		// crumbContainer.add(item);

		show(aBack, false);
		show(aDetail, false);
		show(aAssign, false);

		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.back();
			}
		});

	}

	public void setMiddleHeight() {
		int totalHeight = divMainContainer.getElement().getOffsetHeight();
		int topHeight = divContentTop.getElement().getOffsetHeight();
		// int bottomHeight = divContentBottom.getElement().getOffsetHeight();
		int middleHeight = totalHeight - topHeight - 43;

		System.err.println("Total Height>>>" + totalHeight);
		System.err.println("Top Height>>>" + topHeight);

		if(middleHeight<0){
			divProgramsTable.setHeight(middleHeight + "px");
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setBudget(Double number) {
		if (number != null) {
			headerContainer
					.setBudget(NumberUtils.CURRENCYFORMAT.format(number));
		}
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

	public void createDefaultTab() {
		createTab("Summary", 0, true);
	}

	public void createTab(String text, long id, boolean active) {
		BulletPanel li = new BulletPanel();
		ActionLink a = new ActionLink(text);

		a.setHref("#home;page=activities;activity=" + id);
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
	 */
	@Override
	public void setData(List<IsProgramDetail> activities) {
		tblView.setLastUpdatedId(lastUpdatedId);
		tblView.setData(activities);
		lastUpdatedId = null;
	}

	@Override
	public void createProgramTabs(List<IsProgramDetail> programs) {
		showContent(!(programs == null || programs.isEmpty()));

		this.programs = programs;
		listPanel.clear();
		if (programs == null) {
			return;
		}

		createDefaultTab();
		// System.err.println("Size = " + programs.size());
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
		setSelection(singleResult.getType(), false);

		assert singleResult.getPeriod() != null;
		headerContainer.getPeriodDropdown().setValue(singleResult.getPeriod());

		if (singleResult.getProgramSummary() != null) {
			BulletListPanel breadCrumbs = headerContainer
					.setBreadCrumbs(singleResult.getProgramSummary());
			panelCrumbs.clear();
			panelCrumbs.add(breadCrumbs);
			showBackButton(true);
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

			setBudget(singleResult.getBudgetAmount());

			if (!tblView.isSummaryTable) {
				// select tab
				selectTab(singleResult.getId());
				headerContainer.setText(singleResult.getName());
				setData(singleResult.getChildren());
			}else{
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
		int size = listPanel.getWidgetCount();
		for (int i = 0; i < size; i++) {
			BulletPanel li = (BulletPanel) listPanel.getWidget(i);

			Anchor a = (Anchor) li.getWidget(0);
			String href = "#home;page=activities;activity=" + id;
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
	public HasClickHandlers getNewObjectiveLink() {

		return aNewObjective;
	}

	@Override
	public void setSelection(ProgramDetailType type) {
		setSelection(type, false);
	}

	/**
	 * Programs, objectives, activities etc can be selected from the Activities
	 * table (by ticking the checkbox beside them) or by drilling down on any of
	 * them
	 * 
	 * <p>
	 * Some actions are available based on whether the element selected is
	 * actually part of the details (rows of the table) or it is the parent
	 * element whose details are displayed on the table.
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
	public void setSelection(ProgramDetailType type, boolean isRowData) {
		show(aProgram, false);
		show(aNewOutcome, false);
		show(aNewObjective, false);
		show(aNewActivity, false);
		show(aNewTask, false);
		show(aEdit, true);
		show(aAssign, isRowData);
		show(aDetail, isRowData);

		if (type == ProgramDetailType.PROGRAM) {
			show(aNewOutcome, !isRowData);
			show(aNewObjective, isRowData);
			// Program can be selected from the SummaryTab == isRowData
			// or When A Program Tab e.g Wildlife Program is selected
			show(aEdit, AppContext.isCurrentUserAdmin());
		} else if (type == ProgramDetailType.OBJECTIVE) {
			show(aAssign, false);
		} else if (type == ProgramDetailType.OUTCOME) {
			show(aNewActivity, true);
			show(aAssign, false);
		} else if (type == ProgramDetailType.ACTIVITY) {
			show(aNewTask, true);
			show(aAssign, false);
		} else if (type == ProgramDetailType.TASK) {
			show(aNewTask, true);
		} else {
			show(aDetail, false);
			show(aAssign, false);
			show(aProgram, AppContext.isCurrentUserAdmin());
			show(aEdit, false);
		}
	}

	@Override
	public HasClickHandlers getEditLink() {
		return aEdit;
	}

	public void setPeriods(List<PeriodDTO> periods) {
		headerContainer.setPeriodDropdown(periods);

		// if (periods != null && !periods.isEmpty()) {
		// setDates("(" + periods.get(0).getDescription() + ")");
		// }
	}

	public HasClickHandlers getDetailButton() {
		return aDetail;
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

	@Override
	public void setProgramId(Long programId) {
		this.programId = programId;
		tblView.setProgramId(programId);
		headerContainer.setProgramId(programId);
	}

	public HasClickHandlers getAddButton() {
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
	public void setActivePeriod(PeriodDTO activePeriod) {
		if (activePeriod == null) {
			List<PeriodDTO> periods = headerContainer.getPeriodDropdown()
					.getSelectionValues();

			if (periods != null) {
				for (PeriodDTO period : periods) {
					if (period.isCurrentPeriod()) {
						headerContainer.getPeriodDropdown().setValue(period);
						headerContainer.setDates("("
								+ periods.get(0).getDescription() + ")");
						break;
					}
				}
			}
		} else {
			headerContainer.getPeriodDropdown().setValue(activePeriod);
		}
	}

}
