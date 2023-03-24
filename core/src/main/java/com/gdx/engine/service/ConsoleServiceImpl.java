package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.gdx.engine.console.ConsoleMsgLog;
import com.gdx.engine.event.*;
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

    public ConsoleServiceImpl() {}

    public static synchronized ConsoleServiceImpl getInstance( ) {
        if (consoleServiceInstance == null)
            consoleServiceInstance = new ConsoleServiceImpl();
        return consoleServiceInstance;
    }

    public void cmd(String cmd) {
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
                    ServiceFactoryImpl.getAssetService().loadResources();
                }
                if (part.get(1).equalsIgnoreCase("reload")) {
                    ServiceFactoryImpl.getAssetService().loadResources(); // TODO - should be reload existing one
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
                log.info("Version: {}", ServiceFactoryImpl.getConfigService().getVersion());
                break;

            case "SCREEN":
                ServiceFactoryImpl.getScreenService().show(part.get(1).toUpperCase(), part.get(2));
                break;

            case "CFG":
                ConfigServiceImpl configService = ServiceFactoryImpl.getConfigService();
                if (part.get(1).equalsIgnoreCase("console")) {
                    if (part.get(2).equalsIgnoreCase("show")) {

                        configService.getConsoleConfig().setShowConsole(
                                OperationUtil.getBooleanValue(part.get(3), configService.getConsoleConfig().isShowConsole())
                        );
                        // Sending console events event
                        ServiceFactoryImpl.getEventService().sendEvent(new ConfigConsoleChangedEvent());
                        if (configService.getConsoleConfig().isShowConsole()) {
                            ServiceFactoryImpl.getEventService().sendEvent(new ConsoleEnabledEvent());
                        }
                    }
                }
                if (part.get(1).equalsIgnoreCase("screen")) {
                    if (part.get(2).equalsIgnoreCase("fps")) {
                        configService.getScreenConfig().getDebugConfig().setFps(
                                OperationUtil.getBooleanValue(part.get(3), configService.getScreenConfig().getDebugConfig().isFps())
                        );
                        log.info("display fps: {}", configService.getScreenConfig().getDebugConfig().isFps());
                    }
                    if (part.get(2).equalsIgnoreCase("heap")) {
                        configService.getScreenConfig().getDebugConfig().setHeap(
                                OperationUtil.getBooleanValue(part.get(3), configService.getScreenConfig().getDebugConfig().isHeap())
                        );
                        log.info("display heap: {}", configService.getScreenConfig().getDebugConfig().isHeap());
                    }
                    ServiceFactoryImpl.getEventService().sendEvent(new ConfigScreenChangedEvent());
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
                    if (part.get(2).equalsIgnoreCase("anim")) {
                        configService.getBox2DConfig().setAnimatedSpriteRendering(
                                OperationUtil.getBooleanValue(part.get(3), configService.getBox2DConfig().isAnimatedSpriteRendering())
                        );
                        log.info("box2d animated sprite rendering: {}", configService.getBox2DConfig().isAnimatedSpriteRendering());
                    }
                    if (part.get(2).equalsIgnoreCase("debug")) {
                        configService.getBox2DConfig().setBox2DDebugRendering(
                                OperationUtil.getBooleanValue(part.get(3), configService.getBox2DConfig().isBox2DDebugRendering())
                        );
                        log.info("box2d debug rendering: {}", configService.getBox2DConfig().isBox2DDebugRendering());
                    }
                    if (part.get(2).equalsIgnoreCase("lights")) {
                        configService.getBox2DConfig().getBox2DLightsConfig().setRendering(
                                OperationUtil.getBooleanValue(part.get(3), configService.getBox2DConfig().getBox2DLightsConfig().isRendering())
                        );
                        log.info("box2d lights rendering: {}", configService.getBox2DConfig().getBox2DLightsConfig().isRendering());
                    }
                    ServiceFactoryImpl.getEventService().sendEvent(new ConfigBox2DChangedEvent());
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
                    ServiceFactoryImpl.getEventService().sendEvent(new ConfigAudioChangedEvent());
                }

                if (part.get(1).equalsIgnoreCase("update")) {
                    ServiceFactoryImpl.getConfigService().updateConfigs();
                }

                break;

            case "MUSIC":
                if (part.get(1).equalsIgnoreCase("play") &&
                        ServiceFactoryImpl.getConfigService().getAudioConfig().isMusic()) {
                    ServiceFactoryImpl.getAudioService().music(AudioState.MUSIC_PLAY, part.get(2));
                }
                if (part.get(1).equalsIgnoreCase("playLoop") &&
                        ServiceFactoryImpl.getConfigService().getAudioConfig().isMusic()) {
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
                if (part.get(1).equalsIgnoreCase("play") &&
                        ServiceFactoryImpl.getConfigService().getAudioConfig().isSound()) {
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
                    log.info("Run commands for profile: {}", ServiceFactoryImpl.getConfigService().getProfileString());
                }
                break;

            case "PROFILE":
                log.info("Profile: {}", ServiceFactoryImpl.getConfigService().getProfileString());
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

            case "CLR":
                ConsoleWindow.clearMsg();
                break;
        }
    }

    @Override
    public void addMessage(ConsoleMsgLog message) {
        ConsoleWindow.addMessage(message);
    }

    public void runProfileCommands() {
        ConfigServiceImpl configService = ServiceFactoryImpl.getConfigService();
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
