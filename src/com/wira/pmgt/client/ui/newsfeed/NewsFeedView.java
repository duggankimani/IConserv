package com.wira.pmgt.client.ui.newsfeed;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.UserWidget;
import com.wira.pmgt.client.ui.events.CloseCarouselEvent;
import com.wira.pmgt.client.ui.newsfeed.calendar.ProgramCalendarItem;
import com.wira.pmgt.client.ui.newsfeed.components.CarouselPopup;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.client.util.Definitions;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class NewsFeedView extends ViewImpl implements
		NewsFeedPresenter.MyView {

	private final Widget widget;

	private Timer timer;
	final int timerSeconds = 600;

	public interface Binder extends UiBinder<Widget, NewsFeedView> {
	}

	@UiField
	Anchor aCreate;
	@UiField
	Anchor aFollowUp;
	@UiField
	Anchor aReceive;
	@UiField
	Anchor aReview;
	@UiField
	Anchor aClose;
	@UiField
	DivElement divTutorial;
	@UiField
	LIElement liCreate;
	@UiField
	LIElement liFollowUp;
	@UiField
	LIElement liReceive;
	@UiField
	LIElement liReview;
	@UiField HTMLPanel panelUpcomingActivities;
	@UiField
	DivElement imgReceive;
	@UiField
	DivElement imgReview;
	@UiField
	HTMLPanel divNewsFeed;
	@UiField
	HTMLPanel divToggleContainer;
	@UiField
	HTMLPanel divOverdue;
	@UiField
	HTMLPanel divNotStarted;
	@UiField
	HTMLPanel divUpcoming;
	@UiField
	HTMLPanel divOnGoing;
	@UiField
	HTMLPanel divCompleted;
	
	
	@UiField(provided=true) Anchor aOverdue;
	@UiField(provided=true) Anchor aNotStarted;
	@UiField(provided=true) Anchor aUpComing;
	@UiField(provided=true) Anchor aOnGoing;
	@UiField(provided=true) Anchor aCompleted;
	
	@UiField SpanElement spnOverdue;
	@UiField SpanElement spnNotStarted;
	@UiField SpanElement spnUpComing;
	@UiField SpanElement spnOnGoing;
	@UiField SpanElement spnCompleted;
	
	
	@UiField
	UserWidget divUserContainer;

	@UiField FocusPanel parentPanel;
	@UiField BulletListPanel panelActivity;
	protected boolean hasElapsed = false;

	@Inject
	public NewsFeedView(final Binder binder) {
		aOverdue = new ToggleAnchor();
		aNotStarted = new ToggleAnchor();
		aUpComing = new ToggleAnchor();
		aOnGoing = new ToggleAnchor();
		aCompleted = new ToggleAnchor();
		
		widget = binder.createAndBindUi(this);
		
		aOverdue.addClickHandler(new ToggleHandler(divOverdue));
		aNotStarted.addClickHandler(new ToggleHandler(divNotStarted));
		aUpComing.addClickHandler(new ToggleHandler(divUpcoming));
		aOnGoing.addClickHandler(new ToggleHandler(divOnGoing, false));
		aCompleted.addClickHandler(new ToggleHandler(divCompleted, false));
		
	}
	
	protected void show(HTMLPanel div, boolean open) {
		if(open){
			div.removeStyleName("hide");
		}else{
			div.addStyleName("hide");
		}
	}
	
	/**
	 * Click Handler
	 * @author duggan
	 *
	 */
	class ToggleHandler implements ClickHandler{
		
		private boolean isOpen = true;
		private HTMLPanel div;
		
		public ToggleHandler(HTMLPanel div) {
			this.div = div;
		}
		
		public ToggleHandler(HTMLPanel div, boolean isOpen) {
			this.div = div;
			this.isOpen = isOpen;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			ToggleAnchor anchor = (ToggleAnchor)event.getSource();
			toggle(isOpen = !isOpen, anchor,true);
		}
		
		void toggle(Anchor anchor, boolean scroll){
			toggle(isOpen, anchor, scroll);
		}
		
		void toggle(boolean open, Anchor anchor, boolean scroll){
			show(div, open);
			if(open){
				anchor.removeStyleName("icon-chevron-down");
				anchor.addStyleName("icon-chevron-up");
				if(scroll)div.getElement().scrollIntoView();
				
			}else{
				anchor.addStyleName("icon-chevron-down");
				anchor.removeStyleName("icon-chevron-up");
			}
		}
		
	}

	/**
	 * Anchor
	 * @author duggan
	 *
	 */
	class ToggleAnchor extends Anchor{
		
		public ToggleAnchor() {
		}
		
		@Override
		public HandlerRegistration addClickHandler(ClickHandler aHandler) {
			if(aHandler instanceof ToggleHandler){
				ToggleHandler handler = (ToggleHandler)aHandler;
				handler.toggle(this, false);
			}
			return super.addClickHandler(aHandler);
		}
	
	}
	
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public BulletListPanel getPanelActivity() {
		return panelActivity;
	}

	@Override
	public void bind() {
		final CarouselPopup popUp1 = new CarouselPopup();
		final int[] position = new int[2];
		position[0] = 40;
		
		aCreate.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				// popUp1.showFocusArea();
				timer = new Timer() {
					@Override
					public void run() {
						position[1] = liCreate.getAbsoluteRight();
						popUp1.showCreate(liCreate.getClientWidth());
						AppManager.showCarouselPanel(popUp1, position, false);
						hasElapsed = true;
						popUp1.setFocus(true);
						// System.out.println("Li Create:"+ position[1]);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aFollowUp.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				// popUp1.showFocusArea();
				timer = new Timer() {
					@Override
					public void run() {
						position[1] = liFollowUp.getAbsoluteRight();
						AppManager.showCarouselPanel(popUp1, position, false);
						popUp1.showFollowUp();
						hasElapsed = true;
						popUp1.setFocus(true);
						// System.out.println("Li FollowUp:"+ position[1]);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aReceive.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				timer = new Timer() {
					@Override
					public void run() {
						int browserWidth = Window.getClientWidth();
						int popovermaxwidth = (int) (0.4 * browserWidth);
						position[1] = imgReceive.getAbsoluteLeft()
									  - popovermaxwidth;
						popUp1.getElement().getStyle().setWidth(popovermaxwidth-15, Unit.PX);

						AppManager.showCarouselPanel(popUp1, position, true);
						popUp1.showTask();
						hasElapsed = true;
						popUp1.setFocus(false);
						// System.out.println("Li Receive:"+ position[1]);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aReview.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {

				timer = new Timer() {
					@Override
					public void run() {
						int browserWidth = Window.getClientWidth();
						int popovermaxwidth = (int) (0.4 * browserWidth);
						position[1] = imgReview.getAbsoluteLeft()
									  - popovermaxwidth;
						popUp1.getElement().getStyle().setWidth(popovermaxwidth-20, Unit.PX);

						AppManager.showCarouselPanel(popUp1, position, true);
						popUp1.showReview();
						hasElapsed = true;
						popUp1.setFocus(false);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aCreate.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});
	
		
		aFollowUp.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});
		aReceive.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});
		aReview.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});

		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divTutorial.addClassName("hidden");
				AppContext.setSessionValue(Definitions.SHOWWELCOMEWIDGET, "false");
			}
		});
		
		if(!AppContext.isShowWelcomeWiget()){
			divTutorial.addClassName("hidden");
		}
		
	}

	@Override
	public void showLeftPanel(boolean show) {
		if(show){
			divToggleContainer.removeStyleName("in");
			divNewsFeed.addStyleName("out");
		}else{
			divToggleContainer.addStyleName("in");
			divNewsFeed.removeStyleName("out");
		}
	}

	@Override
	public void setImage(HTUser currentUser) {
		divUserContainer.setImage(currentUser);
	}

	@Override
	public void setValues(String userName, String userGroups) {
		divUserContainer.setValues(userName, userGroups);
	}

	@Override
	public void setCalendar(List<ProgramSummary> summaries) {
		Collections.sort(summaries, new Comparator<ProgramSummary>(){
			
			@Override
			public int compare(ProgramSummary o1, ProgramSummary o2) {
	
				int c = o1.getStartDate().compareTo(o2.getStartDate());
				if(c==0){
					c = o1.getDescription().compareTo(o2.getDescription());
				}
				return c;
			}
		});
		
		//clear containers
		divUpcoming.clear();
		divNotStarted.clear();
		divOverdue.clear();
		
		boolean hasOverdue=false,hasNotStarted=false,hasUpcoming=false, hasOnGoing=false, hasCompleted=false;
		int overdue=0,notStarted=0,upcoming=0, onGoing=0, completed=0;
		
		for(ProgramSummary program: summaries){
			if(program.isOverdue()){
				addOverdueItem(program);
				hasOverdue=true;
				++overdue;
			}else if(program.isNotStarted()){
				addNotStartedItem(program);
				hasNotStarted=true;
				++notStarted;
			}else if(program.isUpcoming()){
				addUpcomingItem(program);
				hasUpcoming=true;
				++upcoming;
			}else if(program.isOnGoing()){
				addOnGoingItem(program);
				hasOnGoing=true;
				++onGoing;
			}else if(program.isCompleted()){
				addCompletedItem(program);
				hasCompleted=true;
				++completed;
			}
		}
		

		spnOverdue.setInnerText(overdue+"");
		spnNotStarted.setInnerText(notStarted+"");
		spnUpComing.setInnerText(upcoming+"");
		spnOnGoing.setInnerText(onGoing+"");
		spnCompleted.setInnerText(completed+"");
		
		aOverdue.setVisible(hasOverdue);
		aNotStarted.setVisible(hasNotStarted);
		aUpComing.setVisible(hasUpcoming);
		aOnGoing.setVisible(hasOnGoing);
		aCompleted.setVisible(hasCompleted);
		
	}
	private void addCompletedItem(ProgramSummary program) {
		divCompleted.add(new ProgramCalendarItem(program));
	}

	private void addOnGoingItem(ProgramSummary program) {
		divOnGoing.add(new ProgramCalendarItem(program));
	}

	private void addOverdueItem(ProgramSummary program) {
		divOverdue.add(new ProgramCalendarItem(program));
	}
	
	private void addUpcomingItem(ProgramSummary program) {
		divUpcoming.add(new ProgramCalendarItem(program));
	}

	private void addNotStartedItem(ProgramSummary program) {
		divNotStarted.add(new ProgramCalendarItem(program));
	}

}
