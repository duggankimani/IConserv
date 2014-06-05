package com.wira.pmgt.client.ui.donor.save;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.ActionLink;

public class DonorSaveView extends Composite {

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

	private static DonorSaveViewUiBinder uiBinder = GWT
			.create(DonorSaveViewUiBinder.class);

	interface DonorSaveViewUiBinder extends UiBinder<Widget, DonorSaveView> {
	}

	public DonorSaveView() {
		initWidget(uiBinder.createAndBindUi(this));

		aCreateDonor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showCreateContainer(true);
				showDropdownContainer(false);
			}
		});

		aEditDonor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showCreateContainer(false);
				showDropdownContainer(true);
			}
		});

	}

	public void showDropdownContainer(boolean show) {
		if (show) {
			divDropdownContainer.removeStyleName("hidden");
			divCreateContainer.addStyleName("hidden");
		} else {
			divDropdownContainer.addStyleName("hidden");
			divCreateContainer.removeStyleName("hidden");
		}
	}

	public void showCreateContainer(boolean show) {
		if (show) {
			divDropdownContainer.addStyleName("hidden");
			divCreateContainer.removeStyleName("hidden");
		} else {
			divDropdownContainer.removeStyleName("hidden");
			divCreateContainer.addStyleName("hidden");
		}
	}

}
