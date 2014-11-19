package com.wira.pmgt.shared.model.program;

import java.io.Serializable;
import java.util.HashMap;

import com.wira.pmgt.shared.model.PermissionType;

public class ProgramPermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Long, PermissionType> permissions = new HashMap<>();
	private String userId;
	private Long programId;
	
	@SuppressWarnings("unused")
	private ProgramPermission() {
	}
	
	public ProgramPermission(String userId) {
		this.userId = userId;
	}
	
	public ProgramPermission(String userId, Long programId) {
		this.userId = userId;
		this.programId=programId;
	}
	
	public void setPermission(Long programId, PermissionType type){
		permissions.put(programId, type);
	}

	public String getUserId() {
		return userId;
	}

	public Long getProgramId() {
		return programId;
	}

}
