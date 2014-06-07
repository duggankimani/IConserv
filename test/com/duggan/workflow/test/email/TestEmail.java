package com.duggan.workflow.test.email;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.email.EmailServiceHelper;

public class TestEmail {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void sendEmail() throws IOException, MessagingException{
		InputStream is = TestEmail.class.getClass().getResourceAsStream("/email.html");
		String html = IOUtils.toString(is);
		assert html!=null;
		
		html = html.replace("${Request}","Task Assignment");
		html = html.replace("${OwnerId}", "calcacuervo");
		html = html.replace("${Office}", "mariano");
		html = html.replace("${Description}","Buy 200 Bags of Maize for Laikipia Primary");
		html = html.replace("${DocSubject}", "Task-122");
		html = html.replace("${DocumentDate}", SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(
				new Date()));
		html = html.replace("${DocType}","Task");
		html = html.replace("${DocumentURL}", "www.wira.io");
		System.err.println(html);
		EmailServiceHelper.sendEmail(html, "Further Test", "mdkimani@gmail.com", "mariano");
	}

	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		DB.closeSession();
	}
}
