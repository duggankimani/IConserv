package com.wira.pmgt.client.ui.assign;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.wira.pmgt.client.ui.component.Dropdown;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.OrgEntity;
import com.wira.pmgt.shared.model.PermissionType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.UserGroup;

public class TaskAllocation extends Composite {

	private static TaskAllocationUiBinder uiBinder = GWT
			.create(TaskAllocationUiBinder.class);

	interface TaskAllocationUiBinder extends UiBinder<Widget, TaskAllocation> {
	}
	
	OrgEntity entity;
	
	@UiField Image divImage;
	@UiField Dropdown<PermissionType> participantDropdown;
	@UiField Anchor aParticipant;
	@UiField SpanElement spnName;
	@UiField SpanElement spnEmail;
	@UiField SpanElement spnText;
	@UiField Anchor aRemove;
	PermissionType type;
	
	private TaskAllocation(){
		initWidget(uiBinder.createAndBindUi(this));
		participantDropdown.addValueChangeHandler(new ValueChangeHandler<PermissionType>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PermissionType> event) {
				setParticipantType(event.getValue());
			}
		});
		
		divImage.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				divImage.setUrl("img/blueman(small).png");
			}
		});
	}
	public TaskAllocation(ProgramDetailType detailType,OrgEntity entity, PermissionType type){
		this(detailType, entity,type, true);
	}
	
	public TaskAllocation(ProgramDetailType detailType,OrgEntity entity, PermissionType type, boolean isEditable) {
		this();
		participantDropdown.setValues(PermissionType.getTypes(detailType));
		setEntity(entity);
		setParticipantType(type);
		participantDropdown.setEditable(isEditable);
	}
	
	private void setParticipantType(PermissionType type) {
		if(type!=null){
			this.type=type;
			spnText.setInnerText(type.getDisplayName());
		}
	}
	
	private void setEntity(OrgEntity entity) {
		this.entity = entity;
		if(entity==null){
			return;
		}
		
		if(entity instanceof HTUser){
			HTUser user = (HTUser) entity;
			divImage.setUrl(AppContext.getUserImageUrl(user, 50, 50));
			spnName.setInnerText(user.getFullName());
			spnEmail.setInnerText(user.getEmail());
		}
		
		if(entity instanceof UserGroup){
			UserGroup group = (UserGroup) entity;
			divImage.setUrl("img/blueman(small).png");
			spnName.setInnerText(group.getFullName());
		}
	}
	
	
	public HasClickHandlers getRemoveLink() {
		return aRemove;
	}

	public OrgEntity getOrgEntity() {
		// TODO Auto-generated method stub
		return entity;
	}
	
	public PermissionType getParticipantType(){
		return type;
	}

}
