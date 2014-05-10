package com.duggan.workflow.test;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.client.model.TaskType;
import com.wira.pmgt.server.dao.DocumentDaoImpl;
import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.HTSummary;

public class TestApprovalRequest {

	Integer documentId=5;
	String userId="calcacuervo";
	
	DocumentDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		
		DB.beginTransaction();
		dao = DB.getDocumentDao();
	}
	
	@Test
	public void submit(){
		//Document document = DocumentDaoHelper.getDocument(documentId);
		
		//HTSummary doc = document.toTask();
		Document doc = DocumentDaoHelper.getDocument(10L);
		JBPMHelper.get().createApprovalRequest("calcacuervo",doc);
		
		List<HTSummary> lst = JBPMHelper.get().getTasksForUser(userId, TaskType.APPROVALREQUESTNEW);
		
		for(HTSummary summary: lst){
			System.err.println(summary.getDocumentRef()+" : "+summary.getSubject()+" : "+summary.getDescription());
		}
		
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
	
}
