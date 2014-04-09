/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.wira.pmgt.server.executor.api;

/**
 *
 * @author salaboy
 */
public interface Command {
    public ExecutionResults execute(CommandContext ctx) throws Exception;
}
