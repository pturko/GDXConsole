package com.gdx.engine.interfaces.service;

import java.io.IOException;

public interface ConsoleService {
    void runCommands() throws IOException;
    void cmd(String cmd) throws IOException;
}
