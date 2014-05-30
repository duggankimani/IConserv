package com.wira.pmgt.client.ui.programs;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.ActionLink;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.Dropdown;
import com.wira.pmgt.shared.model.program.PeriodDTO;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class ProgramHeader extends Composite {

	private static ActivityHeaderUiBinder uiBinder = GWT
			.create(ActivityHeaderUiBinder.class);

	interface ActivityHeaderUiBinder extends UiBinder<Widget, ProgramHeader> {
	}

	@UiField
	SpanElement spnBudget;
	@UiField
	InlineLabel spnDates;
	@UiField
	ActionLink aProgramEdit;
	@UiField
	FocusPanel panelTitle;
	@UiField
	HTMLPanel divPopup;
	@UiField
	HeadingElement spnTitle;
	@UiField
	Dropdown<PeriodDTO> periodDropdown;
	@UiField
	DivElement divHeader;
	
	private Long programId;

	public ProgramHeader() {
		initWidget(uiBinder.createAndBindUi(this));

		periodDropdown
				.addValueChangeHandler(new ValueChangeHandler<PeriodDTO>() {

					@Override
					public void onValueChange(ValueChangeEvent<PeriodDTO> event) {
						setDates(event.getValue().getDescription());
					}
				});

		spnDates.getElement().setAttribute("data-toggle", "dropdown");
	}
	
	public void setDates(String text) {
		spnDates.getElement().setInnerText(text);
	}
	
	public void setTitle(String title) {
		if (title != null) {
			spnBudget.setTitle(title);
		}
	}
	
	public void setText(String text) {
		if (text != null) {
			spnTitle.setInnerText(text);
		} else {
			spnTitle.setInnerText("Programs & Activities");
		}
	}
	
	
	
	public void setBudget(String text){
		spnBudget.setInnerHTML(text);
	}
	
	public void setPeriodDropdown(List<PeriodDTO> periods){
		periodDropdown.setValues(periods);
	}
	
	public Dropdown<PeriodDTO> getPeriodDropdown() {
		return periodDropdown;
	}
	
	

	private BreadCrumbItem createCrumb(String text, String title, Long id, Boolean isActive) {
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

		return crumb;
	}
	
	
	public BulletListPanel setBreadCrumbs(List<ProgramSummary> summaries) {
		BulletListPanel crumbContainer =  new BulletListPanel();
		crumbContainer.setStyleName("breadcrumb");
		crumbContainer.clear();
		BreadCrumbItem crumb = createCrumb("Home", "Home", 0L, false );
		crumbContainer.add(crumb);
		for (int i = summaries.size() - 1; i > -1; i--) {
			ProgramSummary summary = summaries.get(i);
			crumb=createCrumb(summary.getName(), summary.getDescription(),
					summary.getId(), i == 0);
			crumbContainer.add(crumb);
		}
		
		return crumbContainer;
	}
	
	/*
	 * Get href from Id
	 */
	private String getHref(Long id) {
		String href = "";
		if (id == null || id == 0) {
			href = "#home;page=activities;activity=0";
		} else if (programId == null) {
			href = "#home;page=activities;activity=0d" + id;
		} else if (id != programId) {
			href = "#home;page=activities;activity=" + programId + "d" + id;
		} else {
			href = "#home;page=activities;activity=" + id;
		}
		return href;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	
	public void setLeftMargin(Boolean status) {
		if(status){
			divHeader.removeClassName("no-margin-left");
		}else{
			divHeader.addClassName("no-margin-left");
		}
	}
	

}
