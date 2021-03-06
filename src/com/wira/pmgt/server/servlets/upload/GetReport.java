package com.wira.pmgt.server.servlets.upload;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wira.pmgt.server.dao.helper.FormDaoHelper;
import com.wira.pmgt.server.dao.model.ADDocType;
import com.wira.pmgt.server.dao.model.ADForm;
import com.wira.pmgt.server.dao.model.DocumentModel;
import com.wira.pmgt.server.dao.model.LocalAttachment;
import com.wira.pmgt.server.dao.model.ProcessDefModel;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.reports.GenerateActivityReport;
import com.wira.pmgt.shared.model.settings.SETTINGNAME;

import org.apache.log4j.Logger;

public class GetReport extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(GetReport.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			// check session

			DB.beginTransaction();

			executeGet(req, resp);

			DB.commitTransaction();
		} catch (Exception e) {
			DB.rollback();
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DB.closeSession();
		}

	}

	protected void executeGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("ACTION");
		log.debug("GetReport Action = "+action);

		if (action == null) {
			action = "none";
		}

		if(action.equals("GETDOCUMENTPROCESS")){
			String docId = req.getParameter("did");
			if(docId==null){
				throw new IllegalStateException("DocumentId must not be null for action "+action);
			}
			Long documentId = new Long(docId);
			
			DocumentModel model = DB.getDocumentDao().getById(documentId);
			assert model!=null;
			
			ADDocType type = model.getType();
			
			if(type==null){
				return ;
			}
			
			ProcessDefModel processDefnition = type.getProcessDef();
			
			if(processDefnition==null)
				return;
			
			assert processDefnition!=null;
			
			List<LocalAttachment> attachments = DB.getAttachmentDao().getAttachmentsForProcessDef(processDefnition, true);
			
			if(attachments.size()==0){
				return;
			}
			
			processAttachmentRequest(resp, attachments.get(0));
		}
		
		if (action.equals("GETATTACHMENT")) {
			processAttachmentRequest(req, resp);
		}
		
		if(action.equals("EXPORTFORM")){
			processExportFormRequest(req , resp);
		}
		
		if(action.equals("GetUser")){
			processUserImage(req, resp);
		}
		
		if(action.equals("GetLogo")){
			processSettingsImage(req, resp);
		}
		
		if(action.equals("EXPORTPROGRAMS")){
			processExportProgramRequest(req, resp);
		}

	}

	private void processExportProgramRequest(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		String param1 = req.getParameter("programid");
		String param2 = req.getParameter("activityid");
		String param3 = req.getParameter("outcomeid");
		String param4 = req.getParameter("periodid");
		String programType = req.getParameter("programType");
		String code = req.getParameter("code");
		
		Long programId=null,activityId=null,outcomeId  = null, periodid=null;
		
		if(param1!=null){
			programId = getValue(param1);
			//export all
		}
		
		if(param2!=null){
			activityId = getValue(param2);
			//export all
		}
		
		if(param3!=null){
			outcomeId = getValue(param3);
			//export all
		}
		
		if(param4!=null){
			periodid = getValue(param4);
		}
				
		GenerateActivityReport report = new GenerateActivityReport(programId,code, outcomeId, activityId,periodid,programType, "xlsx");
		processAttachmentRequest(resp,report.getBytes(), report.getName());
	}

	private Long getValue(String param) {
		try{
			return Long.parseLong(param);
		}catch(Exception e){}
		
		return null;
		
	}

	private void processSettingsImage(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		String settingName = req.getParameter("settingName");
		log.debug("Logging- SettingName "+settingName);
		assert settingName!=null;
		
		String widthPx = req.getParameter("width");
		String heightPx = req.getParameter("height");
		
		if(widthPx!=null && heightPx==null){
			heightPx=widthPx;
		}
		
		Double width=null;
		Double height=null;
		
		if(widthPx!=null && widthPx.matches("[0-9]+(\\.[0-9][0-9]?)?")){
			width = new Double(widthPx);
			height = new Double(heightPx);
		}
		
		LocalAttachment attachment = DB.getAttachmentDao().getSettingImage(SETTINGNAME.valueOf(settingName));
		
		if(attachment==null){
			log.debug("No Attachment Found for Setting: ["+settingName+"]");
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		log.debug("Attachment found for setting: ["+settingName+"], FileName = "+attachment.getName());
		
		byte[] bites = attachment.getAttachment();
		
		if(width!=null){
			ImageUtils.resizeImage(resp, bites, width.intValue(), height.intValue());
		}else{
			ImageUtils.resizeImage(resp, bites);
		}
	}

	private void processUserImage(HttpServletRequest req,
			HttpServletResponse resp) throws IOException{		
		String userId = req.getParameter("userId");
		assert userId!=null;
		
		String widthPx = req.getParameter("width");
		String heightPx = req.getParameter("height");
		
		if(widthPx!=null && heightPx==null){
			heightPx=widthPx;
		}
		
		Double width=null;
		Double height=null;
		
		if(widthPx!=null && widthPx.matches("[0-9]+(\\.[0-9][0-9]?)?")){
			width = new Double(widthPx);
			height = new Double(heightPx);
		}
		
		LocalAttachment attachment = DB.getAttachmentDao().getUserImage(userId);
		
		if(attachment==null){
			log.debug("No Image Found for user: ["+userId+"]");
			//Mark as not found
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		byte[] bites = attachment.getAttachment();
		
		if(width!=null && height!=null){
			ImageUtils.resizeImage(resp, bites, width.intValue(), height.intValue());
		}else if (height!=null){
			ImageUtils.resizeImage(resp, bites,height.intValue(), height.intValue());
		}else if(width!=null){
			ImageUtils.resizeImage(resp, bites,width.intValue(), width.intValue());
		}else{
			ImageUtils.resizeImage(resp, bites);
		}
		
	}

	private void processExportFormRequest(HttpServletRequest req,
			HttpServletResponse resp) {
		String param1 = req.getParameter("formId");
		assert param1!=null;
		
		Long formId  = Long.parseLong(param1);
		ADForm form = DB.getFormDao().getForm(formId);
		
		String name = form.getCaption();
		if(name==null){
			name=form.getName();
		}
		
		if(name==null){
			name="Untitled"+formId;
		}
		
		name=name+".xml";
		
		String xml = FormDaoHelper.exportForm(form);
		
		processAttachmentRequest(resp,xml.getBytes() , name);
		
	}

	private void processAttachmentRequest(HttpServletRequest req,
			HttpServletResponse resp) {
		String id = req.getParameter("attachmentId");
		if (id == null)
			return;

		LocalAttachment attachment = DB.getAttachmentDao().getAttachmentById(
				Long.parseLong(id));
		
		processAttachmentRequest(resp, attachment);
	}
	

	private void processAttachmentRequest(HttpServletResponse resp, LocalAttachment attachment) {

		resp.setContentType(attachment.getContentType());
		processAttachmentRequest(resp, attachment.getAttachment(), attachment.getName());
	}
	
	private void processAttachmentRequest(HttpServletResponse resp, byte[] data, String name ){
		if(name.endsWith("png") || name.endsWith("jpg") || name.endsWith("html") || name.endsWith("htm") 
				|| name.endsWith("svg") || name.endsWith("pdf")){
			//displayed automatically
			resp.setHeader("Content-disposition", "inline;filename=\""
					+ name);
		}else{
			resp.setHeader("Content-disposition", "attachment;filename=\""
					+ name);
		}
			
		
		resp.setContentLength(data.length);
		
		writeOut(resp, data);

	}

	private void writeOut(HttpServletResponse resp,
			byte[] data) {
		ServletOutputStream out = null;
		try{
			out = resp.getOutputStream();
			out.write(data);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		try{
			out.close();			
		}catch(Exception e){
			throw new RuntimeException(e);
		}		
	}

}
