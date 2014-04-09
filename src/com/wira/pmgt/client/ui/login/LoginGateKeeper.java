package com.wira.pmgt.client.ui.login;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.wira.pmgt.client.util.AppContext;

@Singleton
public class LoginGateKeeper implements Gatekeeper {

	@Inject
	public LoginGateKeeper(){	
	}
	
	@Override
	public boolean canReveal() {
		
		return AppContext.isValid();
	}

}
