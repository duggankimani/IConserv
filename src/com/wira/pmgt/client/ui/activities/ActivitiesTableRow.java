package com.wira.pmgt.client.ui.activities;

import static com.wira.pmgt.client.ui.util.NumberUtils.CURRENCYFORMAT;
import static com.wira.pmgt.client.ui.util.NumberUtils.NUMBERFORMAT;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.RowWidget;
import com.wira.pmgt.client.ui.events.ActivitySelectionChangedEvent;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;

public class ActivitiesTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, ActivitiesTableRow> {
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
	HTMLPanel divRating;
	@UiField
	HTMLPanel divBudget;
	@UiField
	HTMLPanel divCheckbox;
	@UiField
	CheckBox chkSelect;

	@UiField
	SpanElement spnStatus;

	int level = 0;
	long programId=0;
	IsProgramActivity activity;
	List<FundDTO> funding = null;
	
	//Is the current status showing children or not
	boolean showChildren=true;
	
	//Programs Summary Grid or Program Details
	boolean isSummaryRow=false;

	List<HTMLPanel> allocations = new ArrayList<HTMLPanel>();
	
	Timer timer = new Timer() {

		@Override
		public void run() {
			highlight(false);
		}
	};
	
	public ActivitiesTableRow(IsProgramActivity activity, List<FundDTO> sortedListOfFunding,
			Long programId, boolean isSummaryRow,
			int level) {
				
		initWidget(uiBinder.createAndBindUi(this));
		this.isSummaryRow = isSummaryRow;
		this.activity = activity;
		this.programId= (programId==null? 0: programId);
		this.level = level;
		this.funding = sortedListOfFunding;
		
		//Show/ hide this details based on its level on load
		if(!isSummaryRow){
			
			this.showChildren=(level==0);
			
			if(level>1){
				//Only show level 0 and level 1 items - Hide all the rest
				show(false);
				setHasChildren(false);	
			}else if(activity.getChildren()!=null && !activity.getChildren().isEmpty()){
				setHasChildren(this.showChildren);
			}
		}else{
			this.showChildren=true;
			divRowCaret.setVisible(activity.getType()==ProgramDetailType.PROGRAM && activity.getObjectives().size()>0);
		}
		
		
		//Bind Row to Table
		setRow(row);
		
		//Initialize table details
		init();

	}
	
