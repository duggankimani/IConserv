package com.wira.pmgt.server.executor.api;

/**
 *
 * @author salaboy
 */
public interface Executor extends Service {

    public Long scheduleRequest(CommandCodes commandName, CommandContext ctx);

    public void cancelRequest(Long requestId);

    public int getInterval();

    public void setInterval(int waitTime);

    public int getRetries();

    public void setRetries(int defaultNroOfRetries);

    public int getThreadPoolSize();

    public void setThreadPoolSize(int nroOfThreads);
}
