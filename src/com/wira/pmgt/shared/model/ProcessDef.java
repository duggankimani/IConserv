package com.wira.pmgt.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessDef implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private String processId;
	
	private List<DocumentType> docTypes;
	
	private Date lastModified;
	
	private Long fileId;
	
	private String fileName;
	
	private String description;
	
	private Status status;
	
	private Long imageId;
	
	private String imageName;
	
	private List<Attachment> files = new ArrayList<Attachment>();

	public ProcessDef() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public List<DocumentType> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<DocumentType> docTypes) {
		this.docTypes = docTypes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		if(this.fileName==null && !files.isEmpty()){
		  String names = "";
		  for(Attachment att: files){
			  names = names.concat(att.getName()+",");
			  
		  }
		  
		  fileName = names.substring(0, names.lastIndexOf(','));
		}
		
		return fileName;
	}

	public void setFileName(String fileName) {		
		this.fileName = fileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public List<Attachment> getFiles() {
		return files;
	}

	public void setFiles(List<Attachment> files) {
		this.files = files;
	}

	public void addFile(Attachment attachment) {
		files.add(attachment);
	}
	
}
