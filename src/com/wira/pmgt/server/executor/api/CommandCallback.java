package com.wira.pmgt.server.executor.api;

public interface CommandCallback {

    void onCommandDone(CommandContext ctx, ExecutionResults results);
}
