package com.wira.jbpm.humantask;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.IsProgramDetail;

public class TestGetPrograms {


	@Before
	public void init(){
		DBTrxProvider.init();
		DB.beginTransaction();	
	}
	

	@Test
	public void createFunds(){
		
		List<IsProgramDetail> activities = ProgramDaoHelper.getProgramsByType(ProgramDetailType.PROGRAM,false);
		
		for(IsProgramDetail a: activities){
			System.err.println(a.getName()+" >> "+a.getProgramOutcomes());
		}
	}
	
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
	}
}
