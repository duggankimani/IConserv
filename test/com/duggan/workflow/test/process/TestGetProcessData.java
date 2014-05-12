package com.duggan.workflow.test.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.task.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.server.helper.email.EmailServiceHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.jbpm.ProcessMigrationHelper;
import com.wira.pmgt.shared.model.Actions;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.HTSummary;
import com.wira.pmgt.shared.model.HTask;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.form.ProcessMappings;

public class TestGetProcessData {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
//		ProcessMigrationHelper.start(14L);
//		ProcessMigrationHelper.start(16L);
//		ProcessMigrationHelper.start(4L);
	}
	
	@Test
	public void load(){
		EmailServiceHelper.getProperties();
	}
	
	@Ignore
	public void submitTask(){
		String userId="calcacuervo";
		Long taskId=2458L;
		Actions action = Actions.COMPLETE;
		
		Map<String, Object> vals = new HashMap<>();
		
		Map<String, Value> values = new HashMap<String, Value>();
		if(values!=null){
			long processInstanceId=0L;
			Task task = JBPMHelper.get().getTaskClient().getTask(taskId);
			processInstanceId = task.getTaskData().getProcessInstanceId();
						
			Document document = DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId,false);
			assert document!=null;
			
			ProcessMappings mappings = JBPMHelper.get().getProcessDataMappings(taskId);
			
			for(String key: values.keySet()){
				Value value = values.get(key);				
				vals.put(key, value==null?null: value.getValue());
				if(key!=null){
					key = mappings.getOutputName(key);
					document.setValue(key,value);
				}
			}
			vals.put("documentOut", document);
		}
		
		JBPMHelper.get().execute(taskId, userId, action, vals);
	}
	
	@Ignore
	public void getTask(){
		Long taskId = 2456L;
		HTSummary task = JBPMHelper.get().getTask(taskId);
		
	}
	
	@Ignore
	public void getTaskParameters(){
		Long taskId = 1506L;
		String name= JBPMHelper.get().getDisplayName(taskId);
		
		System.out.println("Name ="+name);
	}
	
	@Ignore
	public void getParameterz(){
		//Long taskId = 1518L;
		Long taskId = 2568L;
		Task task = JBPMHelper.get().getSysTask(taskId);
		
		Map<String,Object> vls = JBPMHelper.get().getMappedData(task);
		for(String key: vls.keySet()){
			Object v = vls.get(key);
			System.err.println(key+" - "+v);
		}
		
		Assert.assertNotNull(vls);
		Assert.assertNotSame(vls.size(), 0);
		
		//Assert.assertNotNull(summary.getDetails());
		//Assert.assertTrue(summary.getDetails().size()>0);
	}
	
	@Ignore
	public void getParameters(){
		Long taskId = 1518L;
		HTask summary = JBPMHelper.get().getTask(taskId);
		Map<String,Value> vls = summary.getValues();
		for(String key: vls.keySet()){
			Value v = vls.get(key);
			System.err.println(key+" - "+v.getValue());
		}
		Map<String, Value> values = summary.getValues();
		Assert.assertNotNull(values);
		Assert.assertNotSame(values.size(), 0);
		
		//Assert.assertNotNull(summary.getDetails());
		//Assert.assertTrue(summary.getDetails().size()>0);
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
