/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.wira.pmgt.server.executor.commands;

import com.wira.pmgt.server.executor.api.Command;
import com.wira.pmgt.server.executor.api.CommandContext;
import com.wira.pmgt.server.executor.api.ExecutionResults;

/**
 *
 * @author salaboy
 */
public class PrintOutCommand implements Command{

    public ExecutionResults execute(CommandContext ctx) {
        System.out.println(">>> Hi This is the first command!");
        ExecutionResults executionResults = new ExecutionResults();
        return executionResults;
    }
    
}
