package com.duggan.workflow.test.export;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.helper.FormDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;

public class TestFormExport {


	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void export(){
		String out = FormDaoHelper.exportForm(2L);
		System.err.println(out);
		FormDaoHelper.importForm(out+"   ");
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}

}
