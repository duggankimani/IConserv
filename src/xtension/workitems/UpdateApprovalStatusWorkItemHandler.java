package xtension.workitems;

import org.apache.log4j.Logger;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;

import com.wira.pmgt.server.dao.helper.DocumentDaoHelper;
import com.wira.pmgt.shared.model.Document;

/**
 * This work item handler updates the approval status
 * of the local document. It should be called if the
 * document is completed(approved to the end) or 
 * rejected by any of the approvers in the process
 * 
 * @author duggan
 *
 */
public class UpdateApprovalStatusWorkItemHandler implements WorkItemHandler{
	
	private Logger log = Logger.getLogger(UpdateApprovalStatusWorkItemHandler.class);

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Long documentId = null;
		
		if(workItem.getParameter("DocumentId")!=null){
			documentId = new Long(workItem.getParameter("DocumentId").toString());
		}else if(workItem.getParameter("document")!=null){
			Document document = (Document)workItem.getParameter("document");
			documentId = document.getId();
			assert document!=null;
		}else{
			log.error("UpdateApprovalStatusWorkItemHandler: 'DocumentId' cannot be null;" +
					" please ensure you've declared a input called DocumentId, or document and " +
					" that you've mapped it appropriately in your BPMN File");
			throw new IllegalArgumentException("UpdateApprovalStatusWorkItemHandler: 'documentId' cannot be null;" +
					" please ensure you've declared a input called documentId, or document and " +
					" that you've mapped it appropriately in your BPMN File");
		}
		
		Object isApproved = workItem.getParameter("isApproved");
		
		if(isApproved==null){
			//nothing to update
			log.warn("UpdateApproval status [DocumentId+"+documentId+"] cannot be performed. Reason [isApproved==null]");
			manager.completeWorkItem(workItem.getId(), workItem.getParameters());
			return;
		}
		
		/*
		 * This work item is meant to primarily record premature
		 * end of process by an approver rejecting a document
		 * 
		 */
		
		String work = workItem.getId()+" : "+workItem.getName()+"; Process Completed = "+workItem.getParameter("isProcessComplete")+
				" :: Approved = "+isApproved;
		Boolean processCompleted = workItem.getParameter("isProcessComplete")==null ? false: 
			new Boolean(workItem.getParameter("isProcessComplete").toString());
				
		//only update document if process is completed or the document was rejected
		if(processCompleted || !(Boolean)isApproved){
			DocumentDaoHelper.saveApproval(new Long(documentId.toString()), (Boolean)isApproved);
			System.err.println("SAVE :: "+work);
		}else{
			System.err.println("Ignore :: "+work);
		}
		
		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		
	}

	
}
