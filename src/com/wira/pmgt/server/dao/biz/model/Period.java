package com.wira.pmgt.server.dao.biz.model;

import java.lang.Long;
import java.lang.String;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.wira.pmgt.server.dao.model.PO;

/**
 * Program Calendar Periods</p>
 * All the program targets/ outcomes/ finances are managed within 
 * a single calendar period, usually 1st Jan - 31st Dec or 1 July - 30 June
 *  
 * @author duggan
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Period.findActive", query="FROM Period p where p.isActive=:isActive and p.startDate<:now and p.endDate>:now order by description"),
	@NamedQuery(name="Period.findAll", query="FROM Period p"),
	@NamedQuery(name="Period.findById", query="FROM Period p where p.id=:id")
	})
public class Period extends PO{

	private static final long serialVersionUID = -5038433776061027590L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String description;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="period")
	private Set<ProgramDetail> programs = new HashSet<>();//Programs and activities 
	
	public Period() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Set<ProgramDetail> getPrograms() {
		return programs;
	}

	public void setPrograms(Set<ProgramDetail> programs) {
		this.programs = programs;
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
}
