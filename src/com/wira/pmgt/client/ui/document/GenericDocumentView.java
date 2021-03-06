package com.wira.pmgt.client.ui.document;

import static com.wira.pmgt.client.ui.document.GenericDocumentPresenter.*;
import static com.wira.pmgt.client.ui.util.DateUtils.DATEFORMAT;
import static com.wira.pmgt.client.ui.util.DateUtils.TIMEFORMAT12HR;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.pmgt.client.ui.component.CommentBox;
import com.wira.pmgt.client.ui.document.form.FormPanel;
import com.wira.pmgt.client.ui.upload.custom.Uploader;
import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.client.ui.wfstatus.ProcessState;
import com.wira.pmgt.shared.model.Actions;
import com.wira.pmgt.shared.model.Delegate;
import com.wira.pmgt.shared.model.DocStatus;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.NodeDetail;
import com.wira.pmgt.shared.model.Priority;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.Form;

public class GenericDocumentView extends ViewImpl implements
		GenericDocumentPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, GenericDocumentView> {
	}

	@UiField
	SpanElement spnCreated;
	@UiField
	SpanElement spnDeadline;
	@UiField
	SpanElement spnDocType;
	@UiField
	SpanElement spnSubject;

	@UiField
	SpanElement spnDate;

	@UiField
	SpanElement spnValue;
	@UiField
	SpanElement spnPartner;
	@UiField
	SpanElement spnDescription;

	@UiField
	Image img;

	@UiField
	Anchor aSimulate;
	@UiField
	Anchor aEdit;
	@UiField
	Anchor aSave;
	@UiField
	Anchor aDelete;
	@UiField
	Anchor aClaim;
	@UiField
	Anchor aStart;
	@UiField
	Anchor aSuspend;
	@UiField
	Anchor aResume;
	@UiField
	Anchor aComplete;
	@UiField
	Anchor aDelegate;
	@UiField
	Anchor aRevoke;
	@UiField
	Anchor aStop;
	@UiField
	Anchor aForward;
	@UiField
	Anchor aProcess;
	@UiField
	Anchor aApprove;
	@UiField
	Anchor aReject;
	@UiField
	HTMLPanel statusContainer;
	@UiField
	HTMLPanel divProcess;
	@UiField
	Element eOwner;
	@UiField
	Element eTitle;
	// @UiField Element eDelegate;
	@UiField
	HTMLPanel spnPriority;
	@UiField
	SpanElement spnAttachmentNo;
	@UiField
	SpanElement spnActivityNo;
	@UiField
	DivElement divAttachment;
	@UiField
	SpanElement spnStatusBody;
	@UiField
	HTMLPanel panelActivity;
	@UiField
	Uploader uploader;
	@UiField
	HTMLPanel panelAttachments;
	// @UiField Anchor aAttach1;
	@UiField
	Anchor aAttach2;
	@UiField
	Anchor aShowProcess;
	@UiField
	CommentBox commentPanel;
	@UiField
	HTMLPanel commentBox;

	@UiField
	DivElement btnGroup;
	@UiField
	DivElement divDate;
	@UiField
	DivElement divDesc;
	@UiField
	DivElement divPartner;
	@UiField
	DivElement divValue;
	@UiField
	DivElement divContent;

	@UiField
	HTMLPanel fldForm;
	@UiField
	HTMLPanel divContainer;
	
	@UiField
	HTMLPanel divNocomments;

	@UiField
	HTMLPanel divBody;
	@UiField
	HTMLPanel divUserInfo;
	@UiField
	HTMLPanel divUpperContent;

	FormPanel formPanel;
	String url = null;

	List<Actions> validActions = null;
	boolean isBizProcessDisplayed=true;
	boolean overrideDefaultComplete=false;
	boolean overrideDefaultStart=false;
	private String timeDiff;

	@Inject
	public GenericDocumentView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		UIObject.setVisible(aEdit.getElement(), false);
		aEdit.getElement().setAttribute("type", "button");
		aEdit.getElement().setAttribute("data-toggle", "tooltip");
		aProcess.getElement().setAttribute("data-toggle", "button");
		aSimulate.getElement().setAttribute("type", "button");
		UIObject.setVisible(aForward.getElement(), false);
		aApprove.getElement().setAttribute("type", "button");
		aReject.getElement().setAttribute("type", "button");
		aForward.getElement().setAttribute("type", "button");
		aForward.getElement().setAttribute("alt", "Forward for Approval");
		aShowProcess.setVisible(false);

		img.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});

		showDefaultFields(false);
		disableAll();// Hide all buttons

		showOneButton();

		aShowProcess.removeStyleName("gwt-Anchor");

		aShowProcess.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (url != null)
					Window.open(url, "Business Process", null);
			}
		});

		aEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				formPanel.setReadOnly(false);
				UIObject.setVisible(btnGroup, false);
				UIObject.setVisible(aForward.getElement(), false);
				UIObject.setVisible(aEdit.getElement(), false);
				UIObject.setVisible(aSave.getElement(), true);

			}
		});

		aProcess.addClickHandler(new ClickHandler() {
			private boolean isClicked = false;

			@Override
			public void onClick(ClickEvent event) {
				if (isClicked) {
					aProcess.addStyleName("disabled");
					divProcess.removeStyleName("hide");
					divContent.removeClassName("span12");
					divContent.addClassName("span9");
					isClicked = false;
				} else {
					aProcess.removeStyleName("disabled");
					divProcess.addStyleName("hide");
					divContent.removeClassName("span9");
					divContent.addClassName("span12");
					isClicked = true;
				}
			}
		});

		aSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (isValid()) {
					UIObject.setVisible(btnGroup, true);
					UIObject.setVisible(aForward.getElement(), true);
					UIObject.setVisible(aEdit.getElement(), true);
					UIObject.setVisible(aSave.getElement(), false);
				}
			}
		});

		UIObject.setVisible(aSave.getElement(), false);
		statusContainer.add(new InlineLabel("Nothing to show"));
	}

	private void showOneButton() {

	}

	private void disableAll() {
		show(aClaim, false);
		show(aStart, false);
		show(aSuspend, false);
		show(aResume, false);
		show(aComplete, false);
		show(aDelegate, false);
		show(aReject, false);
		show(aRevoke, false);
		show(aStop, false);
		show(aForward, false);
		show(aApprove, false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setForm(Form form) {
		fldForm.clear();

		if (form == null || form.getFields() == null)
			return;

		// hide static fields
		showDefaultFields(false);

		formPanel = new FormPanel(form);

		if (validActions != null) {
			if (validActions.contains(Actions.COMPLETE)) {
				formPanel.setReadOnly(false);
			} else {
				formPanel.setReadOnly(true);
			}
		}
		fldForm.add(formPanel);
	}

	public void showDefaultFields(boolean show) {
		UIObject.setVisible(divDate, show);
		UIObject.setVisible(divDesc, show);
		UIObject.setVisible(divPartner, show);
		UIObject.setVisible(divValue, show);
	}

	public void setValues(HTUser createdBy, Date created, String type,
			String subject, Date docDate, String value, String partner,
			String description, Integer priority, DocStatus status, Long id,
			String taskDisplayName) {
		disableAll();
		if (createdBy != null) {
			if (createdBy.getSurname() != null)
				eOwner.setInnerText(createdBy.getSurname());
			else
				eOwner.setInnerText(createdBy.getUserId());

			setImage(createdBy);
		}

		if (created != null)
			timeDiff = DateUtils.getTimeDifferenceAsString(created);
		if (timeDiff != null)
			spnCreated.setInnerText(TIMEFORMAT12HR.format(created) + " ("
					+ timeDiff + " )");

		if (!(taskDisplayName == null || taskDisplayName.equals(""))) {
			spnDocType.setInnerText(taskDisplayName);
		} else if (type != null) {
			spnDocType.setInnerText(type);
		}

		if (subject != null) {
			spnSubject.setInnerText(subject);
		}

		if (docDate != null) {
			spnDate.setInnerText(DATEFORMAT.format(docDate));
		}

		if (value != null) {
			spnValue.setInnerText(value);
			// UIObject.setVisible(divValue, true);
		}

		if (partner != null) {
			// UIObject.setVisible(divPartner, true);
			spnPartner.setInnerText(partner);
		}

		if (description != null)
			spnDescription.setInnerText(description);

		if (status == DocStatus.DRAFTED) {
			showForward(true);
		} else {
			showForward(false);
		}

		if (status != null) {
			spnStatusBody.setInnerText(status.name());

			if (status == DocStatus.APPROVED) {
				spnStatusBody.setClassName("label label-success");
			}

			if (status == DocStatus.INPROGRESS) {
				spnStatusBody.setClassName("label label-info");
			}

			if (status == DocStatus.REJECTED) {
				spnStatusBody.setClassName("label label-danger");
			}

		}

		if (priority != null) {
			Priority prty = Priority.get(priority);

			switch (prty) {
			case CRITICAL:
				spnPriority.addStyleName("label-important");
				// spnPriority.setInnerText("Urgent");
				break;

			case HIGH:
				spnPriority.addStyleName("label-warning"); //
				// spnPriority.setInnerText("Important");
				break;

			default:
				spnPriority.addStyleName("hide");
				break;
			}
		}

		this.url = null;
		if (id != null) {
			String root = GWT.getModuleBaseURL();
			root = root.replaceAll("/gwtht", "");
			this.url = root + "getreport?did=" + id
					+ "&ACTION=GETDOCUMENTPROCESS";
			aShowProcess.setVisible(true);
		}

	}

	public void setValidTaskActions(List<Actions> actions) {
		this.validActions = actions;
		if(actions!=null)
		for(Actions action : actions){		
			Anchor target=null;
			switch(action){
			case CLAIM:
				target=aClaim;
				break;
			case COMPLETE:
				//target=aComplete;
				if(!overrideDefaultComplete){
					show(aApprove);
					show(aReject);
				}
				break;
			case DELEGATE:
				target=aDelegate;
				break;
			case FORWARD:
				if(!overrideDefaultStart){
					target=aForward;
				}
				
				break;
			case RESUME:
				target=aResume;
				break;
			case REVOKE:
				target=aRevoke;
				break;
			case START:
				target=aStart;
				break;
			case STOP:
				target=aStop;
				break;
			case SUSPEND:
				target=aSuspend;
				break;
			}
			
			if(target!=null){
				show(target);
			}
		}

	}

	@Override
	public void showForward(boolean show) {
		show(aForward, show);
	}

	@Override
	public void show(boolean IsShowapprovalLink, boolean IsShowRejectLink) {

		show(aApprove, IsShowapprovalLink);
		show(aReject, IsShowRejectLink);
	}

	public HasClickHandlers getSimulationBtn() {
		return aSimulate;
	}

	public HasClickHandlers getSaveButton() {
		return aSave;
	}

	public HasClickHandlers getDeleteButton() {
		return aDelete;
	}

	@Override
	public void showEdit(boolean displayed) {
		UIObject.setVisible(aEdit.getElement(), displayed);
		UIObject.setVisible(aDelete.getElement(), displayed);
	}

	@Override
	public void setStates(List<NodeDetail> states) {
		statusContainer.clear();
		if (states != null) {
			NodeDetail detail = null;
			for (NodeDetail state : states) {
				if (state.isEndNode())
					detail = state;
				else
					statusContainer.add(new ProcessState(state));

			}

			// ensure end node always comes last
			if (detail != null) {
				statusContainer.add(new ProcessState(detail));
			}
		}
	}

	public void show(Anchor target) {
		show(target, true);
	}

	public void show(Anchor target, boolean isvisible) {
		if (isvisible) {
			target.removeStyleName("hide");
		}
		UIObject.setVisible(target.getElement(), isvisible);

	}

	public HasClickHandlers getClaimLink() {
		return aClaim;
	}

	public HasClickHandlers getStartLink() {
		return aStart;
	}

	public HasClickHandlers getSuspendLink() {
		return aSuspend;
	}

	public HasClickHandlers getResumeLink() {
		return aResume;
	}

	public HasClickHandlers getCompleteLink() {
		return aComplete;
	}

	public HasClickHandlers getDelegateLink() {
		return aDelegate;
	}

	public HasClickHandlers getRevokeLink() {
		return aRevoke;
	}

	public HasClickHandlers getStopLink() {
		return aStop;
	}

	public HasClickHandlers getForwardForApproval() {
		return aForward;
	}

	public HasClickHandlers getApproveLink() {
		return aApprove;
	}

	public HasClickHandlers getRejectLink() {
		return aReject;
	}

	public HasValueChangeHandlers<String> getCommentBox(){
		return commentPanel;
	}

	@Override
	public void setInSlot(Object slot, Widget content) {

		if (slot == ACTIVITY_SLOT) {
			panelActivity.clear();
			if (content != null) {
				panelActivity.add(content);
			}
		}
		if (slot == BODY_SLOT) {
			divBody.clear();
			if (content != null) {
				divBody.add(content);
			}

		}
		if (slot == ATTACHMENTS_SLOT) {
			panelAttachments.clear();
			if (content != null) {
				panelAttachments.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}
	}

	public void hideTopHeader(boolean show) {
		if (show) {
			divUpperContent.addStyleName("hide");
			divUserInfo.addStyleName("hide");
		} else {
			divUpperContent.removeStyleName("hide");
			divUserInfo.removeStyleName("hide");
		}

	}

	@Override
	public void addToSlot(Object slot, Widget content) {

		if (slot == ACTIVITY_SLOT) {

			if (content != null) {
				panelActivity.add(content);
			}
		}
		if (slot == ATTACHMENTS_SLOT) {
			if (content != null) {
				panelAttachments.add(content);
			}
		} else {
			super.addToSlot(slot, content);
		}

	}

	@Override
	public Uploader getUploader() {
		return uploader;
	}

	@Override
	public void setComment(String value) {
		commentPanel.setValue(value);
	}

	public HasClickHandlers getUploadLink2() {
		return aAttach2;
	}

	public SpanElement getSpnAttachmentNo() {
		return spnAttachmentNo;
	}

	public SpanElement getSpnActivityNo() {
		return spnActivityNo;
	}

	public DivElement getDivAttachment() {
		return divAttachment;
	}

	@Override
	public boolean isValid() {
		if (formPanel == null) {
			return true;
		}
		return formPanel.isValid();
	}

	@Override
	public Map<String, Value> getValues() {
		if (formPanel == null) {
			return null;
		}

		return formPanel.getValues();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		delegate.getCreated();
		delegate.getDelegateTo();
		delegate.getUserId();
		// eDelegate.setInnerText(delegate.getDelegateTo());
	}

	public void setDeadline(Date endDateDue) {

		String deadline = "";
		timeDiff = DateUtils.getTimeDifferenceAsString(endDateDue);
		if (timeDiff != null) {
			deadline = TIMEFORMAT12HR.format(endDateDue) + " (" + timeDiff
					+ " )";
		}

		if (DateUtils.isOverdue(endDateDue)) {
			spnDeadline.removeClassName("hide");
			spnDeadline.getStyle().setColor("#DD4B39");
			deadline = "Overdue " + deadline;
		} else if (DateUtils.isDueInMins(30, endDateDue)) {
			spnDeadline.removeClassName("hide");
			spnDeadline.getStyle().setColor("#F89406");
			deadline = "Due " + deadline;
		}

		spnDeadline.setInnerText(deadline);
	}

	private void setImage(HTUser user) {
		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if (moduleUrl.endsWith("/")) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.length() - 1);
		}
		moduleUrl = moduleUrl + "/getreport?ACTION=GetUser&userId="
				+ user.getUserId();
		img.setUrl(moduleUrl);
	}
	
	@Override
	public void overrideDefaultCompleteProcess() {
		overrideDefaultComplete=true;
		aApprove.addStyleName("hide");
		aReject.addStyleName("hide");
	}

	@Override
	public void overrideDefaultStartProcess() {
		overrideDefaultStart=true;
		aForward.addStyleName("hide");
	}

	@Override
	public void showCommentsPanel(boolean show) {
		if(show){
			divNocomments.addStyleName("hide");
			commentPanel.removeStyleName("hide"); 
		}else{
			divNocomments.removeStyleName("hide");
			commentPanel.addStyleName("hide"); 
		}
	}
	
	@Override
	public void showAttachmentPanel(boolean show)
	{
		if(show){
			divAttachment.addClassName("hide");
		}else{
			divAttachment.removeClassName("hide");
		}
	}


}
