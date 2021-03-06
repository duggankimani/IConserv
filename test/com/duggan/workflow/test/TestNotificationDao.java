package com.duggan.workflow.test;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.NotificationDaoImpl;
import com.wira.pmgt.server.dao.helper.NotificationDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.model.NotificationType;

public class TestNotificationDao {

	NotificationDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		
		DB.beginTransaction();
		dao = DB.getNotificationDao();
	}
	
	@Test
	public void createNotification(){
		
		Notification notification = new Notification();
		notification.setCreated(new Date());
		notification.setDocumentId(31L);
		//notification.setDocumentType(DocType.INVOICE);
		notification.setNotificationType(NotificationType.APPROVALREQUEST_APPROVERNOTE);
		notification.setOwner(LoginHelper.get().getUser("calcacuervo"));
		notification.setRead(false);
		notification.setSubject("Inv/001/01");
		notification.setTargetUserId(new HTUser("mariano"));
		notification = NotificationDaoHelper.saveNotification(notification);		
		DB.commitTransaction();
		Assert.assertNotNull(notification.getId());
				
		DB.beginTransaction();
		NotificationDaoHelper.delete(notification.getId());		
		DB.commitTransaction();
		
		Assert.assertTrue(NotificationDaoHelper.getAllNotifications("mariano").isEmpty());
	}
	
	@After
	public void destroy(){
		DB.rollback();
		DB.closeSession();
	}
}
