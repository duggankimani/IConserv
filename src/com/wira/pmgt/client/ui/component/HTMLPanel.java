package com.wira.pmgt.client.ui.component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class HTMLPanel extends ComplexPanel {

	public HTMLPanel() {
		setElement(Document.get().createDivElement());
	}

	public void setCssId(String id) {
		getElement().setId(id);
	}

	public void add(Widget w) {
		super.add(w, getElement());
	}

}
