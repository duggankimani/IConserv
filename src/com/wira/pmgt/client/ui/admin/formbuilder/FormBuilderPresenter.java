package com.wira.pmgt.client.ui.admin.formbuilder;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.pmgt.client.model.UploadContext;
import com.wira.pmgt.client.service.TaskServiceCallback;
import com.wira.pmgt.client.ui.AppManager;
import com.wira.pmgt.client.ui.OnOptionSelected;
import com.wira.pmgt.client.ui.admin.formbuilder.upload.FormImportView;
import com.wira.pmgt.client.ui.events.ErrorEvent;
import com.wira.pmgt.client.ui.events.ProcessingCompletedEvent;
import com.wira.pmgt.client.ui.events.PropertyChangedEvent;
import com.wira.pmgt.client.ui.events.PropertyChangedEvent.PropertyChangedHandler;
import com.wira.pmgt.client.ui.events.SaveFormDesignEvent;
import com.wira.pmgt.client.ui.events.SaveFormDesignEvent.SaveFormDesignHandler;
import com.wira.pmgt.client.ui.events.SavePropertiesEvent;
import com.wira.pmgt.client.ui.events.SavePropertiesEvent.SavePropertiesHandler;
import com.wira.pmgt.client.util.AppContext;
import com.wira.pmgt.shared.model.StringValue;
import com.wira.pmgt.shared.model.form.Field;
import com.wira.pmgt.shared.model.form.Form;
import com.wira.pmgt.shared.model.form.Property;
import com.wira.pmgt.shared.requests.CreateFormRequest;
import com.wira.pmgt.shared.requests.DeleteFormModelRequest;
import com.wira.pmgt.shared.requests.ExportFormRequest;
import com.wira.pmgt.shared.requests.GetFormModelRequest;
import com.wira.pmgt.shared.requests.GetFormsRequest;
import com.wira.pmgt.shared.requests.MultiRequestAction;
import com.wira.pmgt.shared.responses.CreateFormResponse;
import com.wira.pmgt.shared.responses.ExportFormResponse;
import com.wira.pmgt.shared.responses.GetFormModelResponse;
import com.wira.pmgt.shared.responses.GetFormsResponse;
import com.wira.pmgt.shared.responses.MultiRequestActionResult;

