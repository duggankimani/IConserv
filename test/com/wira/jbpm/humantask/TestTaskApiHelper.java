package com.wira.jbpm.humantask;

import org.junit.After;
import org.junit.Before;

import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;

public class TestTaskApiHelper {

	@Before
	public void init(){
		DBTrxProvider.init();
		DB.beginTransaction();	
	}
	
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
	}
}
