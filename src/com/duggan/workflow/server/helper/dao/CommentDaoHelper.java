package com.duggan.workflow.server.helper.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.duggan.workflow.server.dao.CommentDaoImpl;
import com.duggan.workflow.server.dao.model.CommentModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Comment;

public class CommentDaoHelper {

	public static Comment saveComment(Comment comment){
		
		CommentDaoImpl dao = DB.getCommentDao();
		
		CommentModel model = new CommentModel();
		
		if(comment.getId()!=null){
			model = dao.getComment(comment.getId());			
		}
		
		copyData(model, comment);
		
		model = dao.saveOrUpdate(model);
		
		comment.setId(model.getId());
		
		return comment;
	}
	
	public static List<Comment> getAllComments(String userId){
		CommentDaoImpl dao = DB.getCommentDao();
		List<CommentModel> models = dao.getAllComments(userId);
		
		return copyData(models);
	}
	
	private static List<Comment> copyData(List<CommentModel> models) {
		List<Comment> Comments = new ArrayList<>();
		
		for(CommentModel m:models){
			Comment note = new Comment();
			copyData(note, m);
			Comments.add(note);
		}
		
		return Comments;
	}


	private static void copyData(CommentModel commentTo,
			Comment modelFrom) {
		assert commentTo!=null;
				
		if(commentTo.getId()==null){
			commentTo.setCreated(new Date());
			
			if(SessionHelper.getCurrentUser()!=null)
				commentTo.setCreatedBy(SessionHelper.getCurrentUser().getId());
		}else{
			commentTo.setUpdated(new Date());
			if(SessionHelper.getCurrentUser()!=null)
				commentTo.setUpdatedBy(SessionHelper.getCurrentUser().getId());
		}
		
		commentTo.setDocumentId(modelFrom.getDocumentId());
		commentTo.setComment(modelFrom.getComment());
		commentTo.setId(modelFrom.getId());
		commentTo.setUserId(modelFrom.getUserId());
		commentTo.setParentId(modelFrom.getParentId());
		
	}
	
	private static void copyData(Comment commentTo,
			CommentModel modelFrom) {
				
		commentTo.setDocumentId(modelFrom.getDocumentId());
		
		String owner = modelFrom.getCreatedBy();
		HTUser createdBy = LoginHelper.get().getUser(owner);
		commentTo.setCreatedByUser(createdBy);
		commentTo.setCreated(modelFrom.getCreated());
		commentTo.setCreatedBy(owner);
		commentTo.setDocumentId(modelFrom.getDocumentId());
		commentTo.setId(modelFrom.getId());
		commentTo.setUpdated(modelFrom.getUpdated());
		commentTo.setUpdatedBy(modelFrom.getUpdatedBy());
		commentTo.setUserId(commentTo.getUserId());
		commentTo.setParentId(modelFrom.getParentId());
	}

	public static void delete(Long id) {
		CommentDaoImpl dao = DB.getCommentDao();
		dao.delete(id);
	}

	public static List<Comment> getAllComments(Long documentId) {
		CommentDaoImpl dao = DB.getCommentDao();
		List<CommentModel> comments = dao.getAllComments(documentId);
		
		List<Comment> rtn = copyData(comments);
		
		return rtn;
	}
}