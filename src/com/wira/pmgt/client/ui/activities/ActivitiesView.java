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
import com.wira.pmgt.client.ui.component.BulletPanel;
import com.wira.pmgt.client.ui.component.TableView;
import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class ActivitiesView extends ViewImpl implements
		ActivitiesPresenter.IActivitiesView {

	private final Widget widget;

	@UiField HTMLPanel divContent;
	@UiField HTMLPanel divNoContent;
	@UiField TableView tblView;
	@UiField SpanElement spnBudget;
	@UiField Anchor aNewOutcome;
	@UiField Anchor aNewActivity;
	@UiField BulletListPanel listPanel;
	
	@UiField HeadingElement spnTitle;

	public interface Binder extends UiBinder<Widget, ActivitiesView> {
	}

	@Inject
	public ActivitiesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		listPanel.setId("mytab");
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
	
	public void createTab(String text, boolean active){
		BulletPanel li = new BulletPanel();
		Anchor a = new Anchor(text);
		a.setHref("#...");
		li.add(a);
		if(active){
			li.addStyleName("active");
		}else{
			li.removeStyleName("active");
		}
		listPanel.add(li);
	}

	@Override
	public void setActivities(List<IsProgramActivity> programs) {
		/*Table samples */
		tblView.clearRows();
		tblView.setAutoNumber(false);
		List<String> names = new ArrayList<String>();
		names.add("Checkbox");
		names.add("TITLE");
		names.add("STATUS");
		names.add("PROGRESS");
		names.add("RATING");
		names.add("BUDGET");
		tblView.setHeaders(names);

		for(IsProgramActivity activity: programs){
			tblView.addRow(new CheckBox(), new InlineLabel(activity.getName()),new InlineLabel("CREATED"),
					new InlineLabel("0%"), new InlineLabel("N/A"), new InlineLabel(activity.getBudgetAmount()==null? null: activity.getBudgetAmount()+""));
		}
	}

	@Override
	public void setPrograms(List<IsProgramActivity> programs) {
		if(programs==null){
			return;
		}
		for(IsProgramActivity activity: programs){
			createTab(activity.getName(), programs.indexOf(activity)==0);
		}
	}

}