	public void init(){
		//Program/Task status - Created, Started, Done, Closed etc
		setStatus("created", "info");
		
		//Name - Highlighting provided based on child level
		setActivityName();
		//Padding based on the child level
		setPadding();
		
		//Show different cols based on whether this is a program summary listing or program details
		if (isSummaryRow) {
			divProgress.setStyleName("hide");
			divRating.setStyleName("hide");
			divStatus.setStyleName("hide");

		} else {
			divProgress.getElement().setInnerText("0%");
			divRating.getElement().setInnerText("N/A");
			if(activity.getChildren().isEmpty()){
					divRowCaret.addStyleName("hide");
			}
		}

		//Budgeting & Allocations information
		String budgetAmount = activity.getBudgetAmount() == null ? ""
				: CURRENCYFORMAT.format(activity.getBudgetAmount());
		divBudget.getElement().setInnerText(budgetAmount);

		chkSelect.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				AppContext.fireEvent(new ActivitySelectionChangedEvent(
						ActivitiesTableRow.this.activity, event.getValue()));
			}
		});
				

		//Set Funding
		setFunding();
	}

	private void setActivityName() {
		divName.getElement().setInnerText(activity.getName());
		divName.setHref("#home;page=activities;activity="+programId+"d"+activity.getId());
		divRowStrip.addClassName("label-info");

		if (activity.getType() == ProgramDetailType.OBJECTIVE)
			divName.getElement().setInnerText(
					activity.getName() + " - " + activity.getDescription());
		if (level == 0) {
			divName.addStyleName("bold");
		}

	}

	public IsProgramActivity getActivity() {
		return activity;
	}

	public void setSelectionChangeHandler(ValueChangeHandler<Boolean> handler) {
		chkSelect.addValueChangeHandler(handler);
	}

	/*
	 * Sets the status var statusType: danger-red, warning-golden, info - bluish
	 */
	public void setStatus(String text, String statusType) {
		spnStatus.setInnerText(text);
		spnStatus.addClassName("label-" + statusType);
	}

	@Override
	public void setRowNumber(int number) {
		// divRowNo.getElement().setInnerText(""+number);
	}

	private void setPadding() {
		if (level > 0) {
			
			if (level == 2) {
				divRowStrip.addClassName("label-info");
				divRowStrip.addClassName("label-warning");
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

		for (FundDTO programFund : funding) {
			int idx = activitySourceOfFunds.indexOf(programFund);

			if (idx == -1) {
				createTd(new InlineLabel(""));
			} else {
				ProgramFundDTO activityFund = activityFunding.get(idx);
				HTMLPanel amounts = new HTMLPanel("");
				String amount = activityFund.getAmount() == null ? ""
						: NUMBERFORMAT.format(activityFund.getAmount());
				amounts.add(new InlineLabel(amount));

				Double allocation = activityFund.getAllocation();
				if (allocation != null && allocation != 0.0) {
					HTMLPanel allocationPanel = new HTMLPanel("("
							+ NUMBERFORMAT.format(allocation) + ")");
					allocationPanel.setTitle("Allocated amount");
					allocationPanel.getElement().getStyle()
							.setFontSize(0.8, Unit.EM);
					allocationPanel.addStyleName("underline");
					if (allocation > activityFund.getAmount()) {
						allocationPanel.addStyleName("text-warning");
					} else {
						allocationPanel.addStyleName("text-success");
					}
					allocationPanel.getElement().getStyle()
							.setFontSize(0.8, Unit.EM);
					amounts.add(allocationPanel);
					allocations.add(allocationPanel);
					allocationPanel.setVisible(showChildren);
				}
				createTd(amounts);
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
		divRowCaret.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//Toggle
				toggle(!showChildren);
			}
		});	
	}

	private void setHasChildren(boolean hasChildren) {
		//divRowCaret.setHref("#home;page=activities;activity="+programId+"d"+activity.getId());
		if (hasChildren) {
			divRowCaret.removeStyleName("icon-caret-right");
			divRowCaret.addStyleName("icon-caret-down");
		} else {
			divRowCaret.removeStyleName("icon-caret-down");
			divRowCaret.addStyleName("icon-caret-right");
		}

		if(level!=0)
		  showAllocations(hasChildren);
	}
	
	//default toggle
	private void toggle(boolean isShowChildren){
		this.showChildren= isShowChildren;
		
		HTMLPanel panel = (HTMLPanel)this.getParent();
		int idx = panel.getWidgetIndex(this);
		assert idx!=-1;
		
		int childCount= activity.getChildren()== null? 0: activity.getChildren().size();
		if(programId==0){
			//summary table
			childCount = activity.getObjectives()==null?0 :activity.getObjectives().size();
		}
		
		setHasChildren(isShowChildren);
		if(childCount==0){
			return;
		}
		
		int childrenCollapsed=0;
		//loop until you count n children
		for(int i=idx+1;(i<panel.getWidgetCount() && childrenCollapsed<childCount); i++){
			ActivitiesTableRow row = (ActivitiesTableRow)panel.getWidget(i);
			if(row.getActivity().getParentId()==activity.getId()){
				childrenCollapsed++;
				if(!showChildren){
					//toggle children of children only when collapsing
					row.toggle(showChildren);
				}
				row.show(showChildren);
			}
			
		}
	}
	
	/**
	 * Might be called directly by parent
	 * 
	 * @param hide
	 */
	private void show(boolean show){
		//showChildren=show; //Synchronize states with caller
		row.setStyleName(show? "tr":"hide");
	}

	private void showAllocations(boolean showChildren) {
		for(HTMLPanel widget: allocations){
			widget.setVisible(showChildren);
		}
	}
	
}
