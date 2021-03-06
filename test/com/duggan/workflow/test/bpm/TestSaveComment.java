package com.duggan.workflow.test.bpm;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.wira.pmgt.server.dao.helper.CommentDaoHelper;
import com.wira.pmgt.server.dao.helper.NotificationDaoHelper;
import com.wira.pmgt.server.dao.model.CommentModel;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.shared.model.Comment;
import com.wira.pmgt.shared.model.Notification;
import com.wira.pmgt.shared.model.NotificationType;

public class TestSaveComment {

	@Before
	public void setup() {
		DBTrxProvider.init();
		LoginHelper.get();
		DB.beginTransaction();
	}

	@Ignore
	public void getAllComments(){
		List<CommentModel> list = DB.getCommentDao().getAllComments();
		System.err.println(">> "+list.size());
	}
	
	@Test
	public void getActivities(){
		List<Notification> notes = NotificationDaoHelper.getAllNotifications(null, NotificationType.APPROVALREQUEST_OWNERNOTE);
		System.err.println("Notes:: "+notes.size());		
	}
	
	@Ignore
	public void save() {
		Comment comment = new Comment();
		comment.setComment("Comment xyz .......... ");
		comment.setCreated(new Date());
		comment.setCreatedBy(LoginHelper.get().getUser("mariano"));
		comment.setDocumentId(2L);
		comment.setId(null);
		comment.setUserId("mariano");
		CommentDaoHelper.saveComment(comment);
	}

	@After
	public void tearDown() {
		DB.commitTransaction();
		try {
			LoginHelper.get().close();
		} catch (Exception e) {
		}
		DBTrxProvider.close();
	}
}
