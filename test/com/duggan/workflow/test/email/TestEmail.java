package com.duggan.workflow.test.email;

import java.io.IOException;
import java.io.InputStream;

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
		String body = IOUtils.toString(is);
		assert body!=null;
		
		EmailServiceHelper.sendEmail(body, "RE: Further Testing", "mdkimani@gmail.com");
	}

	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		DB.closeSession();
	}
}
