package com.wira.pmgt.client.ui.admin.processes;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProcessView extends ViewImpl implements ProcessPresenter.IProcessView {

	private final Widget widget;
	@UiField Anchor aNewProcess;
	@UiField HTMLPanel tblRow;
	@UiField Anchor aStartProcesses;
	
	public interface Binder extends UiBinder<Widget, ProcessView> {
	}

	@Inject
	public ProcessView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getaNewProcess() {
		return aNewProcess;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if(slot == ProcessPresenter.TABLE_SLOT){
			tblRow.clear();
			if(content!=null){
				tblRow.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
	}
	
	@Override
	public void addToSlot(Object slot, Widget content) {
		if(slot == ProcessPresenter.TABLE_SLOT){
			if(content!=null){
				tblRow.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
	}
	
	@Override
	public HasClickHandlers getStartAllProcesses(){
		return aStartProcesses;
	}
	
}
