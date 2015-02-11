package com.duggan.workflow.test;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TestABC {

	static Integer y;
	@Test
	public void comment(){
		int noOfDays = 7;
		Calendar prev = Calendar.getInstance();
		prev.roll(Calendar.DATE, -noOfDays);
		Date pastNDaysDate = prev.getTime();
		
		System.err.println(pastNDaysDate);
	}
}
