package com.wira.pmgt.server.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wira.pmgt.shared.model.ApproverAction;
import com.wira.pmgt.shared.model.DocStatus;
import com.wira.pmgt.shared.model.Document;
import com.wira.pmgt.shared.model.Notification;

public class GetDocumentDS {

	public GetDocumentDS() {
		// TODO Auto-generated constructor stub
	}
		
	public static List<DocumentReport> createBeanCollection(){
		
		Document doc = new Document();
		doc.setCreated(new Date());
		doc.setPartner("ABC LTD");
		doc.setSubject("INV/565/12");
		//doc.setType(DocType.REQUISITION);
		doc.setStatus(DocStatus.REJECTED);
		doc.setDocumentDate(new Date());
		
		DocumentReport r = new DocumentReport();
		r.setDocument(doc);
		
		List<Notification> notifications = new ArrayList<>();
		Notification notification = new Notification();
		notification.setCreated(new Date());
		notification.setApproverAction(ApproverAction.APPROVED);
		//notification.setCreatedBy("Duggan");
		notifications.add(notification);
		
		Notification notification1 = new Notification();
		notification1.setCreated(new Date());
		notification1.setApproverAction(ApproverAction.REJECTED);
		//notification1.setCreatedBy("Mariano");
		notifications.add(notification1);
		
		r.setNotifications(notifications);
		List<DocumentReport> coll = new ArrayList<>();
		
		coll.add(r);
		
		return coll;
	}
}
