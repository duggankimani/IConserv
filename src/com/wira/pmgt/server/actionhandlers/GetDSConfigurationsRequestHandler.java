package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DSConfigHelper;
import com.wira.pmgt.server.db.LookupLoaderImpl;
import com.wira.pmgt.shared.model.DSConfiguration;
import com.wira.pmgt.shared.model.Status;
import com.wira.pmgt.shared.requests.GetDSConfigurationsRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDSConfigurationsResponse;

public class GetDSConfigurationsRequestHandler
		extends
		BaseActionHandler<GetDSConfigurationsRequest, GetDSConfigurationsResponse> {

	@Inject
	public GetDSConfigurationsRequestHandler() {
	}

	@Override
	public void execute(GetDSConfigurationsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetDSConfigurationsResponse response = (GetDSConfigurationsResponse)actionResult;
		
		List<DSConfiguration> configs =  DSConfigHelper.getConfigurations();
		if(configs!=null){
			LookupLoaderImpl impl = new LookupLoaderImpl();
			for(DSConfiguration config: configs){
				boolean isActive = impl.testDatasourceName(config.getName());	
				config.setStatus(isActive?Status.RUNNING:Status.INACTIVE);
			}
		}
		response.setConfigurations(configs);
	}
	
	@Override
	public Class<GetDSConfigurationsRequest> getActionType() {
		return GetDSConfigurationsRequest.class;
	}
}
