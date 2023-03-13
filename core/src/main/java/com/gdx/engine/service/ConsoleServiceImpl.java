package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.gdx.engine.console.ConsoleMsgLog;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.ConsoleEnabledEvent;
import com.gdx.engine.interfaces.service.ConsoleService;
import com.gdx.engine.model.config.ConsoleCmd;
import com.gdx.engine.state.AudioState;
import com.gdx.engine.screen.window.ConsoleWindow;
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

@Slf4j
public class ConsoleServiceImpl implements ConsoleService {
    private static ConsoleServiceImpl consoleServiceInstance;
    private static ConfigServiceImpl configService;
    private static AssetServiceImpl assetService;

    public ConsoleServiceImpl() {}

    public static synchronized ConsoleServiceImpl getInstance( ) {
        if (consoleServiceInstance == null)
            consoleServiceInstance = new ConsoleServiceImpl();
        return consoleServiceInstance;
    }

    public void cmd(String cmd) {
        configService = ServiceFactoryImpl.getConfigService();
        assetService = ServiceFactoryImpl.getAssetService();

        List<String> options = Arrays.stream(cmd.split(StringUtils.SPACE))
                .map(String::trim)
                .collect(Collectors.toList());

        if (options.size() == 0) {
            return;
        }

        Map<Integer, String> part = IntStream.range(0, options.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), options::get));

        switch (options.stream().findFirst().get().toUpperCase()) {
            case "ASSET":
                if (part.get(1).equalsIgnoreCase("load")) {
                    assetService.loadResources();
                }
                if (part.get(1).equalsIgnoreCase("reload")) {
                    assetService.loadResources();
                }
                break;

            case "APP":
                if (part.get(1).equalsIgnoreCase("exit")) {
                    Gdx.app.exit();
                }
                break;

            case "INFO":
                log.info("{}", part.get(1));
                break;

            case "VER":
            case "VERSION":
                log.info("Version: {}", configService.getVersion());
                break;

            case "SCREEN":
                ServiceFactoryImpl.getScreenService().show(part.get(1).toUpperCase(), part.get(2));
                break;

            case "CFG":
                if (part.get(1).equalsIgnoreCase("console")) {
                    if (part.get(2).equalsIgnoreCase("show")) {
                        configService.getConsoleConfig().setShowConsole(
                                OperationUtil.getBooleanValue(part.get(3), configService.getConsoleConfig().isShowConsole())
                        );
                        // Send console enabled event
                        if (configService.getConsoleConfig().isShowConsole()) {
                            ServiceFactoryImpl.getEventService().sendEvent(new ConsoleEnabledEvent());
                        }
                        log.info("display console: {}", configService.getConsoleConfig().isShowConsole());
                    }
                }
                if (part.get(1).equalsIgnoreCase("window")) {
                    if (part.get(2).equalsIgnoreCase("showFPS")) {
                        configService.getScreenConfig().getDebugConfig().setShowFPS(
                                OperationUtil.getBooleanValue(part.get(3), configService.getScreenConfig().getDebugConfig().isShowFPS())
                        );
                        log.info("display fps: {}", configService.getScreenConfig().getDebugConfig().isShowFPS());
                    }
                }
                if (part.get(1).equalsIgnoreCase("map")) {
                    if (part.get(2).equalsIgnoreCase("rendering")) {
                        configService.getTiledMapConfig().setRendering(
                                OperationUtil.getBooleanValue(part.get(3), configService.getTiledMapConfig().isRendering())
                        );
                        log.info("map rendering: {}", configService.getTiledMapConfig().isRendering());
                    }
                }
                if (part.get(1).equalsIgnoreCase("box2d")) {
                    if (part.get(2).equalsIgnoreCase("rendering")) {
                        configService.getBox2DConfig().setRendering(
                                OperationUtil.getBooleanValue(part.get(3), configService.getBox2DConfig().isRendering())
                        );
                        log.info("box2d rendering: {}", configService.getBox2DConfig().isRendering());
                    }
                    if (part.get(2).equalsIgnoreCase("sprite")) {
                        configService.getBox2DConfig().setStaticSpriteRendering(
                                OperationUtil.getBooleanValue(part.get(3), configService.getBox2DConfig().isStaticSpriteRendering())
                        );
                        log.info("box2d static sprite rendering: {}", configService.getBox2DConfig().isStaticSpriteRendering());
                    }
                }
                if (part.get(1).equalsIgnoreCase("audio")) {
                    if (part.get(2).equalsIgnoreCase("music")) {
                        configService.getAudioConfig().setMusic(
                                OperationUtil.getBooleanValue(part.get(3), configService.getAudioConfig().isMusic())
                        );
                        log.info("music: {}", configService.getAudioConfig().isMusic());
                    }
                    if (part.get(2).equalsIgnoreCase("sound")) {
                        configService.getAudioConfig().setSound(
                                OperationUtil.getBooleanValue(part.get(3), configService.getAudioConfig().isSound())
                        );
                        log.info("sound: {}", configService.getAudioConfig().isSound());
                    }
                }

                if (part.get(1).equalsIgnoreCase("update")) {
                    ServiceFactoryImpl.getConfigService().updateConfigs();
                }

                // Sending config changed events
                ServiceFactoryImpl.getEventService().sendEvent(new ConfigChangedEvent(configService.getApplicationConfig()));
                break;

            case "MUSIC":
                if (part.get(1).equalsIgnoreCase("play") && configService.getAudioConfig().isMusic()) {
                    ServiceFactoryImpl.getAudioService().music(AudioState.MUSIC_PLAY, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("playLoop") && configService.getAudioConfig().isMusic()) {
                    ServiceFactoryImpl.getAudioService().music(AudioState.MUSIC_PLAY_LOOP, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("stop")) {
                    ServiceFactoryImpl.getAudioService().music(AudioState.MUSIC_STOP, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("stopAll")) {
                    ServiceFactoryImpl.getAudioService().music(AudioState.MUSIC_STOP_ALL, part.get(2));
                }
                break;

            case "SOUND":
                if (part.get(1).equalsIgnoreCase("play") && configService.getAudioConfig().isSound()) {
                    ServiceFactoryImpl.getAudioService().sfx(AudioState.SOUND_PLAY, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("stop")) {
                    ServiceFactoryImpl.getAudioService().sfx(AudioState.SOUND_STOP, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("stopAll")) {
                    ServiceFactoryImpl.getAudioService().sfx(AudioState.SOUND_STOP_ALL, part.get(2));
                }
                break;

            case "CMD":
                if (part.get(1).equalsIgnoreCase("profile")) {
                    runProfileCommands();
                    log.info("Run commands for profile: {}", configService.getProfileString());
                }
                break;

            case "PROFILE":
                log.info("Profile: {}", configService.getProfileString());
                break;

            case "MAP":
                if (part.get(1).equalsIgnoreCase("load")) {
                    ServiceFactoryImpl.getTiledMapService().load(part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("reload")) {
                    TiledMapServiceImpl tiledMapService = ServiceFactoryImpl.getTiledMapService();
                    ServiceFactoryImpl.getTiledMapService().load(tiledMapService.getMapName());
                }
                if (part.get(1).equalsIgnoreCase("clear")) {
                    ServiceFactoryImpl.getTiledMapService().clear();
                }
                break;

            case "UI":
                if (part.get(1).equalsIgnoreCase("load")) {
                    ServiceFactoryImpl.getUIService().load();
                    log.info("UI loaded");
                }
                break;
        }
    }

    @Override
    public void addMessage(ConsoleMsgLog message) {
        ConsoleWindow.addMessage(message);
    }

    public void runProfileCommands() {
        configService = ServiceFactoryImpl.getConfigService();
        assetService = ServiceFactoryImpl.getAssetService();
        if (configService.getConsoleConfig().isStartCommands()) {
            ConsoleCmd consoleCmd = FileLoaderUtil.getConsoleCmd(
                    ServiceFactoryImpl.getAssetService().getConsoleCmdPathProfileFile(configService.getProfileString()));
            consoleCmd.getCmd().forEach(this::cmd);
        }
    }

    @Override
    public void runFileCommands(String fileName) {
            ConsoleCmd consoleCmd = FileLoaderUtil.getConsoleCmd(
                    ServiceFactoryImpl.getAssetService().getConsoleCmdPathFile(fileName));
            consoleCmd.getCmd().forEach(this::cmd);
    }

    public void dispose() {}

}
