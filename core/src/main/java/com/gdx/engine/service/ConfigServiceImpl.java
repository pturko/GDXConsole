package com.gdx.engine.service;

import com.gdx.engine.event.*;
import com.gdx.engine.interfaces.service.ConfigService;
import com.gdx.engine.model.config.*;
import com.gdx.engine.util.FileLoaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import static com.gdx.engine.service.AssetServiceImpl.EXTERNAL_APPLICATION_CONFIG;

@Slf4j
public class ConfigServiceImpl implements ConfigService {
    private static ConfigServiceImpl configServiceInstance;
    private static ApplicationConfig applicationConfig;

    //TODO - fix hardcoded filename
    private final String CONFIG_FILE= "asset/config/application.json";

    private static String version;
    private static Profile profile;
    private static AssetConfig assetConfig;
    private static ScreenConfig screenConfig;
    private static ConsoleConfig consoleConfig;
    private static AudioConfig audioConfig;
    private static Box2DConfig box2DConfig;
    private static TiledMapConfig tiledMapConfig;

    public static synchronized ConfigServiceImpl getInstance( ) {
        if (configServiceInstance == null) {
            configServiceInstance = new ConfigServiceImpl();
        }
        return configServiceInstance;
    }

    ConfigServiceImpl() {
    }

    @Override
    public void updateConfigs() {
        try {
            applicationConfig = FileLoaderUtil.getApplicationConfig(CONFIG_FILE);
            update(applicationConfig);
            log.info("Config reloaded!");
            if (!EXTERNAL_APPLICATION_CONFIG) {
                log.warn("EXTERNAL_APPLICATION_CONFIG should be TRUE");
            }
            updateGlobalConfig();
        } catch (IOException e) {
            log.error("Can't update application configs!");
        }
    }

    public void update(ApplicationConfig applicationConfig) {
        version = applicationConfig.getVersion();
        profile = setProfile(applicationConfig.getProfile());
        assetConfig = applicationConfig.getAssetConfig();
        screenConfig = applicationConfig.getScreenConfig();
        consoleConfig = applicationConfig.getConsoleConfig();
        audioConfig = applicationConfig.getAudioConfig();
        box2DConfig = applicationConfig.getBox2DConfig();
        tiledMapConfig = applicationConfig.getTiledMapConfig();
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
    public AssetConfig getAssetConfig() {
        return assetConfig;
    }

    @Override
    public ScreenConfig getScreenConfig() {
        return screenConfig;
    }

    @Override
    public ConsoleConfig getConsoleConfig() {
        return consoleConfig;
    }

    @Override
    public AudioConfig getAudioConfig() {
        return audioConfig;
    }

    @Override
    public Box2DConfig getBox2DConfig() {
        return box2DConfig;
    }

    @Override
    public TiledMapConfig getTiledMapConfig() {
        return tiledMapConfig;
    }

    @Override
    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public Profile setProfile(String profile) {
        Profile profileState = Profile.DEFAULT;

        if (null == profile || profile.equals(StringUtils.EMPTY)) {
            log.warn("Profile couldn't be empty! Set to DEFAULT");
            return profileState;
        }

        try {
            profileState = Profile.valueOf(profile.toUpperCase());
        } catch (Exception e) {
            log.warn("Profile '{}' couldn't found! Set to DEFAULT", profile.toUpperCase());
        }

        return profileState;
    }

    @Override
    public String getProfileString() {
        return profile.name().toUpperCase();
    }

    private static void updateGlobalConfig() {
        // Sending config changed event for all components
        ServiceFactoryImpl.getEventService().sendEvent(new ConfigAssetChangedEvent());
        ServiceFactoryImpl.getEventService().sendEvent(new ConfigAudioChangedEvent());
        ServiceFactoryImpl.getEventService().sendEvent(new ConfigBox2DChangedEvent());
        ServiceFactoryImpl.getEventService().sendEvent(new ConfigConsoleChangedEvent());
        ServiceFactoryImpl.getEventService().sendEvent(new ConfigMapChangedEvent());
        ServiceFactoryImpl.getEventService().sendEvent(new ConfigScreenChangedEvent());
    }
}
