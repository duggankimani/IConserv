package com.wira.pmgt.server.actionhandlers;

import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DSConfigHelper;
import com.wira.pmgt.server.dao.model.DataSourceConfig;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.LookupLoaderImpl;
import com.wira.pmgt.shared.model.DSConfiguration;
import com.wira.pmgt.shared.model.Status;
import com.wira.pmgt.shared.requests.GetDSStatusRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetDSStatusResponse;

public class GetDSStatusRequestActionHandler extends
		BaseActionHandler<GetDSStatusRequest, GetDSStatusResponse> {

	@Inject
	public GetDSStatusRequestActionHandler() {
	}

	@Override
	public void execute(GetDSStatusRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		String configName = action.getConfigName();
		LookupLoaderImpl impl = new LookupLoaderImpl();
		
		GetDSStatusResponse response = (GetDSStatusResponse)actionResult;
		if(configName!=null){
			
			boolean isActive = impl.testDatasourceName(configName);
			DSConfiguration config = DSConfigHelper.getConfigurationByName(configName);
			config.setStatus(isActive?Status.RUNNING:Status.INACTIVE);
			response.addConfig(config);
			
		}else{
			List<DSConfiguration> configs =  DSConfigHelper.getConfigurations();
			if(configs!=null){
				for(DSConfiguration config: configs){
					boolean isActive = impl.testDatasourceName(config.getName());	
					config.setStatus(isActive?Status.RUNNING:Status.INACTIVE);
					response.addConfig(config);
				}
			}
		}		
		
	}
	
	@Override
	public Class<GetDSStatusRequest> getActionType() {
		return GetDSStatusRequest.class;
	}
}
