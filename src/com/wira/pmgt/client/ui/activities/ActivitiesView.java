package com.wira.pmgt.client.ui.activities;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.TableView;

public class ActivitiesView extends ViewImpl implements
		ActivitiesPresenter.IActivitiesView {

	private final Widget widget;

	public @UiField
	HTMLPanel divContent;
	@UiField
	HTMLPanel divNoContent;
	@UiField
	TableView tblView;
	@UiField
	SpanElement spnBudget;
	@UiField
	Anchor aNewOutcome;
	
	@UiField
	Anchor aNewActivity;
	
	@UiField HeadingElement spnTitle;
	@UiField BulletListPanel crumbContainer;

	public interface Binder extends UiBinder<Widget, ActivitiesView> {
	}

	@Inject
	public ActivitiesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		/*BreadCrumb Samples*/
		createCrumb("Home", false);
		createCrumb("WildLife Management", false);
		createCrumb("Increased Understanding ...", true);
		
		/*Table samples */
		tblView.setAutoNumber(false);
		List<String> names = new ArrayList<String>();
		names.add("Checkbox");
		names.add("TITLE");
		names.add("STATUS");
		names.add("PROGRESS");
		names.add("RATING");
		names.add("BUDGET");
		tblView.setHeaders(names);

		tblView.addRow(new CheckBox(), new InlineLabel("Test"),new InlineLabel("Test2"),
				new InlineLabel(""), new InlineLabel(""), new InlineLabel(""));
		
		tblView.addRow(new CheckBox(), new InlineLabel("Test"),new InlineLabel("Test2"),
				new InlineLabel("Test2"), new InlineLabel("Test 3"), new InlineLabel(""));

	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public void setTitle(String text) {
		if(text!=null){
			spnTitle.setInnerText(text);
		}else{
			spnTitle.setInnerText("Programs & Activities");
		}
	}

	public void setBudget(String number) {
		if(number!=null){
			spnBudget.setInnerHTML(number);
		}
	}
	
	public HasClickHandlers getaNewOutcome() {
		return aNewOutcome;
	} 
	
	public HasClickHandlers getaNewActivity() {
		return aNewActivity;
	}
	
	@Override
	public void showContent(boolean status) {
		if (status) {
			divContent.removeStyleName("hidden");
			divNoContent.addStyleName("hidden");
		} else {
			divContent.addStyleName("hidden");
			divNoContent.removeStyleName("hidden");
		}
	}
	
	public void createCrumb(String text, Boolean isActive){
		BreadCrumbItem crumb = new BreadCrumbItem();
		crumb.setActive(isActive);
		crumb.setLinkText(text);
		crumbContainer.add(crumb);
	}

}
