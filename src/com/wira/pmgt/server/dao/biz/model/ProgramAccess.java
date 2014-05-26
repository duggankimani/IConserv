package com.wira.pmgt.server.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;

import com.wira.pmgt.server.dao.model.PO;
import com.wira.pmgt.shared.model.ParticipantType;

/**
 * User/ Group Access permissions
 * 
 * @author duggan
 *
 */
@Entity
public class ProgramAccess extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Index(name="idx_userid")
	private String userId;
	
	@Index(name="idx_groupid")
	private String groupId;
	
	@ManyToOne
	@JoinColumn(name="programdetailid", nullable=false,referencedColumnName="id")
	private ProgramDetail programDetail;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private ParticipantType type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ParticipantType getType() {
		return type;
	}

	public void setType(ParticipantType type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public ProgramDetail getProgramDetail() {
		return programDetail;
	}

	public void setProgramDetail(ProgramDetail programDetail) {
		this.programDetail = programDetail;
	}

	@Override
	public boolean equals(Object obj) {
	
		if(!(obj instanceof ProgramAccess)){
			return false;
		}
		
		ProgramAccess other = (ProgramAccess)obj;
		
		if(userId!=null && programDetail!=null){
			return userId.equals(other.userId) && type==other.type;
		}
		
		if(groupId!=null && programDetail!=null){
			return groupId.equals(other.groupId) && type==other.type;
		}
		
		return false;
	}
}
