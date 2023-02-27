package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.engine.console.ConsoleGdxLogAppender;
import com.gdx.engine.console.ConsoleMsgLog;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.interfaces.service.ConsoleService;
import com.gdx.engine.model.config.ConsoleCmd;
import com.gdx.engine.state.AudioState;
import com.gdx.engine.util.FileLoaderUtil;
import com.gdx.engine.util.OperationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.gdx.engine.console.ConsoleGdxLogAppender.CONSOLE_MAX_MESSAGES;


@Slf4j
public class ConsoleServiceImpl implements ConsoleService {

    private static ConsoleServiceImpl consoleServiceInstance;
    private static ConfigServiceImpl configService;
    private static AssetServiceImpl assetService;
    private static ScreenServiceImpl screenService;
    private static TiledMapServiceImpl tiledMapService;
    private static EventServiceImpl eventService;

    public static synchronized ConsoleServiceImpl getInstance( ) {
        if (consoleServiceInstance == null)
            consoleServiceInstance = new ConsoleServiceImpl();
        return consoleServiceInstance;
    }

    public ConsoleServiceImpl() {
    }

    public void cmd(String cmd) {
        screenService = ScreenServiceImpl.getInstance();
        configService = ConfigServiceImpl.getInstance();
        assetService = AssetServiceImpl.getInstance();
        eventService = EventServiceImpl.getInstance();

        List<String> options = Arrays.stream(cmd.split(StringUtils.SPACE))
                .map(String::trim)
                .collect(Collectors.toList());

        if (options.size() == 0) {
            return;
        }

        Map<Integer, String> part = IntStream.range(0, options.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), i -> options.get(i)));

        switch (options.stream().findFirst().get().toUpperCase()) {
            case "APP":
                if (part.get(1).equalsIgnoreCase("reload")) {

                }
                if (part.get(1).equalsIgnoreCase("exit")) {
                    Gdx.app.exit();
                }
                break;

            case "VER":
            case "VERSION":
                log.info("Version: {}", configService.getVersion());
                break;

            case "INFO":
                log.info("{}", part.get(1));
                break;

            case "SCREEN":
                screenService.show(part.get(1).toUpperCase(), part.get(2));
                break;

            case "RESOURCES":
                if (part.get(1).equalsIgnoreCase("load")) {
                    assetService.loadResources();
                    log.info("Resources loaded");
                }
                break;

            case "CFG":
                if (part.get(1).equalsIgnoreCase("console")) {
                    if (part.get(2).equalsIgnoreCase("show")) {
                        configService.getConsoleConfig().setShowConsole(
                                OperationUtil.getBooleanValue(part.get(3), configService.getConsoleConfig().isShowConsole())
                        );
                        resetActiveScreen();
                        log.info("display console: {}", configService.getConsoleConfig().isShowConsole());
                    }
                }
                if (part.get(1).equalsIgnoreCase("window")) {
                    if (part.get(2).equalsIgnoreCase("showFPS")) {
                        configService.getScreenConfig().getDebugConfig().setShowFPS(
                                OperationUtil.getBooleanValue(part.get(3), configService.getScreenConfig().getDebugConfig().isShowFPS())
                        );
                        resetActiveScreen();
                        log.info("display fps: {}", configService.getScreenConfig().getDebugConfig().isShowFPS());
                    }
                }
                if (part.get(1).equalsIgnoreCase("map")) {
                    if (part.get(2).equalsIgnoreCase("rendering")) {
                        configService.getTiledMapConfig().setRendering(
                                OperationUtil.getBooleanValue(part.get(3), configService.getTiledMapConfig().isRendering())
                        );
                        resetActiveScreen();
                        log.info("map rendering: {}", configService.getTiledMapConfig().isRendering());
                    }
                }
                if (part.get(1).equalsIgnoreCase("box2d")) {
                    if (part.get(2).equalsIgnoreCase("rendering")) {
                        configService.getBox2DConfig().setRendering(
                                OperationUtil.getBooleanValue(part.get(3), configService.getBox2DConfig().isRendering())
                        );
                        resetActiveScreen();
                        log.info("box2d rendering: {}", configService.getBox2DConfig().isRendering());
                    }
                    if (part.get(2).equalsIgnoreCase("sprite")) {
                        configService.getBox2DConfig().setStaticSpriteRendering(
                                OperationUtil.getBooleanValue(part.get(3), configService.getBox2DConfig().isStaticSpriteRendering())
                        );
                        resetActiveScreen();
                        log.info("box2d static sprite rendering: {}", configService.getBox2DConfig().isStaticSpriteRendering());
                    }
                }
                if (part.get(1).equalsIgnoreCase("audio")) {
                    if (part.get(2).equalsIgnoreCase("music")) {
                        configService.getAudioConfig().setMusic(
                                OperationUtil.getBooleanValue(part.get(3), configService.getAudioConfig().isMusic())
                        );
                        resetActiveScreen();
                        log.info("music: {}", configService.getAudioConfig().isMusic());
                    }
                    if (part.get(2).equalsIgnoreCase("sound")) {
                        configService.getAudioConfig().setSound(
                                OperationUtil.getBooleanValue(part.get(3), configService.getAudioConfig().isSound())
                        );
                        resetActiveScreen();
                        log.info("sound: {}", configService.getAudioConfig().isSound());
                    }
                }

                if (part.get(1).equalsIgnoreCase("update")) {
                    configService.updateConfigs();
                    resetActiveScreen();
                }

                // Sending config changed events
                eventService.sendEvent(new ConfigChangedEvent(configService.getApplicationConfig()));

                break;

            case "MUSIC":
                if (part.get(1).equalsIgnoreCase("play") && configService.getAudioConfig().isMusic()) {
                    AudioServiceImpl.getInstance().music(AudioState.MUSIC_PLAY, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("playLoop") && configService.getAudioConfig().isMusic()) {
                    AudioServiceImpl.getInstance().music(AudioState.MUSIC_PLAY_LOOP, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("stop")) {
                    AudioServiceImpl.getInstance().music(AudioState.MUSIC_STOP, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("stopAll")) {
                    AudioServiceImpl.getInstance().music(AudioState.MUSIC_STOP_ALL, part.get(2));
                }
                break;

            case "SOUND":
                if (part.get(1).equalsIgnoreCase("play") && configService.getAudioConfig().isSound()) {
                    AudioServiceImpl.getInstance().sfx(AudioState.SOUND_PLAY, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("stop")) {
                    AudioServiceImpl.getInstance().sfx(AudioState.SOUND_STOP, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("stopAll")) {
                    AudioServiceImpl.getInstance().sfx(AudioState.SOUND_STOP_ALL, part.get(2));
                }
                break;

            case "CMD":
                if (part.get(1).equalsIgnoreCase("profile")) {
                    runCommands();
                    log.info("Run commands for profile: {}", configService.getProfileString());
                }
                break;

            case "PROFILE":
                log.info("Profile: {}", configService.getProfileString());
                break;

            case "MAP":
                if (part.get(1).equalsIgnoreCase("load")) {
                    tiledMapService = TiledMapServiceImpl.getInstance();
                    if (tiledMapService.load(part.get(2))) {
                        resetActiveScreen();
                        log.info("TiledMap '{}' loaded", part.get(2));
                    }
                }
                break;
        }
    }

    public void runCommands() {
        configService = ConfigServiceImpl.getInstance();
        assetService = AssetServiceImpl.getInstance();
        if (configService.getConsoleConfig().isStartCommands()) {
            ConsoleCmd consoleCmd = FileLoaderUtil.getConsoleCmd(
                    assetService.getConsoleCmdPathFile(configService.getProfileString()));
            for (String cmdScr : consoleCmd.getCmd()) {
                cmd(cmdScr);
            }
        }
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        // TODO - split message if too long
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
                    cmd.getMessage(), 8, 30 + (i * 17));
            i++;
        }
    }

    public void resetActiveScreen() {
        Screen activeScreen = ScreenServiceImpl.getInstance().getActiveScreen();
        if (activeScreen != null) {
            activeScreen.resume();
        }
    }

    public void dispose() {}

}
