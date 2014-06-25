package com.wira.pmgt.client.ui.admin.formbuilder.component;

import java.util.List;

import org.aspectj.weaver.patterns.PerFromSuper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.MyHTMLPanel;
import com.wira.pmgt.client.ui.reports.Performance;
import com.wira.pmgt.client.ui.reports.Performance.PerformanceType;

public class ColorWidget extends Composite {

	private static ColorWidgetUiBinder uiBinder = GWT
			.create(ColorWidgetUiBinder.class);

	@UiField
	HTMLPanel chartContainer;

	interface ColorWidgetUiBinder extends UiBinder<Widget, ColorWidget> {
	}

	public ColorWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		chartContainer.getElement().getStyle().setMarginTop(0.0, Unit.PX);
	}

	public ColorWidget(List<Performance> performances) {
		this();

		for (Performance p1 : performances) {
			MyHTMLPanel panel = new MyHTMLPanel("span2");
			InlineLabel label = new InlineLabel();

			if (p1.isShowPercentage()) {
				label.getElement().setInnerHTML(p1.getPercentage() + "%");
			}
			String text = getCssStyle(p1.getPerfomanceType());
			label.addStyleName("label label-" + text);
			label.getElement().getStyle().setMarginTop(10.0, Unit.PX);
			label.getElement().getStyle().setHeight(13, Unit.PX);
			panel.setTitle(p1.getTitle());

			Double width = p1.getPercentage() * 1.9;
			width = width > 1 ? width - 2 : width;
			panel.getElement().getStyle().setWidth(width, Unit.PX);
			panel.getElement().getStyle().setMarginLeft(0, Unit.PX);
			panel.add(label);
			chartContainer.add(panel);
		}

		// setMarginTop(MarginTop);
	}

	private String getCssStyle(PerformanceType perfomanceType) {
		switch (perfomanceType) {
		case GOOD:
			return "success";

		case AVERAGE:
			return "info";

		case POOR:
			return "danger";

		case NODATA:
			return "default";

		default:
			return "default";
		}
	}

	public void setMarginTop(Double value) {
	}

}
