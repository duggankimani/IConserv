package com.wira.pmgt.shared.model.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wira.pmgt.shared.model.ProgramDetailType;

/**
 * 
 * @author duggan
 * Tree Node
 */
public class ProgramTreeModel extends BasicProgramDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long parentNodeId;
	public List<ProgramTreeModel> children= new ArrayList<ProgramTreeModel>();

	private Long outcomeId;
	private String outcomeName;
	
	public ProgramTreeModel() {
	}
	
	public ProgramTreeModel(String name, Long programId,
		Long id, Long parentid, ProgramDetailType type, ProgramStatus status,
		Long outcomeId,String outcomeName){
		super(name, programId,id,parentid,type, status);
		this.outcomeId = outcomeId;
		this.outcomeName = outcomeName;
	}

	public Long getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(Long parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public List<ProgramTreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<ProgramTreeModel> children) {
		this.children = children;
	}
	
	public void addChild(ProgramTreeModel node){
		children.add(node);
	}

	public Long getOutcomeId() {
		return outcomeId;
	}

	public void setOutcomeId(Long outcomeId) {
		this.outcomeId = outcomeId;
	}

	public String getOutcomeName() {
		return outcomeName;
	}

	public void setOutcomeName(String outcomeName) {
		this.outcomeName = outcomeName;
	}
	
}
