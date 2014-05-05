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
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.BulletPanel;
import com.wira.pmgt.client.ui.component.DropDownList;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class ActivitiesView extends ViewImpl implements
		ActivitiesPresenter.IActivitiesView {

	private final Widget widget;

	@UiField
	HTMLPanel divContent;
	@UiField
	HTMLPanel divNoContent;
	@UiField
	ActivitiesTable tblView;
	@UiField
	SpanElement spnBudget;
	@UiField
	SpanElement spnDates;
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
	Anchor aProgramEdit;
	@UiField
	FocusPanel panelTitle;
	@UiField
	DropDownList<PeriodDTO> lstPeriod;

	@UiField
	BulletListPanel listPanel;

	@UiField
	HeadingElement spnTitle;
	@UiField
	Anchor aLeft;
	@UiField
	Anchor aRight;
	Long lastUpdatedId;

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
		//registerEditFocus();

		MouseDownHandler downHandler = new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				scrollTimer.cancel();
				if(event.getSource()==aLeft){
					scrollDistancePX=-6;
				}else{
					scrollDistancePX=6;

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
	}

	private void registerEditFocus() {
		show(aProgramEdit, false);
		

		panelTitle.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				show(aProgramEdit, true);
			}
		});

		panelTitle.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				show(aProgramEdit, false);
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
		
		if(singleResult.getProgramSummary()!=null)
			setBreadCrumbs(singleResult.getProgramSummary());

		if (singleResult.getType() == ProgramDetailType.PROGRAM && !tblView.isSummaryTable) {
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

		if (type == ProgramDetailType.PROGRAM) {
			show(aNewOutcome, !isRowData);
			show(aNewObjective, true);
			//Program can be selected from the SummaryTab == isRowData 
			//or When A Program Tab e.g Wildlife Program is selected
			show(aEdit, isRowData); 
		}else if (type == ProgramDetailType.OBJECTIVE) {

		} else if (type == ProgramDetailType.OUTCOME) {
			show(aNewActivity, true);
		} else if (type == ProgramDetailType.ACTIVITY) {
			show(aNewTask, true);
		} else if (type == ProgramDetailType.TASK) {
			show(aNewTask, true);
		}else{
			show(aEdit, false);
		}
	}

	@Override
	public HasClickHandlers getEditLink() {
		return aEdit;
	}

	public void setPeriods(List<PeriodDTO> periods) {
		lstPeriod.setItems(periods);
		
		if(periods!=null && !periods.isEmpty()){
			setDates("("+periods.get(0).getDescription()+")");
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

	@Override
	public void setLastUpdatedId(Long lastUpdatedId) {
		this.lastUpdatedId = lastUpdatedId;
	}

	@Override
	public void setProgramId(Long programId) {
		this.programId=programId;
		tblView.setProgramId(programId);
	}

	public void createCrumb(String text, String title, Long id, Boolean isActive) {
		BreadCrumbItem crumb = new BreadCrumbItem();
		if (text.equals("Home")) {
			crumb.setHome(true);
			crumb.setLinkText("");
		}else{
			if (text.length() > 25) {
				text = text.substring(0, 25) + "...";
			}
			crumb.setLinkText(text);
		}
		crumb.setActive(isActive);
		crumb.setTitle(title);
		
		if(id==null || id==0){
			crumb.setHref("#home;page=activities;activity=0");
		}else if(programId==null){
			crumb.setHref("#home;page=activities;activity=0d" + id);
		}else if(id!=programId){
			crumb.setHref("#home;page=activities;activity="+programId+"d" + id);
		}else{
			crumb.setHref("#home;page=activities;activity=" + id);
		}
		
		crumbContainer.add(crumb);
	}

	public void setBreadCrumbs(List<ProgramSummary> summaries) {
		crumbContainer.clear();
		createCrumb("Home", "Home", 0L, false);
		for (int i = summaries.size() - 1; i > -1; i--) {
			ProgramSummary summary = summaries.get(i);
			createCrumb(summary.getName(), summary.getDescription(),
					summary.getId(), i == 0);
		}

	}
	
	public void setDates(String text) {
		spnDates.setInnerText(text);
	}
}
