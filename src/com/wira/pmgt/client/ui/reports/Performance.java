package com.wira.pmgt.client.ui.reports;

public class Performance {
	private String title;
	private Integer percentage;
	private PerformanceType type;
	private boolean showPercentage=true;

	public Performance(String title, Integer percentage, PerformanceType perfomanceType) {
		this.title = title;
		this.percentage = percentage;
		this.type=perfomanceType;
	}
	
	public Performance(String title, Integer percentage, PerformanceType perfomanceType, boolean showPercentage) {
		this(title,percentage,perfomanceType);
		this.showPercentage = showPercentage;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getPercentage() {
		return percentage;
	}
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	public PerformanceType getPerfomanceType() {
		return type;
	}
	public void setPerfomanceType(PerformanceType perfomanceType) {
		this.type = perfomanceType;
	}
	
	
	public enum PerformanceType{
		GOOD("Good"),
		AVERAGE("Average"),
		POOR("Poor"),
		NODATA("No Data");
		
		String displayName;
		
		private PerformanceType(String name) {
			this.displayName = name;
		}
		
		public String getDisplayName() {
			return displayName;
		}
	}

	public boolean isShowPercentage() {
		return showPercentage;
	}

	public void setShowPercentage(boolean showPercentage) {
		this.showPercentage = showPercentage;
	}
}
