package com.wira.pmgt.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.dao.helper.FormDaoHelper;
import com.wira.pmgt.server.dao.helper.ProcessDefHelper;
import com.wira.pmgt.server.dao.model.ADDocType;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.DocumentType;
import com.wira.pmgt.shared.model.ProcessDef;
import com.wira.pmgt.shared.model.form.FormModel;
import com.wira.pmgt.shared.requests.GetFormModelRequest;
import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.GetFormModelResponse;

public class GetFormModelRequestActionHandler extends
		BaseActionHandler<GetFormModelRequest, GetFormModelResponse> {

	@Inject
	public GetFormModelRequestActionHandler() {
	}

	@Override
	public void execute(GetFormModelRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {

		String requestFor = action.getType();
		Boolean loadChildrenInfo = action.getLoadChildrenToo();
		Long id = action.getId();
		Long parentId = action.getParentId();

		GetFormModelResponse response = (GetFormModelResponse) actionResult;

		FormModel model = null;

		List<FormModel> models = new ArrayList<>();
		switch (requestFor) {
		case FormModel.FORMMODEL:
			if(id!=null){
				model = FormDaoHelper.getForm(id, loadChildrenInfo);
				models.add(model);
			}else if(action.getTaskId()!=null){
				model = FormDaoHelper.getFormByName(JBPMHelper.get().getTaskName(action.getTaskId()));
				if(model!=null)
					models.add(model);
			}else if(action.getDocumentId()!=null){
				
				ADDocType type = DB.getDocumentDao().getDocumentTypeByDocumentId(action.getDocumentId());
				Long formId=null;
				
				if(type!=null)
					formId = DB.getDocumentDao().getFormId(type.getId());
				
				if(formId!=null){
					model = FormDaoHelper.getForm(formId, true);
					models.add(model);
				}
								
			}else{
				models.addAll(FormDaoHelper.getForms());
			}
			break;

		case FormModel.FIELDMODEL:
			
			if(id!=null){
				model = FormDaoHelper.getField(id);
				models.add(model);
			}else if(parentId!=null){
				models.addAll(FormDaoHelper.getFields(parentId,true));
			}
			
			break;

		case FormModel.PROPERTYMODEL:
			model = FormDaoHelper.getProperty(id);
			models.add(model);
			break;

		}
		
		response.setFormModel(models);

	}

	@Override
	public Class<GetFormModelRequest> getActionType() {
		return GetFormModelRequest.class;
	}
}
