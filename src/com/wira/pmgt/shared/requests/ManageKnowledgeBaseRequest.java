package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.model.ManageProcessAction;
import com.wira.pmgt.shared.requests.BaseRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.ManageKnowledgeBaseResponse;

import java.lang.Long;

public class ManageKnowledgeBaseRequest extends
		BaseRequest<ManageKnowledgeBaseResponse> {

	private Long processDefId;

	private ManageProcessAction action;
	
	private boolean force;
	
	@SuppressWarnings("unused")
	private ManageKnowledgeBaseRequest() {
		// For serialization only
	}

	public ManageKnowledgeBaseRequest(Long processDefId, ManageProcessAction action, boolean force) {
		this.processDefId = processDefId;
		this.action = action;
		this.force = force;
	}

	public Long getProcessDefId() {
		return processDefId;
	}

	public ManageProcessAction getAction() {
		return action;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new ManageKnowledgeBaseResponse();
	}

	public boolean isForce(){
		return force;
	}
}
