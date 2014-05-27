package xtension.workitems;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.model.program.ProgramStatus;

public class UpdateActivityStatus implements WorkItemHandler {

	static Logger logger = Logger.getLogger(UpdateActivityStatus.class);
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		if(workItem.getParameter("status")==null){
			logger.error("Variable 'status' not found. Cannot Update Activity Status");
			throw new IllegalArgumentException("UpdateActivityStatus.executeWorkitem:Variable 'status' cannot be null," +
					"Please check your BPMN Process");
		}
		
		String status = (String)workItem.getParameter("status");
		
		Long programId = null;
		if(workItem.getParameter("programId")==null){
			
		}
		programId = new Long(workItem.getParameter("programId").toString());

		ProgramDetail detail = DB.getProgramDaoImpl().getProgramDetail(programId);
		detail.setStatus(ProgramStatus.valueOf(status));
		DB.getProgramDaoImpl().save(detail);
		
		manager.completeWorkItem(workItem.getId(), new HashMap<String, Object>());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		
	}
}
