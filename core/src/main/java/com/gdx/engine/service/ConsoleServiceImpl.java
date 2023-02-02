package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.engine.console.ConsoleGdxLogAppender;
import com.gdx.engine.console.ConsoleMsgLog;
import com.gdx.engine.interfaces.service.ConsoleService;
import com.gdx.engine.model.config.ConsoleCmd;
import com.gdx.engine.util.FileLoaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class ConsoleServiceImpl implements ConsoleService {

    private static ConsoleServiceImpl consoleServiceInstance;
    private static ConfigServiceImpl configService;
    private static ResourceLoaderServiceImpl resourceService;
    private static WindowServiceImpl windowService;

    private static final int CONSOLE_MAX_MESSAGES = 25;

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
        configService = ConfigServiceImpl.getInstance();
        resourceService = ResourceLoaderServiceImpl.getInstance();

        switch (keyCmd) {
            case "VER":
            case "VERSION":
                log.info("Version: {}", configService.getVersion());
                break;

            case "SCREEN":
                windowService.show(partOneCmd.toUpperCase());
                log.info("Set screen: {}", partOneCmd);
                break;

            case "RESOURCES":
                if (partOneCmd.equalsIgnoreCase("load")) {
                    resourceService.loadResources();
                    log.info("Resources loaded");
                }
                break;

            case "CMD":
                if (partOneCmd.equalsIgnoreCase("profile")) {
                    runCommands();
                    log.info("Run commands for profile: {}", configService.getProfileString());
                }
                break;

            case "PROFILE":
                log.info("Profile: {}", configService.getProfileString());
                break;

            case "EXIT":
                Gdx.app.exit();
        }
    }

    public void runCommands() {
        configService = ConfigServiceImpl.getInstance();
        resourceService = ResourceLoaderServiceImpl.getInstance();
        if (configService.getConsoleConfig().isStartConsoleCmd()) {
            ConsoleCmd consoleCmd = FileLoaderUtil.getConsoleCmd(
                    resourceService.getConsoleCmdPathFile(configService.getProfileString()));
            for (String cmdScr : consoleCmd.getCmd()) {
                cmd(cmdScr);
            }
        }
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        int i = 1;
        List<ConsoleMsgLog> commandList = ConsoleGdxLogAppender.getCommandList();
        int maxCmdSize = (commandList.size() - 1) - CONSOLE_MAX_MESSAGES;
        if (maxCmdSize < 0) {
            maxCmdSize = 0;
        }
        for (int j = commandList.size() - 1; j >= maxCmdSize; j--) {
            ConsoleMsgLog cmd = commandList.get(j);
            font.setColor(cmd.getColor());
            font.draw(batch, cmd.getDate()
                    + " [" + cmd.getLogLevel() + "] " +
                    cmd.getMessage(), 5, 35 + (i * 17));
            i++;
        }
    }

    public void dispose() {}

}
