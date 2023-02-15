package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.engine.console.ConsoleGdxLogAppender;
import com.gdx.engine.console.ConsoleMsgLog;
import com.gdx.engine.interfaces.service.ConsoleService;
import com.gdx.engine.model.config.ConsoleCmd;
import com.gdx.engine.state.AudioState;
import com.gdx.engine.util.FileLoaderUtil;
import com.gdx.engine.util.OperationUtil;
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
    private static TiledMapServiceImpl tiledMapService;

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
        String partTwoCmd = StringUtils.EMPTY;
        String partThreeCmd = StringUtils.EMPTY;
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
        if (cmdSplit.length > 2) {
            partTwoCmd = cmdSplit[2];
        }
        if (cmdSplit.length > 3) {
            partThreeCmd = cmdSplit[3];
        }

        windowService = WindowServiceImpl.getInstance();
        configService = ConfigServiceImpl.getInstance();
        resourceService = ResourceLoaderServiceImpl.getInstance();

        switch (keyCmd) {
            case "VER":
            case "VERSION":
                log.info("Version: {}", configService.getVersion());
                break;

            case "INFO":
                log.info("{}", partOneCmd);
                break;

            case "SCREEN":
                windowService.show(partOneCmd.toUpperCase(), partTwoCmd);
                break;

            case "RESOURCES":
                if (partOneCmd.equalsIgnoreCase("load")) {
                    resourceService.loadResources();
                    log.info("Resources loaded");
                }
                break;

            case "CONFIG":
                if (partOneCmd.equalsIgnoreCase("console")) {
                    if (partTwoCmd.equalsIgnoreCase("show")) {
                        configService.getConsoleConfig().setShowConsole(
                                OperationUtil.getBooleanValue(partThreeCmd, configService.getConsoleConfig().isShowConsole())
                        );
                        resetActiveScreen();
                        log.info("display console: {}", configService.getConsoleConfig().isShowConsole());
                    }
                }
                if (partOneCmd.equalsIgnoreCase("window")) {
                    if (partTwoCmd.equalsIgnoreCase("showFPS")) {
                        configService.getDebugConfig().setShowFPS(
                                OperationUtil.getBooleanValue(partThreeCmd, configService.getDebugConfig().isShowFPS())
                        );
                        resetActiveScreen();
                        log.info("display fps: {}", configService.getDebugConfig().isShowFPS());
                    }
                }
                if (partOneCmd.equalsIgnoreCase("audio")) {
                    if (partTwoCmd.equalsIgnoreCase("music")) {
                        configService.getAudioConfig().setMusic(
                                OperationUtil.getBooleanValue(partThreeCmd, configService.getAudioConfig().isMusic())
                        );
                        resetActiveScreen();
                        log.info("music: {}", configService.getAudioConfig().isMusic());
                    }
                    if (partTwoCmd.equalsIgnoreCase("sound")) {
                        configService.getAudioConfig().setSound(
                                OperationUtil.getBooleanValue(partThreeCmd, configService.getAudioConfig().isSound())
                        );
                        resetActiveScreen();
                        log.info("sound: {}", configService.getAudioConfig().isSound());
                    }
                }
                break;

            case "MUSIC":
                if (partOneCmd.equalsIgnoreCase("play") && configService.getAudioConfig().isMusic()) {
                    AudioServiceImpl.getInstance().music(AudioState.MUSIC_PLAY, partTwoCmd);
                }
                if (partOneCmd.equalsIgnoreCase("playLoop") && configService.getAudioConfig().isMusic()) {
                    AudioServiceImpl.getInstance().music(AudioState.MUSIC_PLAY_LOOP, partTwoCmd);
                }
                if (partOneCmd.equalsIgnoreCase("stop")) {
                    AudioServiceImpl.getInstance().music(AudioState.MUSIC_STOP, partTwoCmd);
                }
                if (partOneCmd.equalsIgnoreCase("stopAll")) {
                    AudioServiceImpl.getInstance().music(AudioState.MUSIC_STOP_ALL, partTwoCmd);
                }
                break;

            case "SOUND":
                if (partOneCmd.equalsIgnoreCase("play") && configService.getAudioConfig().isSound()) {
                    AudioServiceImpl.getInstance().sfx(AudioState.SOUND_PLAY, partTwoCmd);
                }
                if (partOneCmd.equalsIgnoreCase("stop")) {
                    AudioServiceImpl.getInstance().sfx(AudioState.SOUND_STOP, partTwoCmd);
                }
                if (partOneCmd.equalsIgnoreCase("stopAll")) {
                    AudioServiceImpl.getInstance().sfx(AudioState.SOUND_STOP_ALL, partTwoCmd);
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

            case "MAP":
                if (partOneCmd.equalsIgnoreCase("load")) {
                    tiledMapService = TiledMapServiceImpl.getInstance();
                    if (tiledMapService.load(partTwoCmd)) {
                        resetActiveScreen();
                        log.info("TiledMap '{}' successful loaded", partTwoCmd);
                    }
                }
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

    public void resetActiveScreen() {
        Screen activeScreen = WindowServiceImpl.getInstance().getActiveScreen();
        if (activeScreen != null) {
            activeScreen.resume();
        }
    }

    public void dispose() {}

}
