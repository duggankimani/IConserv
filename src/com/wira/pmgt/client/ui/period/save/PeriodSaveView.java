package com.wira.pmgt.client.ui.period.save;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.DateRangeWidget;
import com.wira.pmgt.client.ui.component.IssuesPanel;
import com.wira.pmgt.shared.model.program.PeriodDTO;

import static com.wira.pmgt.client.ui.util.DateUtils.*;

public class PeriodSaveView extends Composite {

	private static PeriodSaveViewUiBinder uiBinder = GWT
			.create(PeriodSaveViewUiBinder.class);

	interface PeriodSaveViewUiBinder extends UiBinder<Widget, PeriodSaveView> {
	}
	
	@UiField IssuesPanel issues;
	@UiField TextArea txtDescription;
	@UiField DateRangeWidget dtRange;
	
	private PeriodDTO periodDTO;
	
	public PeriodSaveView() {
		initWidget(uiBinder.createAndBindUi(this));
		dtRange.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				setDescription();
			}
		});
	}

	public PeriodSaveView(PeriodDTO period) {
		this();
		this.periodDTO = period;
		txtDescription.setValue(period.getDescription());
		dtRange.setDates(period.getStartDate(), period.getStartDate());
	}

	protected void setDescription() {
		Date startDate = dtRange.getStartDate();
		Date endDate = dtRange.getEndDate();
		txtDescription.setValue(null);
		
		StringBuffer desc = new StringBuffer();
		if(startDate!=null){
			desc.append(MONTHDAYFORMAT.format(startDate) + " - ");
		}else{
			desc.append("? - ");
		}
		
		if(endDate!=null){
			desc.append(MONTHDAYFORMAT.format(endDate) +" "+DateTimeFormat.getFormat("yyyy").format(endDate));
		}
		
		if(!desc.toString().isEmpty()){
			txtDescription.setValue(desc.toString()+" Period");
		}
	}
	
	public boolean isValid(){
		issues.clear();
		boolean isValid=true;
		Date startDate = dtRange.getStartDate();
		Date endDate = dtRange.getEndDate();
		String description = txtDescription.getValue();
		
		if(startDate==null){
			isValid=false;
			issues.addError("Start Date is mandatory");
		}
		
		if(endDate==null){
			isValid=false;
			issues.addError("End Date is mandatory");
		}
		
		if(description==null || description.isEmpty()){
			isValid=false;
			issues.addError("Description is mandatory");
		}
		
		return isValid;
	}

	public PeriodDTO getPeriod() {
		
		PeriodDTO period = periodDTO;
		if(periodDTO==null){
			period = new PeriodDTO();
		}
		
		period.setDescription(txtDescription.getValue());
		period.setStartDate(dtRange.getStartDate());
		period.setEndDate(dtRange.getEndDate());	
		return period;
	}

}
