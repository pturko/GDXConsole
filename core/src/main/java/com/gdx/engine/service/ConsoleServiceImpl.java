package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.gdx.engine.interfaces.service.ConsoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Slf4j
public class ConsoleServiceImpl implements ConsoleService {

    private static ConsoleServiceImpl consoleServiceInstance;
    private static WindowServiceImpl windowService;

    public static synchronized ConsoleServiceImpl getInstance( ) {
        if (consoleServiceInstance == null)
            consoleServiceInstance = new ConsoleServiceImpl();
        return consoleServiceInstance;
    }

    public ConsoleServiceImpl() {
    }

    public void cmd(String cmd) {
        String mainCmd;
        String secondCmd = StringUtils.EMPTY;
        String[] cmdSplitted = Arrays.stream(cmd.split(StringUtils.SPACE))
                .map(String::trim)
                .toArray(String[]::new);

        if (cmdSplitted.length == 0) {
            return;
        }

        mainCmd = cmdSplitted[0].toUpperCase();
        if (cmdSplitted.length > 1) {
            secondCmd = cmdSplitted[1];
        }

        windowService = WindowServiceImpl.getInstance();

        switch (mainCmd) {
            case "VER":
                log.info("version: {}", secondCmd);
                break;

            case "SCREEN":
                windowService.show(secondCmd.toUpperCase());
                log.info("set screen: {}", secondCmd);
                break;

            case "EXIT":
                Gdx.app.exit();
        }
    }

    public void runCommands() {
    }

    public void dispose() {}

}
