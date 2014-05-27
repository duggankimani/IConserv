package com.wira.pmgt.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.wira.pmgt.server.ServerConstants;
import com.wira.pmgt.server.actionhandlers.ApprovalRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.CheckPasswordRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.CreateDocumentActionHandler;
import com.wira.pmgt.server.actionhandlers.CreateFieldRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.CreateFormRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.DeleteAttachmentRequestHandler;
import com.wira.pmgt.server.actionhandlers.DeleteDSConfigurationEventActionHandler;
import com.wira.pmgt.server.actionhandlers.DeleteDocumentRequestHandler;
import com.wira.pmgt.server.actionhandlers.DeleteFormModelRequestHandler;
import com.wira.pmgt.server.actionhandlers.DeleteLineRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.DeleteProcessRequestHandler;
import com.wira.pmgt.server.actionhandlers.ExecuteWorkflowActionHandler;
import com.wira.pmgt.server.actionhandlers.ExportFormRequestHandler;
import com.wira.pmgt.server.actionhandlers.GetActivitiesRequestHandler;
import com.wira.pmgt.server.actionhandlers.GetAlertCountActionHandler;
import com.wira.pmgt.server.actionhandlers.GetAttachmentsRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetCommentsRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetContextRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetDSConfigurationsRequestHandler;
import com.wira.pmgt.server.actionhandlers.GetDSStatusRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetDashBoardDataRequestHandler;
import com.wira.pmgt.server.actionhandlers.GetDocumentRequestHandler;
import com.wira.pmgt.server.actionhandlers.GetDocumentTypesRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetErrorRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetFormModelRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetFormsRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetGroupsRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetItemActionHandler;
import com.wira.pmgt.server.actionhandlers.GetLongTasksRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetNotificationsActionHandler;
import com.wira.pmgt.server.actionhandlers.GetProcessRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetProcessStatusRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetProcessesRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetSettingsRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetTaskCompletionDataActionHandler;
import com.wira.pmgt.server.actionhandlers.GetTaskListActionHandler;
import com.wira.pmgt.server.actionhandlers.GetUserRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.GetUsersRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.LoginRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.LogoutActionHandler;
import com.wira.pmgt.server.actionhandlers.ManageKnowledgeBaseResponseHandler;
import com.wira.pmgt.server.actionhandlers.MultiRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.SaveCommentRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.SaveDSConfigRequestHandler;
import com.wira.pmgt.server.actionhandlers.SaveGroupRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.SaveNotificationRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.SaveProcessRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.SaveSettingsRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.SaveUserRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.SearchDocumentRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.StartAllProcessesRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.UpdateNotificationRequestActionHandler;
import com.wira.pmgt.server.actionhandlers.UpdatePasswordRequestActionHandler;
import com.wira.pmgt.server.actionvalidator.SessionValidator;
import com.wira.pmgt.shared.requests.ApprovalRequest;
import com.wira.pmgt.shared.requests.CheckPasswordRequest;
import com.wira.pmgt.shared.requests.CreateDocumentRequest;
import com.wira.pmgt.shared.requests.CreateFieldRequest;
import com.wira.pmgt.shared.requests.CreateFormRequest;
import com.wira.pmgt.shared.requests.DeleteAttachmentRequest;
import com.wira.pmgt.shared.requests.DeleteDSConfigurationEvent;
import com.wira.pmgt.shared.requests.DeleteDocumentRequest;
import com.wira.pmgt.shared.requests.DeleteFormModelRequest;
import com.wira.pmgt.shared.requests.DeleteLineRequest;
import com.wira.pmgt.shared.requests.DeleteProcessRequest;
import com.wira.pmgt.shared.requests.ExecuteWorkflow;
import com.wira.pmgt.shared.requests.ExportFormRequest;
import com.wira.pmgt.shared.requests.GetActivitiesRequest;
import com.wira.pmgt.shared.requests.GetAlertCount;
import com.wira.pmgt.shared.requests.GetAttachmentsRequest;
import com.wira.pmgt.shared.requests.GetCommentsRequest;
import com.wira.pmgt.shared.requests.GetContextRequest;
import com.wira.pmgt.shared.requests.GetDSConfigurationsRequest;
import com.wira.pmgt.shared.requests.GetDSStatusRequest;
import com.wira.pmgt.shared.requests.GetDashBoardDataRequest;
import com.wira.pmgt.shared.requests.GetDocumentRequest;
import com.wira.pmgt.shared.requests.GetDocumentTypesRequest;
import com.wira.pmgt.shared.requests.GetErrorRequest;
import com.wira.pmgt.shared.requests.GetFormModelRequest;
import com.wira.pmgt.shared.requests.GetFormsRequest;
import com.wira.pmgt.shared.requests.GetGroupsRequest;
import com.wira.pmgt.shared.requests.GetItemRequest;
import com.wira.pmgt.shared.requests.GetLongTasksRequest;
import com.wira.pmgt.shared.requests.GetNotificationsAction;
import com.wira.pmgt.shared.requests.GetProcessRequest;
import com.wira.pmgt.shared.requests.GetProcessStatusRequest;
import com.wira.pmgt.shared.requests.GetProcessesRequest;
import com.wira.pmgt.shared.requests.GetSettingsRequest;
import com.wira.pmgt.shared.requests.GetTaskCompletionRequest;
import com.wira.pmgt.shared.requests.GetTaskList;
import com.wira.pmgt.shared.requests.GetUserRequest;
import com.wira.pmgt.shared.requests.GetUsersRequest;
import com.wira.pmgt.shared.requests.LoginRequest;
import com.wira.pmgt.shared.requests.LogoutAction;
import com.wira.pmgt.shared.requests.ManageKnowledgeBaseRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.requests.SaveCommentRequest;
import com.wira.pmgt.shared.requests.SaveDSConfigRequest;
import com.wira.pmgt.shared.requests.SaveGroupRequest;
import com.wira.pmgt.shared.requests.SaveNotificationRequest;
import com.wira.pmgt.shared.requests.SaveProcessRequest;
import com.wira.pmgt.shared.requests.SaveSettingsRequest;
import com.wira.pmgt.shared.requests.SaveUserRequest;
import com.wira.pmgt.shared.requests.SearchDocumentRequest;
import com.wira.pmgt.shared.requests.StartAllProcessesRequest;
import com.wira.pmgt.shared.requests.UpdateNotificationRequest;
import com.wira.pmgt.shared.requests.UpdatePasswordRequest;
import com.wira.pmgt.shared.requests.CreatePeriodRequest;
import com.wira.pmgt.server.actionhandlers.CreatePeriodRequestHandler;
import com.wira.pmgt.shared.requests.GetPeriodsRequest;
import com.wira.pmgt.server.actionhandlers.GetPeriodsRequestHandler;
import com.wira.pmgt.shared.requests.CreateProgramRequest;
import com.wira.pmgt.server.actionhandlers.CreateProgramRequestHandler;
import com.wira.pmgt.shared.requests.GetProgramsRequest;
import com.wira.pmgt.server.actionhandlers.GetProgramsRequestHandler;
import com.wira.pmgt.shared.requests.GetFundsRequest;
import com.wira.pmgt.server.actionhandlers.GetFundsRequestHandler;
import com.wira.pmgt.shared.requests.GetPeriodRequest;
import com.wira.pmgt.server.actionhandlers.GetPeriodRequestHandler;
import com.wira.pmgt.shared.requests.AssignTaskRequest;
import com.wira.pmgt.server.actionhandlers.AssignTaskRequestHandler;
import com.wira.pmgt.shared.requests.CreateActivityFormRequest;
import com.wira.pmgt.server.actionhandlers.CreateActivityFormRequestHandler;
import com.wira.pmgt.shared.requests.GetProcessInfoRequest;
import com.wira.pmgt.server.actionhandlers.GetProcessInfoRequestHandler;
import com.wira.pmgt.shared.requests.GetTaskInfoRequest;
import com.wira.pmgt.server.actionhandlers.GetTaskInfoRequestHandler;



