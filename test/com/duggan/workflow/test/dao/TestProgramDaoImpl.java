package com.duggan.workflow.test.dao;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.wira.pmgt.server.dao.ProgramDaoImpl;
import com.wira.pmgt.server.dao.biz.model.Period;
import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.shared.model.ProgramDetailType;

public class TestProgramDaoImpl {

	ProgramDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		dao= DB.getProgramDaoImpl();
	}
	
	@Test
	public void loadProgramsByType(){
		List<ProgramDetail> details = dao.getProgramDetails(ProgramDetailType.PROGRAM, dao.getPeriod(1L));
		Assert.assertNotNull(details);
		Assert.assertNotSame(details.size(), 0);
		System.out.println("Load All Size: "+details.size());
	}
	
	@Ignore
	public void loadPrograms(){
		List<ProgramDetail> details = dao.getProgramDetails(dao.getActivePeriod());
		Assert.assertNotNull(details);
		Assert.assertNotSame(details.size(), 0);
		System.out.println("Load All Size: "+details.size());
	}
	
	@Ignore
	public void getActivePeriod(){
		Period active = dao.getActivePeriod();
		Assert.assertNotNull(active);
		System.out.println(active.getDescription());
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}

}
