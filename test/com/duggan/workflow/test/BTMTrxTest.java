package com.duggan.workflow.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.model.ADDocType;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;

public class BTMTrxTest {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void persist(){
		ADDocType docType = DB.getDocumentDao().getDocumentTypeById(1L);
		String subject = DB.getDocumentDao().generateDocumentSubject(docType);
		System.out.println(subject);
	}
	
	@After
	public void destroy(){
		DB.rollback();
		DBTrxProvider.close();
	}
}
