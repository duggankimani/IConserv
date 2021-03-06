package com.wira.pmgt.shared.model.program;

import java.io.Serializable;
import java.util.Date;

import com.wira.pmgt.shared.model.ProgramDetailType;

public class ProgramSummary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private Long id;
	private String name;
	private String description;
	private ProgramDetailType type;
	private ProgramStatus status;
	private Long parentId; 
	private Date startDate;
	private Date endDate; 
	protected Long programId; //Main Parent Program ID
	private Date dateCompleted;
	
	public ProgramSummary(){}
	
	public ProgramSummary(String name, String description, Long programId,
			Long id, Long parentid, ProgramDetailType type, Date startDate,
			Date endDate, ProgramStatus status, Date dateCompleted) {
		this.name = name;
		this.description=description;
		this.programId = programId;
		this.id =id;
		this.parentId = parentid;
		this.type = type;
		this.startDate=startDate;
		this.endDate = endDate;
		this.status = status;
		this.dateCompleted = dateCompleted;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getProgramId() {
		return programId;
	}
	
	public boolean isOverdue(){
		return endDate!=null && (status!=ProgramStatus.COMPLETED && status!=ProgramStatus.CLOSED) && endDate.before(new Date());
	}
	
	public boolean isNotStarted(){
		return startDate!=null && status==ProgramStatus.CREATED && startDate.before(new Date());
	}
	
	public boolean isUpcoming(){		
		return startDate!=null && new Date().before(startDate) && status==ProgramStatus.CREATED;
	}
	
	public boolean isOnGoing(){
		return startDate!=null && (status==ProgramStatus.OPENED || status==ProgramStatus.REOPENED) 
				&& startDate.before(new Date()) && endDate.after(new Date());
	}
	
	public boolean isCompleted(){
		return status==ProgramStatus.CLOSED || status==ProgramStatus.COMPLETED;
	}
	
	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

}
