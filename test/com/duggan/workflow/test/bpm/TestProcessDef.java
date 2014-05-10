package com.duggan.workflow.test.bpm;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.model.ADDocType;
import com.wira.pmgt.server.dao.model.ProcessDefModel;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;

public class TestProcessDef {

	
	@Before
	public void setup() {
		DBTrxProvider.init();
		DB.beginTransaction();
	}

	@Test
	public void getProcessDefByDocType(){
		ADDocType type = DB.getDocumentDao().getDocumentTypeById(1L);
		
		Assert.assertNotNull(type);
		
		List<ProcessDefModel> model = DB.getProcessDao().getProcessesForDocType(type);
		
		Assert.assertNotNull(model);
		Assert.assertEquals(1, model.size());
	}
	

	@After
	public void tearDown() {
		//DB.rollback();
		DB.commitTransaction();
		DBTrxProvider.close();
	}

}
