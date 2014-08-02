package com.wira.pmgt.shared.model.program;

import java.io.Serializable;

import com.wira.pmgt.shared.model.ProgramDetailType;

public class BasicProgramDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private ProgramDetailType type;
	private ProgramStatus status;
	protected Long programId;
	protected Long parentId;
	
	public BasicProgramDetails(){}
	
	public BasicProgramDetails(String name, Long programId,
		Long id, Long parentId, ProgramDetailType type,ProgramStatus status) {
		this.name = name;
		this.programId = programId;
		this.id =id;
		this.parentId = parentId;
		this.type = type;
		this.status = status;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ProgramDetailType getType() {
		return type;
	}
	public void setType(ProgramDetailType type) {
		this.type = type;
	}
	public ProgramStatus getStatus() {
		return status;
	}
	public void setStatus(ProgramStatus status) {
		this.status = status;
	}
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
