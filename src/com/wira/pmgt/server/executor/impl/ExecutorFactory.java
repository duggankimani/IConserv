package com.wira.pmgt.server.executor.impl;

import com.wira.pmgt.server.executor.api.Executor;
import com.wira.pmgt.server.executor.api.ExecutorQueryService;
import com.wira.pmgt.server.executor.api.ExecutorRequestAdminService;

public class ExecutorFactory {

	private static Executor executor;
	private static ExecutorQueryService queryService;
	private static ExecutorRequestAdminService adminService;

	public static Executor getExecutor() {
		if (executor == null) {
			synchronized (ExecutorFactory.class) {
				if (executor == null) {
					executor = new ExecutorImpl();
					executor.init();
				}
			}
		}

		return executor;
	}

	public static ExecutorQueryService getExecutorQueryService() {
		if (queryService == null) {
			synchronized (ExecutorFactory.class) {
				if (queryService == null) {
					queryService = new ExecutorQueryServiceImpl();
				}
			}
		}

		return queryService;
	}

	public static ExecutorRequestAdminService getExecutorRequestAdminService() {
		if (adminService == null) {
			synchronized (ExecutorFactory.class) {
				if (adminService == null) {
					adminService = new ExecutorRequestAdminServiceImpl();
				}
			}
		}
		return adminService;
	}
	
	public static ExecutorRunnable getExecutorRunnable(){
		return new ExecutorRunnable();
	}
}
