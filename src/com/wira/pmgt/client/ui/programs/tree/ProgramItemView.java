package com.wira.pmgt.client.ui.programs.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.ProgramTreeModel;

public class ProgramItemView extends Composite {

	private static ProgramItemWidgetUiBinder uiBinder = GWT
			.create(ProgramItemWidgetUiBinder.class);

	interface ProgramItemWidgetUiBinder extends
			UiBinder<Widget, ProgramItemView> {
	}

	@UiField
	CheckBox chkSelect;
	@UiField
	SpanElement spnName;
	@UiField
	SpanElement divRowStrip;
	
	@UiField
	SpanElement spnLabel;

	public ProgramItemView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	ProgramTreeModel model;

	public ProgramItemView(ProgramTreeModel model) {
		this();
		this.model = model;
		spnName.setInnerText(model.getName());
	}

	public void enableMoveFor(ProgramDetailType typeToMove) {
		setStrip();
		if (typeToMove == null) {
			chkSelect.addStyleName("hide");
			return;
		}

		if (model.getType() == ProgramDetailType.OUTCOME
				|| model.getType() == ProgramDetailType.PROGRAM) {
			spnName.addClassName("bold");
		}

		if (typeToMove == ProgramDetailType.ACTIVITY
				&& model.getType() != ProgramDetailType.OUTCOME) {
			chkSelect.getParent().addStyleName("hide");
			return;
		}

		if (typeToMove == ProgramDetailType.TASK) {
			if (model.getType() != ProgramDetailType.ACTIVITY
					&& model.getType() != ProgramDetailType.TASK) {
				chkSelect.getParent().addStyleName("hide");
				return;
			}
		}

	}

	public void setSelected(boolean selected) {
		chkSelect.setValue(selected);
	}

	public void addValueChangeHandler(ValueChangeHandler<Boolean> vch) {
		chkSelect.addValueChangeHandler(vch);
	}

	private void setStrip() {
		switch (model.getType()) {

		/* Set color for ProgramTypes */
		case PROGRAM:
			divRowStrip.addClassName("label-success");
			break;
		case OUTCOME:
			divRowStrip.addClassName("label-info");
			break;
		case OBJECTIVE:
			divRowStrip.addClassName("label-warning");
			break;
		case ACTIVITY:
			divRowStrip.addClassName("label-success");
			break;
		case TASK:
			divRowStrip.addClassName("label-default");
			break;
		default:
			divRowStrip.addClassName("label-default");
			break;
		}

		String firstName = model.getType().getDisplayName().substring(0, 1);

		if (model.getType() == ProgramDetailType.OBJECTIVE) {
			firstName = "B";
		}
		spnLabel.setInnerText(firstName);
	}

}
