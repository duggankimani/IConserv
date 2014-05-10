package com.duggan.workflow.test.bpm;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.jbpm.ProcessMigrationHelper;
import com.wira.pmgt.shared.model.NodeDetail;

public class TestGetApprovalNodes {

	@Before
	public void setup(){
		DBTrxProvider.init();
		ProcessMigrationHelper.init();
	}
	
	@Test
	public void getApprovalStatus(){
		/*
		 * Approach 1 - Get all Tasks generated & their statuses
		 * and use that as the approval log (Match approvers & not approval nodes)
		 */
		
		/*
		 *Approach 2 - Get all completed Human Task nodes 
		 */
		//long processInstanceId =  118L;
		long processInstanceId =  133L;
//		List<Node> nodes = JBPMHelper.get().getProcessDia(processInstanceId);
//		
//		for(Node node: nodes){
//			System.err.println("############ "+node.getName());
//		}
		List<NodeDetail> nodes = JBPMHelper.get().getWorkflowProcessDia(processInstanceId);
		System.err.println(nodes);
	}

	@After
	public void tearDown(){
		try{
			LoginHelper.get().close();
		}catch(Exception e){}
		DBTrxProvider.close();		
	}
}
