package com.wira.jbpm.humantask;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.shared.model.program.FundDTO;

public class TestSaveFunds {

	@Before
	public void init(){
		DBTrxProvider.init();
		DB.beginTransaction();	
	}
	
	@Test
	public void createFunds(){
		FundDTO donor = new  FundDTO();
		donor.setName("USAID");
		donor.setDescription("USAID");
		ProgramDaoHelper.save(donor);
		
		donor = new  FundDTO();
		donor.setName("EKN");
		donor.setDescription("EKN");
		ProgramDaoHelper.save(donor);
		
		donor = new  FundDTO();
		donor.setName("Trade Finance");
		donor.setDescription("Trade Finance");
		ProgramDaoHelper.save(donor);
				
	}
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
	}

}
