package com.wira.pmgt.shared.model.program;

import java.io.Serializable;

/**
 * Funding Entity Details
 * <p>
 * @author duggan
 *
 */
public class FundDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String code; //Code - front end presentation
	private String name; //Donor Name
	private String description; // Any other details 
	
	public FundDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
