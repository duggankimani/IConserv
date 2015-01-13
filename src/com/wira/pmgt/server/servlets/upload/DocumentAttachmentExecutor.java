package com.wira.pmgt.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.wira.pmgt.server.dao.helper.AttachmentDaoHelper;
import com.wira.pmgt.server.dao.model.LocalAttachment;
import com.wira.pmgt.server.db.DB;

public class DocumentAttachmentExecutor extends FileExecutor{

	public String execute(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		String errorMessage="";
		
		Hashtable<String, Long> receivedFiles = getSessionFiles(request, true);
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {					
					String fieldName = item.getFieldName();
					
					//Name of the form field against which this file was uploaded 
					String formFieldName= request.getParameter("formFieldName");
					String overwrite = request.getParameter("overwrite");
					String contentType=item.getContentType();					
					String name = item.getName();
					long size = item.getSize();
					
					LocalAttachment attachment  = new LocalAttachment();
					attachment.setCreated(new Date());
					attachment.setArchived(false);
					attachment.setContentType(contentType);
					attachment.setId(null);
					attachment.setName(name);
					attachment.setSize(size);
					attachment.setAttachment(item.get());
					attachment.setFieldName(formFieldName);
					saveAttachment(attachment, request, overwrite==null? true : overwrite.equals("Y"));
					
					receivedFiles.put(fieldName, attachment.getId());
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}else{
				//handle form fields here 
			}
		}

		
		return errorMessage;
	}
	
	private void saveAttachment(LocalAttachment attachment,
			HttpServletRequest request, boolean overwriteIfExists) {
		
		String id = request.getParameter("documentId");
		
		if(id!=null){
			Long documentId = Long.parseLong(id.toString());
			
			if(overwriteIfExists){
				DB.getAttachmentDao().deleteAttachments(documentId,attachment.getFieldName());
			}
			
			AttachmentDaoHelper.saveDocument(documentId, attachment);
		}
	}

}
