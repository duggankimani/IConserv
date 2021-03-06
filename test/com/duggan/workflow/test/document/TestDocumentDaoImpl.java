package com.duggan.workflow.test.document;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.auth.LoginHelper;

public class TestDocumentDaoImpl {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void checkIfHasAttachment(){
		DB.getAttachmentDao().getHasAttachment(134L);
	}
	
	@Ignore
	public void testGetDocumentTypeByDocumentId(){
		
		DB.getDocumentDao().getDocumentTypeByDocumentId(100L);
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
