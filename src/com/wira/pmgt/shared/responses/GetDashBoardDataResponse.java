package com.wira.pmgt.shared.responses;

import com.wira.pmgt.shared.model.dashboard.Data;

import java.lang.Integer;
import java.util.List;

public class GetDashBoardDataResponse extends BaseResponse {

	private Integer activeCount;
	private Integer requestCount;
	private Integer failureCount;
	private List<Data> requestAging;
	private List<Data> documentCounts;

	public GetDashBoardDataResponse() {
	}

	public GetDashBoardDataResponse(Integer activeCount, Integer requestCount,
			Integer failureCount, List<Data> requestAging, List<Data> documentCounts) {
		this.activeCount = activeCount;
		this.requestCount = requestCount;
		this.failureCount = failureCount;
		this.requestAging = requestAging;
		this.documentCounts = documentCounts;
	}

	public Integer getActiveCount() {
		return activeCount;
	}

	public Integer getRequestCount() {
		return requestCount;
	}

	public Integer getFailureCount() {
		return failureCount;
	}

	public List<Data> getRequestAging() {
		return requestAging;
	}

	public List<Data> getDocumentCounts() {
		return documentCounts;
	}

	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}

	public void setRequestCount(Integer requestCount) {
		this.requestCount = requestCount;
	}

	public void setFailureCount(Integer failureCount) {
		this.failureCount = failureCount;
	}

	public void setRequestAging(List<Data> requestAging) {
		this.requestAging = requestAging;
	}

	public void setDocumentCounts(List<Data> documentCounts) {
		this.documentCounts = documentCounts;
	}
}
