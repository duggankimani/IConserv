package com.wira.pmgt.client.ui.activities;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.BulletPanel;
import com.wira.pmgt.client.ui.component.Dropdown;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class ActivitiesView extends ViewImpl implements
		ActivitiesPresenter.IActivitiesView {

	private final Widget widget;
	
	@UiField
	Anchor aProgram;
	
	@UiField
	HTMLPanel divContent;
	@UiField
	HTMLPanel divNoContent;
	@UiField
	ActivitiesTable tblView;
	@UiField
	SpanElement spnBudget;
	@UiField
	InlineLabel spnDates;
	@UiField
	Anchor aNewOutcome;
	@UiField
	BulletListPanel crumbContainer;
	@UiField
	Anchor aNewActivity;
	@UiField
	Anchor aNewObjective;
	@UiField
	Anchor aNewTask;
	@UiField
	Anchor aEdit;
	@UiField
	Anchor aBack;
	@UiField
	Anchor aAssign;
	@UiField
	Anchor aDetail;
	
	@UiField
	Anchor aProgramEdit;
	@UiField
	FocusPanel panelTitle;
	
	@UiField HTMLPanel divPopup;

	@UiField
	BulletListPanel listPanel;

	@UiField
	HeadingElement spnTitle;
	@UiField
	Anchor aLeft;
	@UiField
	Anchor aRight;
	Long lastUpdatedId;

	@UiField
	Dropdown<PeriodDTO> periodDropdown;

	List<IsProgramActivity> programs = null;

	public interface Binder extends UiBinder<Widget, ActivitiesView> {
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
	public ActivitiesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		listPanel.setId("mytab");
		// registerEditFocus();

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

		createCrumb("Home", "Home", 0L, false);
		
		periodDropdown.addValueChangeHandler(new ValueChangeHandler<PeriodDTO>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PeriodDTO> event) {
				setDates(event.getValue().getDescription());
			}
		});
		
		spnDates.getElement().setAttribute("data-toggle", "dropdown");
		
		
		show(aBack,false);
		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.back();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setTitle(String text) {
		if (text != null) {
			spnTitle.setInnerText(text);
		} else {
			spnTitle.setInnerText("Programs & Activities");
		}
	}

	public void setBudget(Double number) {
		if (number != null) {
			spnBudget.setInnerHTML(NumberFormat.getCurrencyFormat().format(
					number));
		}
	}

	public HasClickHandlers getNewOutcome() {
		return aNewOutcome;
	}

	public HasClickHandlers getNewActivityLink() {
		return aNewActivity;
	}

	@Override
	public void showContent(boolean status) {
		if (status) {
			divContent.removeStyleName("hidden");
			divNoContent.addStyleName("hidden");
			spnTitle.setInnerText("Programs Management");
		} else {
			divContent.addStyleName("hidden");
			divNoContent.removeStyleName("hidden");
		}
	}

	public void createDefaultTab() {
		createTab("Summary", 0, true);
	}

	public void createTab(String text, long id, boolean active) {
		BulletPanel li = new BulletPanel();
		Anchor a = new Anchor(text);
		a.setHref("#home;page=activities;activity=" + id);
		li.add(a);

		if (active) {
			li.addStyleName("active");
		} else {
			li.removeStyleName("active");
		}
		listPanel.add(li);
	}

	@Override
	public void setActivities(List<IsProgramActivity> activities) {
		tblView.setLastUpdatedId(lastUpdatedId);
		tblView.setData(activities);
		lastUpdatedId = null;
	}

	@Override
	public void setPrograms(List<IsProgramActivity> programs) {
		showContent(!(programs == null || programs.isEmpty()));
		this.programs = programs;
		listPanel.clear();
		if (programs == null) {
			return;
		}

		createDefaultTab();
		// System.err.println("Size = " + programs.size());
		for (IsProgramActivity activity : programs) {
			// boolean first = programs.indexOf(activity) == 0;
			createTab(activity.getName(), activity.getId(), false);
		}
	}

	/**
	 * Sets Parent Activity
	 */
	public void setActivity(IsProgramActivity singleResult) {
		setSelection(singleResult.getType(), false);
		setBudget(singleResult.getBudgetAmount());

		if (singleResult.getProgramSummary() != null)
			setBreadCrumbs(singleResult.getProgramSummary());

		if (singleResult.getType() == ProgramDetailType.PROGRAM
				&& !tblView.isSummaryTable) {
			// select tab
			selectTab(singleResult.getId());
			setTitle(singleResult.getName());
			setActivities(singleResult.getChildren());
		} else {
			setActivities(Arrays.asList(singleResult));
		}

	}

	private void show(Anchor aAnchor, boolean show) {
		if (show) {
			aAnchor.getElement().getParentElement().removeClassName("hide");
		} else {
			aAnchor.getElement().getParentElement().addClassName("hide");
		}
	}

	private void selectTab(Long id) {
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
		setSelection(type, true);
	}

	/**
	 * 
	 * @param type
	 * @param isRowData
	 */
	public void setSelection(ProgramDetailType type, boolean isRowData) {
		show(aNewOutcome, false);
		show(aNewObjective, false);
		show(aNewActivity, false);
		show(aNewTask, false);
		show(aEdit, true);
		show(aAssign,false);
		show(aBack, false);
		show(aDetail, false);
		
		if (type == ProgramDetailType.PROGRAM) {
			show(aProgram,false);
			show(aNewOutcome, !isRowData);
			show(aNewObjective, true);
			show(aAssign,true);
			// Program can be selected from the SummaryTab == isRowData
			// or When A Program Tab e.g Wildlife Program is selected
			show(aEdit, isRowData);
		} else if (type == ProgramDetailType.OBJECTIVE) {
			show(aProgram,false);
			show(aAssign,false);
		} else if (type == ProgramDetailType.OUTCOME) {
			show(aProgram,false);
			show(aNewActivity, true);
			show(aAssign,true);
			show(aDetail,true);
		} else if (type == ProgramDetailType.ACTIVITY) {
			show(aProgram,false);
			show(aNewTask, true);
			show(aAssign,true);
			show(aDetail,true);
		} else if (type == ProgramDetailType.TASK) {
			show(aProgram,false);
			show(aNewTask, true);
			show(aAssign,true);
			show(aDetail,true);
		} else {
			show(aProgram,true);
			show(aEdit, false);
		}
	}

	@Override
	public HasClickHandlers getEditLink() {
		return aEdit;
	}

	public void setPeriods(List<PeriodDTO> periods) {
		periodDropdown.setValues(periods);

		if (periods != null && !periods.isEmpty()) {
			setDates("(" + periods.get(0).getDescription() + ")");
		}

	}

	@Override
	public HasClickHandlers getProgramEdit() {
		return aProgramEdit;
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
	}

	public void createCrumb(String text, String title, Long id, Boolean isActive) {
		BreadCrumbItem crumb = new BreadCrumbItem();
		if (text.equals("Home")) {
			crumb.setHome(true);
			crumb.setLinkText("");
			crumb.setHref(getHref(id));
		} else {
			if (text.length() > 25) {
				text = text.substring(0, 25) + "...";
			}
			crumb.setLinkText(text);
			crumb.setHref(getHref(id));
		}
		crumb.setActive(isActive);
		crumb.setTitle(title);

		crumbContainer.add(crumb);
	}
	
	/*
	 * Get href from Id
	 */
	private String getHref(Long id) {
		String href="";
		if (id == null || id == 0) {
			href="#home;page=activities;activity=0";
		} else if (programId == null) {
			href= "#home;page=activities;activity=0d" + id;
		} else if (id != programId) {
			href ="#home;page=activities;activity=" + programId + "d"
					+ id;
		} else {
			href = "#home;page=activities;activity=" + id;
		}
		return href;
	}
	
	public void setBreadCrumbs(List<ProgramSummary> summaries) {
		crumbContainer.clear();
		createCrumb("Home", "Home", 0L, false);
		for (int i = summaries.size() - 1; i > -1; i--) {
			ProgramSummary summary = summaries.get(i);
			createCrumb(summary.getName(), summary.getDescription(),
					summary.getId(), i == 0);
		}
			showBackButton(true);
	}

	public void setDates(String text) {
		spnDates.getElement().setInnerText(text);
	}
	
	
	public HasClickHandlers getAddButton() {
		return aProgram;
	}

	
	public void showBackButton(boolean status) {
		if(status){
			show(aBack, true);
		}else{
			show(aBack,false);
		}
	}

}
