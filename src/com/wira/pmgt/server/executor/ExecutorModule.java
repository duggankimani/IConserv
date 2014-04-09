/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.pmgt.server.executor;


import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.server.executor.impl.ExecutorServiceEntryPointImpl;


/**
 *
 */
public class ExecutorModule {
    private static ExecutorModule instance;
    private ExecutorServiceEntryPoint executorService;
    
    public static ExecutorModule getInstance(){
        if(instance == null){
        	DBTrxProvider.init();
            instance = new ExecutorModule();
        }
        return instance;
    }

    private ExecutorModule() {
        executorService = new ExecutorServiceEntryPointImpl(); 
        executorService.init();
    }

	public ExecutorServiceEntryPoint getExecutorServiceEntryPoint() {
        return this.executorService;
    }

    
    public void dispose(){
        instance = null;
    }
    
    
}
