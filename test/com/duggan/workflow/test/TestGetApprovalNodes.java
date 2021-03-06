package com.duggan.workflow.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.jbpm.ProcessMigrationHelper;
import com.wira.pmgt.shared.model.NodeDetail;

public class TestGetApprovalNodes {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		ProcessMigrationHelper.init();
	}
	
	@Test
	public void getApprovalNodeStatus(){
		List<NodeDetail> lst = JBPMHelper.get().getWorkflowProcessDia(12L);
		
		System.err.println("List :: "+lst.size());
		//Assert.assertTrue(lst.size()==2);
		
	}
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
		DBTrxProvider.close();
	}
}