public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {

		bindHandler(ApprovalRequest.class, ApprovalRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetTaskList.class, GetTaskListActionHandler.class, SessionValidator.class);
		
		bindConstant().annotatedWith(SecurityCookie.class).to(ServerConstants.AUTHENTICATIONCOOKIE);

		bindHandler(ExecuteWorkflow.class, ExecuteWorkflowActionHandler.class, SessionValidator.class);

		bindHandler(GetItemRequest.class, GetItemActionHandler.class, SessionValidator.class);

		bindHandler(CreateDocumentRequest.class,
				CreateDocumentActionHandler.class, SessionValidator.class);

		bindHandler(GetDocumentRequest.class,
				GetDocumentRequestHandler.class, SessionValidator.class);

		bindHandler(LoginRequest.class, LoginRequestActionHandler.class);

		bindHandler(GetContextRequest.class,
				GetContextRequestActionHandler.class, SessionValidator.class);

		bindHandler(LogoutAction.class, LogoutActionHandler.class, SessionValidator.class);

		bindHandler(GetAlertCount.class, GetAlertCountActionHandler.class,
				SessionValidator.class);

		bindHandler(GetErrorRequest.class, GetErrorRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetNotificationsAction.class,
				GetNotificationsActionHandler.class,
				SessionValidator.class);

		bindHandler(SearchDocumentRequest.class,
				SearchDocumentRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(UpdateNotificationRequest.class,
				UpdateNotificationRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(SaveCommentRequest.class,
				SaveCommentRequestActionHandler.class, SessionValidator.class);

		bindHandler(MultiRequestAction.class,
				MultiRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetCommentsRequest.class,
				GetCommentsRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetProcessStatusRequest.class,
				GetProcessStatusRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetAttachmentsRequest.class,
				GetAttachmentsRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetActivitiesRequest.class,
				GetActivitiesRequestHandler.class, SessionValidator.class);

		bindHandler(SaveProcessRequest.class,
				SaveProcessRequestActionHandler.class, SessionValidator.class);

		bindHandler(DeleteProcessRequest.class,
				DeleteProcessRequestHandler.class, SessionValidator.class);

		bindHandler(GetProcessesRequest.class,
				GetProcessesRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetProcessRequest.class,
				GetProcessRequestActionHandler.class, SessionValidator.class);

		bindHandler(ManageKnowledgeBaseRequest.class,
				ManageKnowledgeBaseResponseHandler.class,
				SessionValidator.class);

		bindHandler(SaveUserRequest.class, SaveUserRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(SaveGroupRequest.class,
				SaveGroupRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetUsersRequest.class, GetUsersRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetGroupsRequest.class,
				GetGroupsRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetFormModelRequest.class,
				GetFormModelRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetDocumentTypesRequest.class,
				GetDocumentTypesRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(CreateFormRequest.class,
				CreateFormRequestActionHandler.class, SessionValidator.class);

		bindHandler(CreateFieldRequest.class,
				CreateFieldRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetFormsRequest.class, GetFormsRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(DeleteFormModelRequest.class,
				DeleteFormModelRequestHandler.class,
				SessionValidator.class);

		bindHandler(DeleteLineRequest.class,
				DeleteLineRequestActionHandler.class, SessionValidator.class);

		bindHandler(SaveNotificationRequest.class,
				SaveNotificationRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(DeleteAttachmentRequest.class,
				DeleteAttachmentRequestHandler.class,
				SessionValidator.class);

		bindHandler(StartAllProcessesRequest.class,
				StartAllProcessesRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(DeleteDocumentRequest.class,
				DeleteDocumentRequestHandler.class,
				SessionValidator.class);

		bindHandler(SaveDSConfigRequest.class,
				SaveDSConfigRequestHandler.class, SessionValidator.class);

		bindHandler(GetDSConfigurationsRequest.class,
				GetDSConfigurationsRequestHandler.class,
				SessionValidator.class);

		bindHandler(DeleteDSConfigurationEvent.class,
				DeleteDSConfigurationEventActionHandler.class,
				SessionValidator.class);

		bindHandler(ExportFormRequest.class,
				ExportFormRequestHandler.class, SessionValidator.class);


		bindHandler(GetDSStatusRequest.class,
				GetDSStatusRequestActionHandler.class, SessionValidator.class);

		bindHandler(CheckPasswordRequest.class,
				CheckPasswordRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetUserRequest.class, GetUserRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(UpdatePasswordRequest.class,
				UpdatePasswordRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetDashBoardDataRequest.class,
				GetDashBoardDataRequestHandler.class,
				SessionValidator.class);

		bindHandler(GetLongTasksRequest.class,
				GetLongTasksRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetTaskCompletionRequest.class,
				GetTaskCompletionDataActionHandler.class,
				SessionValidator.class);

		bindHandler(GetSettingsRequest.class,
				GetSettingsRequestActionHandler.class, SessionValidator.class);

		bindHandler(SaveSettingsRequest.class,
				SaveSettingsRequestActionHandler.class, SessionValidator.class);

		bindHandler(CreatePeriodRequest.class,
				CreatePeriodRequestHandler.class, SessionValidator.class);

		bindHandler(GetPeriodsRequest.class,
				GetPeriodsRequestHandler.class, SessionValidator.class);

		bindHandler(CreateProgramRequest.class,
				CreateProgramRequestHandler.class, SessionValidator.class);

		bindHandler(GetProgramsRequest.class,
				GetProgramsRequestHandler.class, SessionValidator.class);

		bindHandler(GetFundsRequest.class, GetFundsRequestHandler.class,
				SessionValidator.class);

		bindHandler(GetPeriodRequest.class,
				GetPeriodRequestHandler.class, SessionValidator.class);

		bindHandler(AssignTaskRequest.class,
				AssignTaskRequestHandler.class, SessionValidator.class);

		bindHandler(CreateActivityFormRequest.class,
				CreateActivityFormRequestHandler.class,
				SessionValidator.class);

		bindHandler(GetProcessInfoRequest.class,
				GetProcessInfoRequestHandler.class, SessionValidator.class);

		bindHandler(GetTaskInfoRequest.class,
				GetTaskInfoRequestHandler.class, SessionValidator.class);
	}
}
