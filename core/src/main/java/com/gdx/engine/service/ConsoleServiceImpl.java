package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.gdx.engine.interfaces.service.ConsoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Slf4j
public class ConsoleServiceImpl implements ConsoleService {

    private static ConsoleServiceImpl consoleServiceInstance;
    private static ResourceLoaderServiceImpl resourceLoaderService;
    private static WindowServiceImpl windowService;

    public static synchronized ConsoleServiceImpl getInstance( ) {
        if (consoleServiceInstance == null)
            consoleServiceInstance = new ConsoleServiceImpl();
        return consoleServiceInstance;
    }

    public ConsoleServiceImpl() {
    }

    public void cmd(String cmd) {
        String keyCmd;
        String partOneCmd = StringUtils.EMPTY;
        String[] cmdSplit = Arrays.stream(cmd.split(StringUtils.SPACE))
                .map(String::trim)
                .toArray(String[]::new);

        if (cmdSplit.length == 0) {
            return;
        }

        keyCmd = cmdSplit[0].toUpperCase();
        if (cmdSplit.length > 1) {
            partOneCmd = cmdSplit[1];
        }

        windowService = WindowServiceImpl.getInstance();

        switch (keyCmd) {
            case "VER":
            case "VERSION":
                log.info("Version: {}", partOneCmd);
                break;

            case "SCREEN":
                windowService.show(partOneCmd.toUpperCase());
                log.info("Set screen: {}", partOneCmd);
                break;

            case "RESOURCES":
                if (partOneCmd.equalsIgnoreCase("load")) {
                    resourceLoaderService = ResourceLoaderServiceImpl.getInstance();
                    resourceLoaderService.loadResources();
                    log.info("Resources loaded");
                }
                break;

            case "EXIT":
                Gdx.app.exit();
        }
    }

    public void runCommands() {
    }

    public void dispose() {}

}
