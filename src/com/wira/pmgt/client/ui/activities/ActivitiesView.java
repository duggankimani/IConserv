package com.wira.pmgt.client.ui.activities;

import java.util.List;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.BulletPanel;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.IsProgramActivity;

public class ActivitiesView extends ViewImpl implements
		ActivitiesPresenter.IActivitiesView {

	private final Widget widget;

	@UiField HTMLPanel divContent;
	@UiField HTMLPanel divNoContent;
	@UiField ActivitiesTable tblView;
	@UiField SpanElement spnBudget;
	@UiField Anchor aNewOutcome;
	@UiField Anchor aNewActivity;
	@UiField Anchor aNewObjective;
	@UiField Anchor aNewTask;
	@UiField Anchor aEdit;
	
	@UiField BulletListPanel listPanel;
	
	@UiField HeadingElement spnTitle;

	List<IsProgramActivity> programs=null;
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

	public void setBudget(Double number) {
		if(number!=null){
			spnBudget.setInnerHTML(NumberFormat.getCurrencyFormat().format(number));
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
			spnTitle.setInnerText("Programs Management");
		} else {
			divContent.addStyleName("hidden");
			divNoContent.removeStyleName("hidden");
		}
	}
	
	public void createTab(String text, long id,boolean active){
		BulletPanel li = new BulletPanel();
		Anchor a = new Anchor(text);
		HTMLPanel opt = new HTMLPanel("<span class='icon-caret-down pull-right'></span>");
		a.setHref("#home;page=activities;activity="+id);
		li.add(a);
		//li.add(opt);
		if(active){
			li.addStyleName("active");
		}else{
			li.removeStyleName("active");
		}
		listPanel.add(li);
	}

	@Override
	public void setActivities(List<IsProgramActivity> programs) {
		tblView.setData(programs);
	}

	@Override
	public void setPrograms(List<IsProgramActivity> programs) {
		showContent(!(programs==null || programs.isEmpty()));
		this.programs = programs;
		listPanel.clear();
		if(programs==null){
			return;
		}
		for(IsProgramActivity activity: programs){
			boolean first = programs.indexOf(activity)==0;
			createTab(activity.getName(),activity.getId(), first);
		}
	}

	/**
	 * Sets Parent Activity
	 */
	
	public void setActivity(IsProgramActivity singleResult) {
		setSelection(singleResult.getType());
		if(singleResult.getType()==ProgramDetailType.PROGRAM){
			//select tab
			selectTab(singleResult.getId());
			setBudget(singleResult.getBudgetAmount());
			setTitle(singleResult.getName());
		}
		setActivities(singleResult.getChildren());
	}

	private void show(Anchor aAnchor, boolean show) {
		if(show){
			aAnchor.getElement().getParentElement().removeClassName("hide");
		}else{
			aAnchor.getElement().getParentElement().addClassName("hide");
		}
	}

	private void selectTab(Long id) {
		int size = listPanel.getWidgetCount();
		for(int i=0; i<size; i++){
			BulletPanel li = (BulletPanel)listPanel.getWidget(i);
			
			Anchor a = (Anchor)li.getWidget(0);
			String href = "#home;page=activities;activity="+id;
			boolean active = a.getHref().endsWith(href);
			
			if(active){
				li.addStyleName("active");
			}else{
				li.removeStyleName("active");
			}
		}
	}

	@Override
	public HasClickHandlers getNewObjectiveLink() {

		return aNewObjective;
	}

	@Override
	public void setSelection(ProgramDetailType type) {
		show(aNewOutcome,false);
		show(aNewObjective,false);
		show(aNewActivity,false);
		show(aNewTask,false);
		show(aEdit, true);
		
		if(type==ProgramDetailType.PROGRAM){
			//select tab
			show(aNewOutcome,true);
			show(aNewObjective,true);
			show(aEdit, false);
		}else if(type==ProgramDetailType.OUTCOME){
			show(aNewActivity,true);
		}else if(type==ProgramDetailType.ACTIVITY){
			show(aNewActivity,true);
			show(aNewTask,true);
		}
	}

	@Override
	public HasClickHandlers getEditLink() {
		
		return aEdit;
	}

}
