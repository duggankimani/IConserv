package com.wira.pmgt.server.actionhandlers;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.client.util.Definitions;
import com.wira.pmgt.server.ServerConstants;
import com.wira.pmgt.server.dao.helper.SettingsDaoHelper;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.server.helper.jbpm.VersionManager;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.settings.SETTINGNAME;
import com.wira.pmgt.shared.model.settings.Setting;
import com.wira.pmgt.shared.requests.GetContextRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetContextRequestResult;

import static com.wira.pmgt.server.ServerConstants.*;

public class GetContextRequestActionHandler extends 
		BaseActionHandler<GetContextRequest, GetContextRequestResult> {

	private final Provider<HttpServletRequest> httpRequest;

	@Inject
	public GetContextRequestActionHandler(Provider<HttpServletRequest> httpRequest) {
		this.httpRequest= httpRequest;
	}

	@Override
	public void execute(GetContextRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		HttpSession session = httpRequest.get().getSession(false);
		
		//Object sessionid=session.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
		Object user = session.getAttribute(ServerConstants.USER);

		GetContextRequestResult result = (GetContextRequestResult)actionResult;
		result.setIsValid(session!=null && user!=null);
		
		if(result.getIsValid()){
			result.setUser((HTUser)user);
			result.setGroups(LoginHelper.get().getGroupsForUser(result.getUser().getUserId()));
		}
		
		result.setVersion(VersionManager.getVersion());
		
		Setting setting = SettingsDaoHelper.getSetting(SETTINGNAME.ORGNAME);
		if(setting!=null){
			Object value = setting.getValue().getValue();
			result.setOrganizationName(value==null? null: value.toString());
		}
				
	}

	@Override
	public void undo(GetContextRequest action, GetContextRequestResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetContextRequest> getActionType() {
		return GetContextRequest.class;
	}
}
