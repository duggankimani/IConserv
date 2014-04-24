package com.wira.pmgt.server.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.wira.pmgt.server.dao.model.PO;

@MappedSuperclass
public class ProgramBasicDetail extends PO {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private static final long serialVersionUID = 6975295488889785294L;
	
	@Column(unique=true, nullable=false)
	private String name;
	
	private String description;

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


}
