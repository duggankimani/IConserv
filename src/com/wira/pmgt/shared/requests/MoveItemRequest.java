package com.wira.pmgt.shared.requests;

import com.wira.pmgt.shared.responses.BaseResponse;
import com.wira.pmgt.shared.responses.MoveItemResponse;

public class MoveItemRequest extends BaseRequest<MoveItemResponse> {

	private Long itemToMoveId;
	private Long parentId;
	private Long outcomeId;

	private MoveItemRequest(){
	}
	
	public MoveItemRequest(Long itemToMoveId, Long parentId, Long outcomeId) {
		this.itemToMoveId = itemToMoveId;
		this.parentId = parentId;
		this.outcomeId = outcomeId;
	}

	public Long getItemToMoveId() {
		return itemToMoveId;
	}

	public void setItemToMoveId(Long itemToMoveId) {
		this.itemToMoveId = itemToMoveId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getOutcomeId() {
		return outcomeId;
	}

	public void setOutcomeId(Long outcomeId) {
		this.outcomeId = outcomeId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new MoveItemResponse();
	}
}
