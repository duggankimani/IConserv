package com.wira.pmgt.shared.model.program;

import java.io.Serializable;
import java.util.Date;

import com.wira.pmgt.shared.model.Listable;

public class PeriodDTO implements Serializable, Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String description;
	private Date startDate;
	private Date endDate;
	
	public PeriodDTO() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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

	@Override
	public String getName() {
		if(id!=null){
			return id+"";
		}
		return description;
	}

	@Override
	public String getDisplayName() {
		return description;
	}
	
}
