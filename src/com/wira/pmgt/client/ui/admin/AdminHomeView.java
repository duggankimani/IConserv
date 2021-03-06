package com.wira.pmgt.client.ui.admin;

import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.admin.AdminHomePresenter.ADMINPAGES;

public class AdminHomeView extends ViewImpl implements
		AdminHomePresenter.MyView {

	private final Widget widget;

	@UiField
	LIElement liDashboard;
	@UiField
	LIElement liProcesses;
	@UiField
	LIElement liUsers;
	// @UiField LIElement liReports;
	@UiField
	LIElement liSettings;
	@UiField
	LIElement liDS;
	@UiField
	SpanElement spanTitle;
	@UiField
	SpanElement iconTitle;
	@UiField
	LIElement liFormBuilder;

	@UiField
	HTMLPanel divContent;

	public interface Binder extends UiBinder<Widget, AdminHomeView> {
	}

	@Inject
	public AdminHomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == AdminHomePresenter.CONTENT_SLOT) {
			divContent.clear();
			if (content != null)
				divContent.add(content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void SetDashboardLink(boolean status, ADMINPAGES page) {
		if (status) {
			liDashboard.setClassName("active");
			iconTitle.setClassName(page.getDisplayIcon());
			spanTitle.setInnerHTML(page.getDisplayName());
		} else
			liDashboard.removeClassName("active");
	}

	public void SetProcessLink(boolean status, ADMINPAGES page) {
		if (status) {
			liProcesses.setClassName("active");
			iconTitle.setClassName(page.getDisplayIcon());
			spanTitle.setInnerHTML(page.getDisplayName());
		} else
			liProcesses.removeClassName("active");
	}

	public void SetUsersLink(boolean status, ADMINPAGES page) {
		if (status) {
			liUsers.setClassName("active");
			iconTitle.setClassName(page.getDisplayIcon());
			spanTitle.setInnerHTML(page.getDisplayName());
		} else
			liUsers.removeClassName("active");
	}

	public void SetReportLink(boolean status, ADMINPAGES page) {
		if (status) {
			// liReports.setClassName("active");
			iconTitle.setClassName(page.getDisplayIcon());
			spanTitle.setInnerHTML(page.getDisplayName());
		} else {
			// liReports.removeClassName("active");
		}
	}

	@Override
	public void SetDSLink(boolean status, ADMINPAGES page) {
		if (status) {
			liDS.setClassName("active");
			iconTitle.setClassName(page.getDisplayIcon());
			spanTitle.setInnerHTML(page.getDisplayName());
		} else
			liDS.removeClassName("active");
	}

	@Override
	public void SetSettingsLink(boolean status, ADMINPAGES page) {
		if (status) {
			liSettings.setClassName("active");
			iconTitle.setClassName(page.getDisplayIcon());
			spanTitle.setInnerHTML(page.getDisplayName());
		} else
			liSettings.removeClassName("active");
	}

	@Override
	public void SetFormBuilderLinks(boolean status, ADMINPAGES page) {
		if (status) {
			liFormBuilder.setClassName("active");
			iconTitle.setClassName(page.getDisplayIcon());
			spanTitle.setInnerHTML(page.getDisplayName());
		} else
			liFormBuilder.removeClassName("active");
	}

	@Override
	public void clearAllLinks() {
		liDashboard.removeClassName("active");
		liProcesses.removeClassName("active");
		liUsers.removeClassName("active");
		liSettings.removeClassName("active");
		// liReports.removeClassName("active");
		liFormBuilder.removeClassName("active");
		liDS.removeClassName("active");
	}

	public void showFormLinkOnly(boolean show) {
		if (show) {
			liDashboard.getStyle().setDisplay(Display.NONE);
			liProcesses.getStyle().setDisplay(Display.NONE);
			liUsers.getStyle().setDisplay(Display.NONE);
			liSettings.getStyle().setDisplay(Display.NONE);
			liDS.getStyle().setDisplay(Display.NONE);
		} else {
			liDashboard.getStyle().setDisplay(Display.BLOCK);
			liProcesses.getStyle().setDisplay(Display.BLOCK);
			liUsers.getStyle().setDisplay(Display.BLOCK);
			liSettings.getStyle().setDisplay(Display.BLOCK);
			liDS.getStyle().setDisplay(Display.BLOCK);
		}
	}

	@Override
	public void limitToForm(boolean limit) {
		if (limit) {
			showFormLinkOnly(true);
		}else{
			showFormLinkOnly(false);
		}
	}

}
