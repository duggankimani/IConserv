package com.wira.pmgt.shared.model;

import java.io.Serializable;
import java.lang.String;
import java.util.List;

public class HTUser implements Serializable, Listable, OrgEntity {

	private static final long serialVersionUID = -5249516544970187459L;
	private Long id;
	private String name;
	private String userId;
	private String email;
	private String surname;
	private String password;
	private List<UserGroup> groups ;
	
	public HTUser() {
	}
	
	public HTUser(String id) {
		this.userId = id;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public String getName() {
		return name;
	}

	public String getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSurname() {
		if(surname==null){
			return userId;
		}
		
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFullName(){
		if(surname==null ||name==null){
			return userId;
		}
		
		return surname+" "+name;
	}
	
	@Override
	public String toString() {
		return userId;
	}

	public List<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}
	
	public String getGroupsAsString(){
		StringBuffer out = new StringBuffer();
		if(groups!=null){
			for(UserGroup group: groups){
				out.append(group.getName()+",");
			}
		}
		
		if(out.length()>0){
			return out.substring(0, out.length()-1);
		}
		
		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean hasGroup(String groupName) {
		for(UserGroup group:groups){
			if(group.getName().equalsIgnoreCase(groupName)){
				return true;
			}
		}
		return false;
	}

	public boolean isAdmin() {
		return hasGroup("admin");
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof HTUser)){
			return false;
		}
		
		HTUser other =  (HTUser)obj;
		
		if(name==null){
			return false;
		}
		
		return name.equals(other.name);
	}

	@Override
	public String getDisplayName() {
		return getFullName();
	}

	@Override
	public String getEntityId() {
		return userId;
	}
}