public class FormBuilderPresenter extends
		PresenterWidget<FormBuilderPresenter.IFormBuilderView> implements
		SavePropertiesHandler, PropertyChangedHandler, SaveFormDesignHandler {

	public interface IFormBuilderView extends View {
		HasClickHandlers getNewButton();
		HasClickHandlers getDeleteButton();
		HasClickHandlers getCloneButton();
		HasClickHandlers getExportButton();
		HasClickHandlers getImportButton();
		InlineLabel getFormLabel();
		Form getForm();
		HasValueChangeHandlers<Form> getFormDropDown();
		void setForm(Form form);
		void setProperty(String property, String value);
		void setForms(List<Form> forms);
		void activatePalette();
		void registerInputDrag();
		void clear();
		String getFormName();
		boolean isTaskForm();
	}

	@Inject
	DispatchAsync dispatcher;

	Long formId=null;

	private List<Form> forms;
	
	@Inject
	public FormBuilderPresenter(final EventBus eventBus,
			final IFormBuilderView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(SavePropertiesEvent.TYPE, this);
		addRegisteredHandler(PropertyChangedEvent.TYPE, this);
		addRegisteredHandler(SaveFormDesignEvent.TYPE, this);
		getView().getFormDropDown().addValueChangeHandler(
				new ValueChangeHandler<Form>() {

					@Override
					public void onValueChange(ValueChangeEvent<Form> event) {
						getView().registerInputDrag();
						getView().activatePalette();

						Form form = event.getValue();

						if (form.getId() == null) {
							return;
						}
						
						History.newItem("adminhome;page=formbuilder;formid="+form.getId(), false);
						loadForm(form.getId());						
					}
				});

		getView().getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getView().clear();
				getView().registerInputDrag();
				getView().activatePalette();

				Form form = getView().getForm();
				form.setCaption(null);
				form.setName(null);
				form.setId(null);
				saveForm(form);
			}
		});

		getView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final Form form = getView().getForm();
				AppManager.showPopUp("Confirm Delete", new HTMLPanel(
						"Do you want to delete form \"" + form.getCaption()+"\""),
						new OnOptionSelected() {

							@Override
							public void onSelect(String button) {
								if (button.equals("Ok")) {
									delete(form);
								}
							}

						}, "Cancel", "Ok");
			}
		});
		
		getView().getCloneButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				final Form form = getView().getForm();				
				Form formClone = form.clone();
				
				if(getView().isTaskForm()){
					
					List<Property> properties  = form.getProperties();
					List<Property> cloneProps = new ArrayList<Property>();
					if(properties!=null){
						for(Property prop: properties){
							Property clone = prop.clone(false);
							if(prop.getName()!=null && prop.getName().equals("NAME")){
								//name property
								if(prop.getValue()!=null && prop.getValue().getValue()!=null){
									String approvalName= prop.getValue().getValue().toString()+"-Approval";
									clone.setValue(new StringValue(approvalName));
									formClone.setName(approvalName);
								}
							}else if(prop.getName()!=null && prop.getName().equals("CAPTION")){
								if(prop.getValue()!=null && prop.getValue().getValue()!=null){
									String approvalCaption = "Approval Form "
								+(prop.getValue().getValue().toString()).replace("Task Form", "");
									clone.setValue(new StringValue(approvalCaption));
									formClone.setCaption(approvalCaption);
								}
							}
							cloneProps.add(clone);
						}
						
						if(forms!=null){
							for(Form aform: forms){
								if(aform.getName().equals(formClone.getName())){
									fireEvent(new ErrorEvent("Form '"+formClone.getName()+"' already exists."
											+ "If you are generating an approval form, please check "
											+ "that it does not exist. Delete any previously generated "
											+ "approval form and try again. "
											+ "Hint- Go back to task assignment to see the forms already generated."
											+ "", 0L));
									return;
								}
							
							}
						}
					}
					
					formClone.setProperties(cloneProps);
					Field submit = null;
					for(Field field: formClone.getFields()){
						if(field.getName()!=null && field.getName().equals("submit")){
							submit = field;
							break;
						}
					}
					
					if(submit!=null){
						formClone.getFields().remove(submit);
					}
				}
								
				//System.err.println(">>>>> :: "+clone+" \n["+clone.getFields()+"] \n"+clone.getProperties());
				
				saveForm(formClone);
			}
		});
		
		getView().getImportButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final FormImportView view = new FormImportView();
				view.setAvoidRepeatFiles(false);
				AppManager.showPopUp("Import Form", view, 
						new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Save")){
									loadForms();
								}else{
									view.cancelImport();
								}
							}
						}, "Save", "Cancel");
			}
		});
		
		getView().getExportButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				assert formId!=null;
				dispatcher.execute(new ExportFormRequest(formId), 
						new TaskServiceCallback<ExportFormResponse>() {
							@Override
							public void processResult(
									ExportFormResponse aResponse) {
								
								String xml = aResponse.getXml();
								
								InlineLabel area = new InlineLabel(xml);
								
								AppManager.showPopUp("Export '"+getView().getFormName()+"'", 
										area,
										new OnOptionSelected() {
											
											@Override
											public void onSelect(String name) {
												if(name.equals("Save To File")){
													UploadContext context = new UploadContext("getreport");
													context.setContext("formId", formId+"");
													context.setContext("ACTION", "EXPORTFORM");
													String url = context.toUrl();
													
													String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
													if(moduleUrl.endsWith("/")){
														moduleUrl = moduleUrl.substring(0, moduleUrl.length()-1);
													}
													url = url.replace("/", "");
													moduleUrl =moduleUrl+"/"+url;
													Window.open(moduleUrl, getView().getFormName(), "");
												}
											}
										}, "Save To File", "Done");
							}
						});
				
			}
		});
	}

	private void delete(Form form) {
		MultiRequestAction action = new MultiRequestAction();
		DeleteFormModelRequest request = new DeleteFormModelRequest(form);
		action.addRequest(request);
		
		GetFormsRequest formsRequest = new GetFormsRequest();
		action.addRequest(formsRequest);
		
		dispatcher.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult result) {
						getView().clear();
						getView().setForm(new Form());
						
						GetFormsResponse resp = (GetFormsResponse)result.get(1);
						setForms(resp.getForms());
					}
				});
	}

	protected void loadForm(Long id) {
		GetFormModelRequest request = new GetFormModelRequest(Form.FORMMODEL,
				id, true);
		setFormId(id);
		dispatcher.execute(request,
				new TaskServiceCallback<GetFormModelResponse>() {
					@Override
					public void processResult(GetFormModelResponse result) {
						Form form = (Form) result.getFormModel().get(0);
						getView().setForm(form);
					}
				});
	}

	protected void saveForm(Form form) {
		this.formId=null;
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new CreateFormRequest(form));
		
		GetFormsRequest formsRequest = new GetFormsRequest();
		action.addRequest(formsRequest);
		dispatcher.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult result) {
						CreateFormResponse createResp = (CreateFormResponse)result.get(0);
						Form form=createResp.getForm();
						setFormId(form.getId());
						getView().setForm(form);
						
						GetFormsResponse response = (GetFormsResponse)result.get(1);
						setForms(response.getForms());
					}
				});
	}

	@Override
	protected void onReset() {
		super.onReset();
		loadForms();
	}
	
	private void loadForms() {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFormsRequest());
		
		if(formId!=null){
			GetFormModelRequest request = new GetFormModelRequest(Form.FORMMODEL,
					formId, true);
			action.addRequest(request);
		}
		
		dispatcher.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult results) {
				setForms(((GetFormsResponse)results.get(0)).getForms());
				
				if(results.getReponses().size()>1){
					GetFormModelResponse response = (GetFormModelResponse)results.get(1);
					Form form = (Form) response.getFormModel().get(0);
					getView().setForm(form);
				}
			}
		});
	}
	
	void setForms(List<Form> forms){
		this.forms = forms;
		getView().setForms(forms);
	}
	
	@Override
	public void onSaveProperties(SavePropertiesEvent event) {
		if (event.getParent() != null
				&& event.getParent().equals(getView().getForm())) {
			saveForm(getView().getForm());
		}
	}

	@Override
	public void onPropertyChanged(PropertyChangedEvent event) {
		if (event.isForField())
			return;

		String property = event.getPropertyName();
		String value = event.getPropertyValue().toString();

		getView().setProperty(property, value);
		//saveForm(getView().getForm());
	}

	@Override
	public void onSaveFormDesign(SaveFormDesignEvent event) {
		saveForm(getView().getForm());
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

}
