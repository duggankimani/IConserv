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

public class ProgramItemView extends Composite{

	private static ProgramItemWidgetUiBinder uiBinder = GWT
			.create(ProgramItemWidgetUiBinder.class);

	interface ProgramItemWidgetUiBinder extends
			UiBinder<Widget, ProgramItemView> {
	}

	@UiField CheckBox chkSelect;
	@UiField SpanElement spnName;
	
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
		if(typeToMove==null){
			chkSelect.addStyleName("hide");
			return;
		}
		
		if(typeToMove==ProgramDetailType.ACTIVITY && model.getType()!=ProgramDetailType.OUTCOME){
			chkSelect.addStyleName("hide");
			return;
		}
		
		if(typeToMove==ProgramDetailType.TASK){
			if(model.getType()!=ProgramDetailType.ACTIVITY && model.getType()!=ProgramDetailType.TASK){
				chkSelect.addStyleName("hide");
				return;
			}
		}
		
		
	}

	public void setSelected(boolean selected) {
		chkSelect.setValue(selected);
	}
	
	public void addValueChangeHandler(ValueChangeHandler<Boolean> vch){
		chkSelect.addValueChangeHandler(vch);
	}
	
}
