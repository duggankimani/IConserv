package com.wira.pmgt.server.dao.biz.model;

import java.lang.String;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.wira.pmgt.server.dao.model.PO;

@Entity
@NamedQueries({
	@NamedQuery(name="Fund.findAll",query="FROM Fund f where f.isActive=:isActive")
})
public class Fund extends PO {

	private static final long serialVersionUID = 1517092783282462575L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String code; //Code - front end presentation
	private String name; //Donor Name
	private String description; // Any other details 
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="fund")	
	private Set<ProgramFund> programFunds = new HashSet<>(); //Allocation to a program/objective/outcome/activity etc

	public Fund() {
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<ProgramFund> getProgramFunds() {
		return programFunds;
	}

	public void setProgramFunds(Set<ProgramFund> programFunds) {
		this.programFunds = programFunds;
	}
}
