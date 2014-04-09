package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.Doc;

public class GetItemResult extends BaseResponse {

	Doc summary;

	public GetItemResult() {
		
	}

	public Doc getSummary() {
		return summary;
	}

	public void setSummary(Doc summary) {
		this.summary = summary;
	}
}
