package com.wira.pmgt.client.ui.detailedActivity;

import static com.wira.pmgt.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.DateRangeWidget;
import com.wira.pmgt.client.ui.component.IssuesPanel;
import com.wira.pmgt.client.ui.component.grid.AggregationGrid;
import com.wira.pmgt.client.ui.component.grid.ColumnConfig;
import com.wira.pmgt.client.ui.component.grid.DataMapper;
import com.wira.pmgt.client.ui.component.grid.DataModel;
import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.client.ui.util.StringUtils;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.Listable;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramDTO;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;
import com.wira.pmgt.shared.model.program.ProgramStatus;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.TargetAndOutcomeDTO;

public class CreateActivityView extends ViewImpl implements
		CreateActivityPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateActivityView> {
	}

	IsProgramDetail Outcome; // The outcome under which this activity is created

	@UiField
	DivElement divActivityName;
	@UiField
	TextBox txtBudgetLine;
	@UiField
	IssuesPanel issues;
	@UiField
	TextArea txtActivity;
	@UiField
	AggregationGrid gridView;
	@UiField
	BulletListPanel crumbContainer;
	@UiField
	InlineLabel spnPeriod;
	@UiField
	AggregationGrid gridTargets;
	@UiField
	DivElement divTargetsAndIndicators;
	@UiField
	Anchor aCopyTargets;
	
	@UiField CheckBox chkMarkClosed;

	@UiField
	DateRangeWidget dtRange;
	
	@UiField SpanElement spnWarn;

	List<Listable> donors = new ArrayList<Listable>();
	ColumnConfig donorField = new ColumnConfig("donor", "Donor Name",
			DataType.SELECTBASIC);

	ProgramDetailType type = ProgramDetailType.ACTIVITY;

	private IsProgramDetail persistedActivity;

	@Inject
	public CreateActivityView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		createGrid();
		txtActivity.getElement().setAttribute("rows", "3");
		createTargetsAndIndicatorsGrid();
		chkMarkClosed.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				spnWarn.addClassName("hide");
				if(event.getValue()){
					return;
				}
				
				List<ProgramFundDTO> funding = gridView.getData(programFundMapper);
				if(funding.isEmpty()){
					return;
				}
				
				if(persistedActivity!=null && persistedActivity.getStatus()!=null
						&& persistedActivity.getStatus().equals(ProgramStatus.CLOSED)){
					//this is an update
					boolean isReopen=false;
					for (ProgramFundDTO dto : funding) {
						//if its a leaf task/activity (has no children, we can update actuals directly) - can we? No
						if(dto.getActual()!=null && dto.getFund()!=null){
							isReopen=true;
						}
						
					}
					
					if(isReopen){
						spnWarn.removeClassName("hide");
					}
				}
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	private void createTargetsAndIndicatorsGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		// ColumnConfig config = new ColumnConfig("condition", "Check",
		// DataType.SELECTBASIC);
		// configs.add(config);

		ColumnConfig config = new ColumnConfig("target", "Target",
				DataType.DOUBLE, "1000");
		config.setMandatory(true);
		configs.add(config);

		config = new ColumnConfig("indicator", "Indicator", DataType.STRING,
				"Participants");
		config.setMandatory(true);
		configs.add(config);

		config = new ColumnConfig("actual", "Output", DataType.DOUBLE, "1000");
		configs.add(config);

		gridTargets.setColumnConfigs(configs);
		gridTargets.setAutoNumber(true);
	}

	public void createGrid() {
		gridView.refresh();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		donorField.setMandatory(true);
		configs.add(donorField);

		ColumnConfig config = new ColumnConfig("amount", "Amount",
				DataType.DOUBLE);
		config.setAggregationColumn(true);
		config.setMandatory(true);
		configs.add(config);

		config = new ColumnConfig("actual", "Actual Exp", DataType.DOUBLE);
		config.setAggregationColumn(true);
		configs.add(config);

		gridView.setColumnConfigs(configs);
		gridView.setAutoNumber(true);
	}

	@Override
	public void setFunds(List<FundDTO> funds) {
		donors.clear();
		Collections.sort(funds, new Comparator<FundDTO>() {
			@Override
			public int compare(FundDTO o1, FundDTO o2) {

				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
		if (funds != null) {
			for (FundDTO dto : funds) {
				donors.add(dto);
			}
		}
		donorField.setDropDownItems(donors);
		gridView.refresh();
	}

	@Override
	public void setPeriod(PeriodDTO period) {
		if (period != null) {
			spnPeriod.setText(period.getDescription());
			dtRange.setRangeValidation(period.getStartDate(),
					period.getEndDate());
			dtRange.setDates(period.getStartDate(), period.getEndDate());
		}
	}

	public void createCrumb(String text, String title, Long id, Boolean isActive) {
		BreadCrumbItem crumb = new BreadCrumbItem();
		crumb.setActive(isActive);
		crumb.setTitle(title);
		if (text.length() > 25) {
			text = text.substring(0, 25) + "...";
		}
		crumb.setLinkText(text);
		crumb.setHref("#home;page=activities;activity=" + id);
		crumbContainer.add(crumb);
	}

	public void setBreadCrumbs(List<ProgramSummary> summaries) {
		crumbContainer.clear();
		for (int i = summaries.size() - 1; i > -1; i--) {
			ProgramSummary summary = summaries.get(i);
			createCrumb(summary.getName(), summary.getDescription(),
					summary.getId(), i == 0);
		}

	}

	@Override
	public void setParentProgram(IsProgramDetail isProgramActivity) {
		if (isProgramActivity.getStartDate() != null
				&& isProgramActivity.getEndDate() != null) {

			if (type == ProgramDetailType.TASK) {
				Date today = new Date();
				Date tomorrow = DateUtils.addDays(today, 1);

				Date startDate = today.after(isProgramActivity.getStartDate())
						&& today.before(isProgramActivity.getEndDate()) ? today
						: isProgramActivity.getStartDate();
				Date endDate = tomorrow.after(isProgramActivity.getStartDate())
						&& tomorrow.before(isProgramActivity.getEndDate()) ? tomorrow
						: isProgramActivity.getEndDate();

				txtBudgetLine.setValue(isProgramActivity.getBudgetLine());
				// txtBudgetLine.setEnabled(false);
				dtRange.setDates(startDate, endDate);
			} else {
				dtRange.setDates(isProgramActivity.getStartDate(),
						isProgramActivity.getEndDate());
			}

			dtRange.setRangeValidation(isProgramActivity.getStartDate(),
					isProgramActivity.getEndDate());
		}

		if (isProgramActivity.getTargetsAndOutcomes() == null
				|| isProgramActivity.getTargetsAndOutcomes().isEmpty()) {
			aCopyTargets.addStyleName("hide");
		}
		setBreadCrumbs(isProgramActivity.getProgramSummary());
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if (isNullOrEmpty(txtActivity.getValue())) {
			isValid = false;
			issues.addError("Activity Name is mandatory");
		}

		if (isNullOrEmpty(txtBudgetLine.getValue())) {
			isValid = false;
			issues.addError("Budget Line is mandatory");
		}

		String err = dtRange.isValid();
		if (err != null) {
			isValid = false;
			issues.addError(err);
		}

		//Mandatory checks - Grids
		List<String> gridErrors = gridTargets.getErrors();
		if(gridErrors!=null){
			isValid=false;
			issues.addError(gridErrors.get(0)+" (Target and Outcomes Grid)");
		}else if((gridErrors=gridView.getErrors())!=null){
			isValid=false;
			issues.addError(gridErrors.get(0)+" (Budgets Grid)");
		}
		
		//Actuals
		List<ProgramFundDTO> funding = gridView.getData(programFundMapper);
		for (ProgramFundDTO dto : funding) {
			int frequency = Collections.frequency(funding, dto);
			if (frequency > 1) {
				isValid = false;
				issues.addError(dto.getFund().getName() + " is repeated");
			}
			
			//if its a leaf task/activity (has no children, we can update actuals directly) - can we? No
			if(dto.getActual()!=null && dto.getFund()!=null)
			if(hasChanged(dto) && !chkMarkClosed.getValue()){
				isValid=false;
				issues.addError("Please tick the \"Mark Activity as closed\" box below the budgets to "
						+ "save the expenditure(Actual) value for "+dto.getFund().getName());
			}
		}
		
		//Outcomes
		List<TargetAndOutcomeDTO> targets = gridTargets.getData(targetAndOutcomeMapper);
		for(TargetAndOutcomeDTO dto: targets){
			if(dto.getActualOutcome()!=null && dto.getActualOutcome()!=0)
			if(hasChanged(dto) && !chkMarkClosed.getValue()){
				isValid=false;
				issues.addError("Please tick the \"Mark Activity as closed\" box below the budgets to "
						+ "save the outcome for indicator \""+dto.getMeasure()+"\"");
			}
		}
		
		if (!isValid) {
			issues.getElement().scrollIntoView();
		}

		return isValid;

	}

	private boolean hasChanged(TargetAndOutcomeDTO dto) {
		if(dto.getId()==null){
			return true;
		}
		
		List<TargetAndOutcomeDTO> outcomes = persistedActivity.getTargetsAndOutcomes();
		for(TargetAndOutcomeDTO outcome: outcomes){
			if(outcome.getId().equals(dto.getId())){
				if(outcome.getActualOutcome()==null){
					return true;
				}
				
				if(!outcome.getActualOutcome().equals(dto.getActualOutcome())){
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean hasChanged(ProgramFundDTO dto) {
		if(dto.getId()==null){
			return true;
		}
		List<ProgramFundDTO> programFundDtos =persistedActivity.getFunding();
		for(ProgramFundDTO fund: programFundDtos){
			if(fund.getId().equals(dto.getId())){
				if(fund.getActual()==null){
					return true;
				}
				
				if(!fund.getActual().equals(dto.getActual())){
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public IsProgramDetail getActivity() {
		ProgramDTO program = new ProgramDTO();
		program.setDescription(txtActivity.getValue());
		program.setName(txtActivity.getValue());
		program.setType(type);
		program.setStartDate(dtRange.getStartDate());
		program.setEndDate(dtRange.getEndDate());
		program.setBudgetLine(txtBudgetLine.getValue());
		if(chkMarkClosed.getValue()){
			program.setStatus(ProgramStatus.CLOSED);
		}else if(persistedActivity!=null){
			if(persistedActivity.getStatus()==ProgramStatus.CLOSED){
				if(program.getProgress()>0){
					program.setStatus(ProgramStatus.OPENED);
				}else{
					program.setStatus(ProgramStatus.CREATED);
				}
			}else{
				program.setStatus(persistedActivity.getStatus());
			}
			
		}

		// Targets and Outcomes
		List<TargetAndOutcomeDTO> targets = gridTargets
				.getData(targetAndOutcomeMapper);
		program.setTargetsAndOutcomes(targets);

		// Activity Funds
		List<ProgramFundDTO> funding = gridView.getData(programFundMapper);
		program.setFunding(funding);
		for(ProgramFundDTO dto: funding){
			System.err.println(dto);
		}
//		Double totalAmount = 0.0;
//		for (ProgramFundDTO programFund : funding) {
//			Double val = programFund.getAmount();
//			if (val != null) {
//				totalAmount += val;
//			}
//		}
//		program.setBudgetAmount(totalAmount);
		return program;

	}

	@Override
	public void setActivity(IsProgramDetail activity) {
		this.persistedActivity = activity;
		if (activity == null) {
			return;
		}
		
		if(activity.getStatus()!=null)
		if(activity.getStatus().equals(ProgramStatus.CLOSED)){
			chkMarkClosed.setValue(true);
		}
		
		txtActivity.setValue(activity.getDescription());

		txtBudgetLine.setValue(activity.getBudgetLine());

		dtRange.setDates(activity.getStartDate(), activity.getEndDate());
		// program.setTargetsAndOutcomes(targetsAndOutcomes);
		//

		Collections.sort(activity.getFunding(),
				new Comparator<ProgramFundDTO>() {
					@Override
					public int compare(ProgramFundDTO o1, ProgramFundDTO o2) {

						return o1.getFund().getName()
								.compareTo(o2.getFund().getName());
					}
				});

		setTargetsAndOutComes(activity.getTargetsAndOutcomes());

		// Program Funds
		List<Object> lst = new ArrayList<Object>();
		for (ProgramFundDTO dto : activity.getFunding()) {
			lst.add(dto);
		}
		List<DataModel> models = programFundMapper.getDataModels(lst);
		gridView.setData(models);
	}

	@Override
	public void setTargetsAndOutComes(
			List<TargetAndOutcomeDTO> targetsAndOutComes) {
		Collections.sort(targetsAndOutComes,
				new Comparator<TargetAndOutcomeDTO>() {
					@Override
					public int compare(TargetAndOutcomeDTO o1,
							TargetAndOutcomeDTO o2) {

						return o1.getMeasure().compareTo(o2.getMeasure());
					}
				});

		List<Object> targets = new ArrayList<Object>();

		if (targetsAndOutComes == null || targetsAndOutComes.isEmpty()) {
			return;
		}

		for (TargetAndOutcomeDTO dto : targetsAndOutComes) {
			targets.add(dto);
		}
		List<DataModel> models = targetAndOutcomeMapper.getDataModels(targets);
		gridTargets.setData(models);

	}

	@Override
	public void clear() {
		chkMarkClosed.setValue(false);
		spnWarn.addClassName("hide");
		txtBudgetLine.setValue(null);
		gridView.setData(new ArrayList<DataModel>());
		txtActivity.setValue(null);
		crumbContainer.clear();
		spnPeriod.setText(null);
		issues.clear();
	}

	/**
	 * Program Fund Mapper
	 */
	DataMapper programFundMapper = new DataMapper() {
		@Override
		public ProgramFundDTO getData(DataModel model) {
			ProgramFundDTO fund = new ProgramFundDTO();
			if (model.get("donor") == null) {
				return null;
			}

			fund.setFund(model.get("donor") == null ? null : (FundDTO) model
					.get("donor"));
			fund.setAmount(model.get("amount") == null ? null : (Double) model
					.get("amount"));
			fund.setActual(model.get("actual") == null ? null : (Double) model
					.get("actual"));
			
			fund.setId(model.getId());
			return fund;
		}

		@Override
		public List<DataModel> getDataModels(List<Object> funding) {
			List<DataModel> models = new ArrayList<DataModel>();
			for (Object obj : funding) {
				ProgramFundDTO fund = (ProgramFundDTO) obj;
				DataModel model = new DataModel();
				model.set("donor", fund.getFund());
				model.setId(fund.getId());
				model.set("amount", fund.getAmount());
				model.set("actual", fund.getActual());
				models.add(model);
			}

			return models;
		}
	};

	DataMapper targetAndOutcomeMapper = new DataMapper() {

		@Override
		public List<DataModel> getDataModels(List<Object> funding) {

			List<DataModel> models = new ArrayList<DataModel>();
			for (Object obj : funding) {
				TargetAndOutcomeDTO target = (TargetAndOutcomeDTO) obj;
				DataModel model = new DataModel();
				model.setId(target.getId());
				model.set("target", target.getTarget());
				model.set("indicator", target.getMeasure());
				model.set("actual", target.getActualOutcome());
				models.add(model);
			}

			return models;
		}

		@Override
		public TargetAndOutcomeDTO getData(DataModel model) {

			if (model.isEmpty()) {
				return null;
			}

			TargetAndOutcomeDTO dto = new TargetAndOutcomeDTO();
			dto.setId(model.getId());
			dto.setMeasure(isNullOrEmpty(model.get("indicator")) ? null : model
					.get("indicator").toString());

			dto.setTarget(model.get("target") == null ? null : (Double) model
					.get("target"));

			dto.setActualOutcome(model.get("actual") == null ? null
					: (Double) model.get("actual"));

			dto.setKey(StringUtils.camelCase(dto.getMeasure()));
			return dto;
		}
	};

	@Override
	public void setType(ProgramDetailType type) {
		this.type = type;
		if (type == ProgramDetailType.TASK) {
			divActivityName.setInnerText("Task Name");
			// divTargetsAndIndicators.setClassName("hide");
			aCopyTargets.removeStyleName("hide");
		} else {
			aCopyTargets.addStyleName("hide");
		}
	}

	public HasClickHandlers getCopyTargetsLink() {
		return aCopyTargets;
	}

}
