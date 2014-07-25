package com.wira.pmgt.shared.model.program;

import java.io.Serializable;

public class PerformanceModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long programId;
	private String name;
	private String description;
	private int countSuccess;
	private int countFail;
	private int countNoData;
	private double percSuccess;
	private double percFail;
	private double avgPerSuccess;
	
	public PerformanceModel() {
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
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

	public int getCountSuccess() {
		return countSuccess;
	}

	public void setCountSuccess(int countSuccess) {
		this.countSuccess = countSuccess;
	}

	public int getCountFail() {
		return countFail;
	}

	public void setCountFail(int countFail) {
		this.countFail = countFail;
	}

	public int getCountNoData() {
		return countNoData;
	}

	public void setCountNoData(int countNoData) {
		this.countNoData = countNoData;
	}

	public double getPercSuccess() {
		return percSuccess;
	}

	public void setPercSuccess(double percSuccess) {
		this.percSuccess = percSuccess;
	}

	public double getPercFail() {
		return percFail;
	}

	public void setPercFail(double percFail) {
		this.percFail = percFail;
	}

	public double getAvgPerSuccess() {
		return avgPerSuccess;
	}

	public void setAvgPerSuccess(double avgPerSuccess) {
		this.avgPerSuccess = avgPerSuccess;
	}
		
	public int getPercCountWithData(){
		double total = getCountFail()+ getCountSuccess()+getCountNoData();;
		if(total==0){
			return 0;
		}
		
		Double value = (getCountFail()+getCountSuccess())*100/total;
		return value.intValue();

	}
	
	public int getPercCountWithNoData(){
		double total = getCountFail()+ getCountSuccess()+getCountNoData();;
		if(total==0){
			return 0;
		}
		
		return 100-getPercCountWithData();
	}
	
	public int getTotalCount(){
		double total = getCountFail()+ getCountSuccess()+getCountNoData();
		
		return ((Double)total).intValue();
		
	}
	
}
