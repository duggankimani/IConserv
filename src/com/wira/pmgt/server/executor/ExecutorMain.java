/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.pmgt.server.executor;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;


import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.executor.api.CommandCodes;
import com.wira.pmgt.server.executor.api.CommandContext;
import com.wira.pmgt.server.executor.entities.RequestInfo;
import com.wira.pmgt.server.executor.impl.ExecutorFactory;

import static com.wira.pmgt.server.executor.api.CommandCodes.*;

/**
 * 
 * @author salaboy
 */
public class ExecutorMain {

	public static void main(String[] args) {
		System.out.println("Starting Executor Service ...");
		final ExecutorServiceEntryPoint executorServiceEntryPoint = ExecutorModule
				.getInstance().getExecutorServiceEntryPoint();
		executorServiceEntryPoint.init();
		System.out.println("Executor Service Started!");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				executorServiceEntryPoint.destroy();
			}
		});

		// executorServiceEntryPoint.scheduleRequest(CommandCodes.SendEmailCommand,
		// context);
		ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
		Runnable command = new Runnable() {
			
			public void run() {
				
				CommandContext context = new CommandContext();
				context.setData("to", "mdkimani@gmail.com");
				context.setData("from", "ebpm.mgr@gmail.com");
				context.setData("body", "Document above approved");
				context.setData("subject", "Inv/JKUAT/002/2013");
				context.setData("businessKey", UUID.randomUUID().toString());
				context.setData("callbacks", SendEmailCallback.name());
				
				try{
					DB.beginTransaction();
					executorServiceEntryPoint.scheduleRequest(
							CommandCodes.SendEmailCommand, context);
					DB.commitTransaction();
				}catch(Exception e){
					DB.rollback();
				}finally{
					DB.closeSession();
				}
				
			}
		};
		
		
		service.scheduleAtFixedRate(command, 3,25, TimeUnit.SECONDS);
	}
}
