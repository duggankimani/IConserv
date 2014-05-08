package com.wira.pmgt.client.ui.home;

import static com.wira.pmgt.client.ui.home.HomePresenter.ACTIVITIES_SLOT;

import java.util.HashMap;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.model.TaskType;

public class HomeView extends ViewImpl implements HomePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HomeView> {
	}

	
	@UiField
	HTMLPanel activityContainer;
	HTMLPanel mainContainer;

	// Filter Dialog Caret
	boolean isNotDisplayed = true;
	boolean isDocPopupDisplayed = false;

	@Inject
	public HomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);

	}

	public HTMLPanel getActivityContainer() {
		return activityContainer;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == ACTIVITIES_SLOT) {
			activityContainer.clear();
			if (content != null) {
				activityContainer.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void addToSlot(Object slot, Widget content) {
		super.addToSlot(slot, content);
	}


	@Override
	public void showmask(boolean mask) {
		if (mask) {
			activityContainer.addStyleName("working-request");
		} else {
			activityContainer.removeStyleName("working-request");
		}
	}

	@Override
	public void setHasItems(boolean b) {
		
	}

	@Override
	public void setHeading(String string) {

	}

	@Override
	public void bindAlerts(HashMap<TaskType, Integer> alerts) {
		
	}

	/*
	 * @Override public void setDocPopupVisible() { if(isDocPopupDisplayed){
	 * divDocPopup.removeStyleName("hidden"); isDocPopupDisplayed=false; }else{
	 * divDocPopup.addStyleName("hidden"); isDocPopupDisplayed=true; } }
	 */

}
