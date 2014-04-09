package com.wira.pmgt.server.executor.api;

import com.wira.pmgt.server.executor.callback.SendEmailCallback;
import com.wira.pmgt.server.executor.commands.PrintOutCommand;
import com.wira.pmgt.server.executor.commands.SendMailCommand;

public enum CommandCodes {
	
	PrintOutCmd(PrintOutCommand.class),
	SendEmailCommand(SendMailCommand.class),
	SendEmailCallback(SendEmailCallback.class);
	
	Class<?> handlerClass;
	
	private CommandCodes(Class<?> handler){
		this.handlerClass = handler;
	}

	public Class<?> getHandlerClass() {
		return handlerClass;
	}
}
