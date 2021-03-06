package com.wira.pmgt.server.helper.auth.callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jbpm.task.identity.UserGroupCallback;

import com.wira.pmgt.server.dao.model.Group;
import com.wira.pmgt.server.dao.model.User;
import com.wira.pmgt.server.db.DB;

public class DBUserGroupCallbackImpl implements UserGroupCallback{

	private static Logger logger = Logger.getLogger(DBUserGroupCallbackImpl.class);
	
	@Override
	public boolean existsUser(String userId) {
		
		User user = DB.getUserGroupDao().getUser(userId);
		
		assert user!=null;
		
		logger.warn("");
		return user!=null;
	}

	@Override
	public boolean existsGroup(String groupId) {
		
		Group group = DB.getUserGroupDao().getGroup(groupId);
		
		assert group!=null;
		
		return group!=null;
	}

	@Override
	public List<String> getGroupsForUser(String userId, List<String> groupIds,
			List<String> allExistingGroupIds) {
		
		List<String> groupNames = new ArrayList<>();
		
		Collection<Group> groups = DB.getUserGroupDao().getAllGroups(userId);
		
		if(groups!=null){
			Iterator<Group> iter = groups.iterator();
			while(iter.hasNext()){
				groupNames.add(iter.next().getName());
			}
		}
		
		assert groupNames.size()>0;
		
		return groupNames;
	}

}
