package com.wira.pmgt.shared.responses;

import java.util.List;

import com.wira.pmgt.shared.model.dashboard.Data;

public class GetTaskCompletionResponse extends BaseResponse {

	private List<Data> data;

	public GetTaskCompletionResponse() {
	}


	public List<Data> getData() {
		return data;
	}


	public void setData(List<Data> data) {
		this.data = data;
	}
}
