package com.wira.pmgt.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.wira.pmgt.server.dao.AttachmentDaoImpl;
import com.wira.pmgt.server.dao.model.LocalAttachment;
import com.wira.pmgt.server.db.DB;

public class UserImageExecutor extends FileExecutor {

	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles)
			throws UploadActionException {

		String err = null;
		String userId = request.getParameter("userId");
		assert userId!=null;
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {					
					String fieldName = item.getFieldName();
					String contentType=item.getContentType();					
					String name = item.getName();
					long size = item.getSize();
					
					LocalAttachment attachment  = new LocalAttachment();
					attachment.setCreated(new Date());
					attachment.setArchived(false);
					attachment.setContentType(contentType);
					attachment.setId(null);
					attachment.setImageUserId(userId);
					attachment.setName(name);
					attachment.setSize(size);
					attachment.setAttachment(item.get());					
					AttachmentDaoImpl impl = DB.getAttachmentDao();
					impl.deleteUserImage(userId);
					
					impl.save(attachment);
					
					registerFile(request, fieldName, attachment.getId());
					
				}catch(Exception e){
					e.printStackTrace();
					err = e.getMessage();
				}
			}
		}
			
		return err;
	}
	
	public void removeItem(HttpServletRequest request, String fieldName){
		Hashtable<String, Long> receivedFiles = getSessionFiles(request, false);
		Long fileId = receivedFiles.get(fieldName);
		
		DB.getFormDao().deleteForm(fileId);		
	}

}
