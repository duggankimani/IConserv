package com.wira.pmgt.shared.model.settings;

import com.wira.pmgt.shared.model.Listable;

public enum REPORTVIEWIMPL implements Listable{

	GOOGLE_DOCS, NEW_TAB, IFRAME;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name();
	}

	@Override
	public String getDisplayName() {
		return name();
	}
}
