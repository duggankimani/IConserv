package com.duggan.workflow.test.settings;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.model.LocalAttachment;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.shared.model.settings.SETTINGNAME;

public class TestGetLogo {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void get(){
		LocalAttachment attachment = DB.getAttachmentDao().getSettingImage(SETTINGNAME.ORGLOGO);
		Assert.assertNotNull(attachment);
		
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		DB.closeSession();
	}
}
