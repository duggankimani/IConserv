package com.wira.pmgt.server.guice;

import javax.servlet.ServletContextEvent;


import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.Injector;
import com.google.inject.Guice;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.guice.DispatchServletModule;
import com.wira.pmgt.server.guice.ServerModule;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.jbpm.ProcessMigrationHelper;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice
				.createInjector(new ServerModule(), new DispatchServletModule());
	}
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		super.contextInitialized(servletContextEvent);
		DBTrxProvider.init();
		JBPMHelper.get();
		
//		try{		
//			DB.beginTransaction();
//			ProcessMigrationHelper.init();
//			DB.commitTransaction();			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			DB.closeSession();
//		}
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		super.contextDestroyed(servletContextEvent);
		
		//JBPMHelper.destroy();
		DBTrxProvider.close();
		try{
			//close ldap connection
			LoginHelper.get().close();
		}catch(Exception e){}
		
	}
}
