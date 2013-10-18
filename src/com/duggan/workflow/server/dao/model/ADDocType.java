package com.duggan.workflow.server.dao.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ADDocType extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true)
	private String name;
	
	private String display;
	
	private String className;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="type")
	private Collection<DocumentModel> documents = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name="processDefId", referencedColumnName="id")
	private ProcessDefModel processDef;
	
	public ADDocType() {
	}
	
	public ADDocType(Long id, String name, String display){
		this.id = id;
		if(name!=null)
			name = name.toUpperCase();
		
		this.name = name;
		this.display= display;
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
		if(name!=null)
			name = name.toUpperCase();
		
		this.name = name;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}

	public ProcessDefModel getProcessDef() {
		return processDef;
	}

	public void setProcessDef(ProcessDefModel processDef) {
		this.processDef = processDef;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ADDocType))
			return false;
		
		ADDocType other = (ADDocType)obj;
		
		if(id==null ^ other.id==null)
			return false;
		
		if(id!=null){
			return id.equals(other.id);
		}
		
		if(name==null ^ other.name==null){
			return false;
		}
		
		if(name!=null)
			if(!name.equals(other.name)){
				return false;
			}
		
		
		return true;
	}
	
	
	@Override
	public int hashCode() {
		int hashcode =7;
		
		if(id!=null){
			hashcode += hashcode+id.hashCode();
		}
		
		if(name!=null){
			hashcode += 17+name.hashCode();
		}
		
		if(hashcode!=7){
			return hashcode;
		}
		
		return super.hashCode();
	}
}
