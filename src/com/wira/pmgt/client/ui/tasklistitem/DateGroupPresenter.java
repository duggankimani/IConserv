package com.wira.pmgt.client.ui.tasklistitem;


import java.util.Date;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.service.ServiceCallback;
import com.wira.pmgt.client.ui.events.PresentTaskEvent;
import com.wira.pmgt.client.ui.events.PresentTaskEvent.PresentTaskHandler;
import com.wira.pmgt.client.ui.util.DateUtils;

public class DateGroupPresenter extends
		PresenterWidget<DateGroupPresenter.MyView> implements PresentTaskHandler{

	public interface MyView extends View {
		void setDate(Date date);
	}

	public static final Object ITEM_SLOT = new Object();
	
	private IndirectProvider<TaskItemPresenter> presenterProvider;
	
	Date date;
	
	@Inject
	public DateGroupPresenter(final EventBus eventBus, final MyView view, Provider<TaskItemPresenter> provider) {
		super(eventBus, view);
		presenterProvider = new StandardProvider<TaskItemPresenter>(provider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(PresentTaskEvent.TYPE, this);
	}
	
	public void setDate(Date dt){
		this.date = dt;
		getView().setDate(dt);
	}
	
	@Override
	public void onPresentTask(final PresentTaskEvent event) {
		Date docDate = event.getDoc().getCreated();
		//System.err.println(">>>>>>>>>>>>>>> Present "+docDate+" :: "+event.getDoc());
		if(!CalendarUtil.isSameDate(date, docDate)){
			return;
		}
				
		presenterProvider.get(new ServiceCallback<TaskItemPresenter>() {
			
			@Override
			public void processResult(TaskItemPresenter result) {
				result.setDocSummary(event.getDoc());
				DateGroupPresenter.this.addToSlot(ITEM_SLOT, result);
			}
			
		});			
	}
		
	
	@Override
	protected void onHide() {
		super.onHide();
		this.unbind();
	}
	
}
