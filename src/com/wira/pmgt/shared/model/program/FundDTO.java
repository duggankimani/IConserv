package com.wira.pmgt.shared.model.program;

import java.io.Serializable;

import com.wira.pmgt.shared.model.Listable;

/**
 * Funding Entity Details
 * <p>
 * @author duggan
 *
 */
public class FundDTO implements Serializable,Listable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name; //Donor Name
	private String description; // Any other details 
	private boolean isActive= true;
	
	public FundDTO() {
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

	@Override
	public String getDisplayName() {
		
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		FundDTO other = (FundDTO)obj;
		
		return other.name.equals(name);
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
