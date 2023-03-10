package com.gdx.engine.interfaces.service;

import com.gdx.engine.console.ConsoleMsgLog;

import java.io.IOException;

public interface ConsoleService {
    void addMessage(ConsoleMsgLog message);
    void runCommands() throws IOException;
    void cmd(String cmd) throws IOException;
}
