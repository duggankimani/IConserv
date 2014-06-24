package com.wira.pmgt.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.MyHTMLPanel;

public class ColorLabel extends Composite {

	private static ColorLabelUiBinder uiBinder = GWT
			.create(ColorLabelUiBinder.class);

	@UiField
	HTMLPanel chartContainer;

	interface ColorLabelUiBinder extends UiBinder<Widget, ColorLabel> {
	}

	public ColorLabel() {
		initWidget(uiBinder.createAndBindUi(this));
		chartContainer.getElement().getStyle().setMarginTop(0.0, Unit.PX);
	}

	public ColorLabel(Double width, Double MarginTop, String... colors) {
		this();
		for (final String text : colors) {
			MyHTMLPanel panel = new MyHTMLPanel("span2");
			InlineLabel label = new InlineLabel();
			label.addStyleName("label label-" + text);
			//label.getElement().setInnerHTML("<i class='icon-question-sign'/>");
			label.getElement().setInnerHTML("20%");
			
			label.getElement().getStyle().setHeight(13, Unit.PX);
			label.getElement().getStyle().setMarginTop(MarginTop, Unit.PX);
			
			panel.getElement().getStyle().setWidth(width, Unit.PX);
			panel.getElement().getStyle().setMarginLeft(0, Unit.PX);
			panel.add(label);
			chartContainer.add(panel);
		}
		// setMarginTop(MarginTop);
	}

	public void setMarginTop(Double value) {
	}

}
