package com.wira.pmgt.client.ui.donor.save;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.ActionLink;
import com.wira.pmgt.client.ui.component.DropDownList;
import com.wira.pmgt.client.ui.component.IssuesPanel;
import com.wira.pmgt.shared.model.program.FundDTO;
import static com.wira.pmgt.client.ui.util.StringUtils.*;

public class DonorSaveView extends Composite {

	@UiField IssuesPanel issues;
	
	@UiField
	TextBox txtName;
	@UiField
	TextArea txtDescription;
	@UiField
	ActionLink aCreateDonor;
	@UiField
	ActionLink aEditDonor;

	@UiField
	HTMLPanel divDropdownContainer;
	@UiField
	HTMLPanel divCreateContainer;
	
	@UiField CheckBox chkActive;
	
	@UiField DropDownList<FundDTO> lstDonors;
	private List<FundDTO> donors = new ArrayList<FundDTO>();

	private static DonorSaveViewUiBinder uiBinder = GWT
			.create(DonorSaveViewUiBinder.class);

	interface DonorSaveViewUiBinder extends UiBinder<Widget, DonorSaveView> {
	}

	private FundDTO selected = null;
	
	public DonorSaveView() {
		initWidget(uiBinder.createAndBindUi(this));

		aCreateDonor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showContainer(divCreateContainer,true);
				showContainer(divDropdownContainer,false);
				clear();
			}
		});

		aEditDonor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showContainer(divCreateContainer,false);
				showContainer(divDropdownContainer,true);
				clear();
			}
		});

		lstDonors.addValueChangeHandler(new ValueChangeHandler<FundDTO>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<FundDTO> event) {
				selected  = event.getValue();				
				showContainer(divDropdownContainer,true);
				showContainer(divCreateContainer,selected!=null);
				
				if(selected!=null){
					txtDescription.setValue(selected.getDescription());
					txtName.setValue(selected.getName());	
					chkActive.setValue(selected.isActive());
				}
			}
		});
		
	}

	public DonorSaveView(List<FundDTO> funds) {
		this();
		this.donors = funds;
		lstDonors.setItems(funds);
	}

	public void showContainer(HTMLPanel panel,boolean show) {
		if (show) {
			panel.removeStyleName("hidden");
		} else {
			panel.addStyleName("hidden");
		}
	}

	private void clear() {
		selected = null;
		lstDonors.setValue(null);
		txtDescription.setValue(null);
		txtName.setValue(null);
	}

	public FundDTO getDonor() {

		FundDTO fund = selected;
		if(fund==null){
			fund = new FundDTO();
		}
		
		fund.setActive(chkActive.getValue());
		fund.setName(txtName.getValue());
		fund.setDescription(txtDescription.getValue());
		
		return fund;
	}
	
	public boolean isValid(){
		boolean isValid = true;
		issues.clear();
		
		if(isNullOrEmpty(txtName.getValue())){
			isValid=false;
			issues.addError("Name is required");
		}
		
		if(isNullOrEmpty(txtDescription.getValue())){
			isValid=false;
			issues.addError("Description is required");
		}
		
		if(selected==null && txtName.getValue()!=null){
			//new -- validate donor Name
			for(FundDTO dto: donors){
				if(dto.getName().equals(txtName.getValue())){
					isValid=false;
					issues.addError("A Donor named '"+txtName.getValue()
							+"' already exists");
				}
			}
		}
		
		return isValid;
	}

}
