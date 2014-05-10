package com.duggan.workflow.test.process;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.jbpm.ProcessMigrationHelper;
import com.wira.pmgt.shared.model.Document;

public class TestLPOApproval {

	Document doc;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(1L);
		ProcessMigrationHelper.start(4L);
		doc = DocumentDaoHelper.getDocument(68L);
	}
	
	@Test
	public void execute(){
		JBPMHelper.get().createApprovalRequest("calcacuervo",doc);
		
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();

//		try{
//			DB.commitTransaction();
//			DB.closeSession();
//		}catch(Exception e){
//			//if(e instanceof Batche)
//		}
	}
}
