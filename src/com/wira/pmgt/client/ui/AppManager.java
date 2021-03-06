package com.wira.pmgt.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.admin.formbuilder.propertypanel.PropertyPanelPresenter;
import com.wira.pmgt.client.ui.popup.GenericPopupPresenter;
import com.wira.pmgt.shared.model.form.FormModel;
import com.wira.pmgt.shared.model.form.Property;

public class AppManager {

	@Inject
	static MainPagePresenter mainPagePresenter;
	@Inject
	static GenericPopupPresenter popupPresenter;
	@Inject
	static PropertyPanelPresenter propertyPanel;

	public static void showPopUp(String header, String content,
			final OnOptionSelected onOptionSelected, String... buttons) {
		showPopUp(header, new InlineLabel(content), onOptionSelected, buttons);
	}

	public static void showPopUp(String header, Widget widget,
			final OnOptionSelected onOptionSelected, String... buttons) {
		popupPresenter.setHeader(header);
		popupPresenter.setInSlot(GenericPopupPresenter.BODY_SLOT, null);
		popupPresenter.setInSlot(GenericPopupPresenter.BUTTON_SLOT, null);

		popupPresenter.getView().setInSlot(GenericPopupPresenter.BODY_SLOT,
				widget);

		for (final String text : buttons) {
			Anchor aLnk = new Anchor();
			if (text.equals("Cancel")) {
				// aLnk.setHTML("&nbsp;<i class=\"icon-remove\"></i>" +
				aLnk.setHTML(text);
				aLnk.setStyleName("btn btn-default pull-right");
			} else {
				aLnk.setHTML(text);
				// + "&nbsp;<i class=\"icon-double-angle-right\"></i>");
				aLnk.setStyleName("btn btn-primary pull-left");
			}

			aLnk.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (onOptionSelected instanceof OptionControl) {
						((OptionControl) onOptionSelected)
								.setPopupView((PopupView) (popupPresenter
										.getView()));
						onOptionSelected.onSelect(text);
					} else {
						popupPresenter.getView().hide();
						onOptionSelected.onSelect(text);
					}
				}
			});
			popupPresenter.getView().addToSlot(
					GenericPopupPresenter.BUTTON_SLOT, aLnk);
		}

		mainPagePresenter.addToPopupSlot(popupPresenter, false);
		// popupPresenter.getView().center();
	}

	public static void showPopUp(String header,
			PresenterWidget<ViewImpl> presenter,
			final OnOptionSelected onOptionSelected, String... buttons) {
		showPopUp(header, presenter.getWidget(), onOptionSelected, buttons);
	}

	public static void showPropertyPanel(FormModel parent,
			List<Property> properties, int top, int left, int arrowposition) {
		// Bad Fix - For correction
		propertyPanel.getView().showBody(false, null);

		propertyPanel.setProperties(parent, properties);
		int[] position = calculatePosition(top, left);
		propertyPanel.getView().getPopUpContainer()
				.setPopupPosition(position[1], position[0]);

		propertyPanel.getView().getiArrow().getElement().getStyle()
				.setTop(arrowposition, Unit.PX);

		propertyPanel.getView().getPopoverFocus().setFocus(true);
		mainPagePresenter.addToPopupSlot(propertyPanel, false);
	}

	public static void showCarouselPanel(Widget widget, int[] position,
			boolean isLeft) {
		// propertyPanel.getView().getPopUpContainer().clear();
		propertyPanel.getView().showBody(true, widget);

		if (isLeft) {
			propertyPanel.getView().getPopUpContainer()
					.removeStyleName("right");
			propertyPanel.getView().getPopUpContainer().addStyleName("left");
		} else {
			propertyPanel.getView().getPopUpContainer().removeStyleName("left");
			propertyPanel.getView().getPopUpContainer().addStyleName("right");
		}

		propertyPanel.getView().getPopUpContainer()
				.setPopupPosition(position[1], position[0]);
		mainPagePresenter.addToPopupSlot(propertyPanel, false);

		// propertyPanel.getView().getPopUpContainer().setModal(true);

	}

	/**
	 * Returns positions of the modal/popover in Relative to the browser size
	 * 
	 * @param %top, %left
	 * @return top(px),left(px)
	 */
	public static int[] calculatePosition(int top, int left) {

		int[] positions = new int[2];
		// ----Calculate the Size of Screen;
		int height = Window.getClientHeight();
		int width = Window.getClientWidth();

		/* Percentage to the Height and Width */
		double percentTop = (top / 100.0) * height;
		double percentLeft = (left / 100.0) * width;

		positions[0] = (int) percentTop;
		positions[1] = (int) percentLeft;

		return positions;
	}

	/*
	 * Hide the Carousel Output
	 */
	public static void hidePopup() {
		propertyPanel.getView().hide();
	}
}
