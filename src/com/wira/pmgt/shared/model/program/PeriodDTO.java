package com.wira.pmgt.shared.model.program;

import java.io.Serializable;
import java.util.Date;

import com.wira.pmgt.client.ui.util.DateUtils;
import com.wira.pmgt.shared.model.Listable;

public class PeriodDTO implements Serializable, Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String description;
	private Date startDate;
	private Date endDate;
	
	public PeriodDTO() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String getName() {
		if(id!=null){
			return id+"";
		}
		return description;
	}

	@Override
	public String getDisplayName() {
		return description;
	}

	public boolean isCurrentPeriod() {
		Date today = new Date();
		return startDate!=null && endDate!=null && (today.after(startDate) && today.before(endDate));
	}
	
	@Override
	public boolean equals(Object obj) {
		PeriodDTO other = (PeriodDTO)obj;
		if(other.id==null || id==null){
			return false;
		}
		
		return id.equals(other.id);
	}
	/**
	 * 
	 * @param period
	 * @return true if the provided period overlaps this period
	 */
	public boolean overlaps(PeriodDTO period){
		if(id!=null && period.getId()!=null){
			if(id.equals(period.getId())){
				return false;//Same period -- Period cannot overlap itself
			}
		}
		
		if(startDate==null || endDate==null || period.startDate==null || period.endDate==null){
			return false;
		}
		
		return (equals(startDate,period.startDate) || equals(startDate,period.endDate) || 
				equals(endDate,period.startDate) || equals(endDate,period.endDate)
				)||			
				(between(startDate, period.startDate, period.endDate) ||
				 between(endDate, period.startDate, period.endDate)	||
				 between(period.startDate, startDate,endDate) ||
				 between(period.endDate, startDate, endDate)
				);
	}

	/**
	 * 
	 * @param testDate
	 * @param start
	 * @param end
	 * @return true if testDate is between startdate and endDate
	 */
	private boolean between(Date testDate, Date start, Date end) {
		//Below parsing is requred to ensure all  dates have the same formats
		//GWT generated dates have timezones embedded while server side dates dont, creating an error in comparing dates
		testDate = DateUtils.CREATEDFORMAT.parse(DateUtils.CREATEDFORMAT.format(testDate));
		start = DateUtils.CREATEDFORMAT.parse(DateUtils.CREATEDFORMAT.format(start));
		end = DateUtils.CREATEDFORMAT.parse(DateUtils.CREATEDFORMAT.format(end));
		
		boolean btw = start.before(testDate) && end.after(testDate);
		if(btw){
			System.out.println(""+testDate+" is between "+start+" && "+end);
		}
		return btw; 
	}
	
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return true if the two dates are equal
	 */
	private boolean equals(Date date1, Date date2){
		boolean equals = date1.equals(date2);
		if(equals){
			System.out.println(date1+" == "+date2);
		}
		return equals; 
	}
}
