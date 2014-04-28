package com.wira.jbpm.humantask;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.shared.model.program.FundDTO;
import com.wira.pmgt.shared.model.program.IsProgramActivity;
import com.wira.pmgt.shared.model.program.ProgramFundDTO;

public class TestSaveFunds {

	@Before
	public void init(){
		DBTrxProvider.init();
		DB.beginTransaction();	
	}
	
	@Test
	public void updateFund(){
		IsProgramActivity fund= ProgramDaoHelper.getProgramById(11L, false,false);
		List<ProgramFundDTO> programFunds = fund.getFunding();
		
		ProgramFundDTO programFundDto = programFunds.get(0);
		FundDTO fundDto = programFundDto.getFund();
		
		//Toggle fund (Change fund from one donor to another)
		if(fundDto.getId()==7L){
			fundDto.setId(8L);//EKN
			fundDto.setName("EKN");
			fundDto.setDescription("EKN");
		}else{
			fundDto.setId(7L);//USAID
			fundDto.setName("USAID");
			fundDto.setDescription("USAID");
		}
		
		programFundDto.setFund(fundDto);		
		fund.setFunding(Arrays.asList(programFundDto));
		ProgramDaoHelper.save(fund);
	}
	
	@Ignore
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
