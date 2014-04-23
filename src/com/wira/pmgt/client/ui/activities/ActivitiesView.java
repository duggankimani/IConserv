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
import com.wira.pmgt.client.ui.component.BreadCrumbItem;
import com.wira.pmgt.client.ui.component.BulletListPanel;
import com.wira.pmgt.client.ui.component.BulletPanel;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.ProgramSummary;

public class ActivitiesView extends ViewImpl implements
		ActivitiesPresenter.IActivitiesView {

	private final Widget widget;

	@UiField HTMLPanel divContent;
	@UiField HTMLPanel divNoContent;
	@UiField ActivitiesTable tblView;
	@UiField SpanElement spnBudget;
	@UiField Anchor aNewOutcome;
	@UiField Anchor aNewActivity;
	@UiField BulletListPanel listPanel;
	
	@UiField HeadingElement spnTitle;
	@UiField BulletListPanel crumbContainer;

	List<IsProgramActivity> programs=null;
	public interface Binder extends UiBinder<Widget, ActivitiesView> {
	}

	@Inject
	public ActivitiesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		listPanel.setId("mytab");
		/*BreadCrumb Samples*/
//		createCrumb("Home", false);
//		createCrumb("WildLife Management", false);
//		createCrumb("Increased Understanding ...", true);
		
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
		} else {
			divContent.addStyleName("hidden");
			divNoContent.removeStyleName("hidden");
		}
	}
	
	public void createCrumb(String text,Long id, Boolean isActive){
		BreadCrumbItem crumb = new BreadCrumbItem();
		crumb.setActive(isActive);
		crumb.setLinkText(text);
		crumb.setHref("#home;page=activities;activity="+id);
		crumbContainer.add(crumb);
	}
	
	public void createTab(String text, long id,boolean active){
		BulletPanel li = new BulletPanel();
		Anchor a = new Anchor(text);
		a.setHref("#home;page=activities;activity="+id);
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
		tblView.setData(programs);
	}

	@Override
	public void setPrograms(List<IsProgramActivity> programs) {
		showContent(!(programs==null || programs.isEmpty()));
		this.programs = programs;
		
		if(programs==null){
			return;
		}
		for(IsProgramActivity activity: programs){
			boolean first = programs.indexOf(activity)==0;
			createTab(activity.getName(),activity.getId(), first);
		}
	}

	@Override
	public void setActivity(IsProgramActivity singleResult) {
		if(singleResult.getType()==ProgramDetailType.PROGRAM){
			//select tab
			selectTab(singleResult.getId());
			setBudget(singleResult.getBudgetAmount());
		}
		List<ProgramSummary> summaries = singleResult.getProgramSummary();
		for(int i=summaries.size()-1; i>-1; i--){
			ProgramSummary summary = summaries.get(i);
			createCrumb(summary.getName(), summary.getId(), i==0);
		}
		
		setActivities(singleResult.getChildren());
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

}
