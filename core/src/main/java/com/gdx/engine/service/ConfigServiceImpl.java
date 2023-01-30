package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gdx.engine.interfaces.service.ConfigService;
import com.gdx.engine.model.config.*;
import com.gdx.engine.util.FileLoaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@Slf4j
public class ConfigServiceImpl implements ConfigService {

    private static ConfigServiceImpl configServiceInstance;
    private static ApplicationConfig applicationConfig;
    private static WindowServiceImpl windowService;
    private static ConsoleServiceImpl consoleService;

    private final String CONFIG_FILE= "asset/config/application.json"; //TODO - fix hardcode filename

    private String version;
    private Profile profile;
    private WindowConfig window;
    private ConsoleConfig console;
    private AudioConfig audio;

    public static synchronized ConfigServiceImpl getInstance( ) {
        if (configServiceInstance == null) {
            configServiceInstance = new ConfigServiceImpl();
        }
        return configServiceInstance;
    }

    ConfigServiceImpl() {
        windowService = WindowServiceImpl.getInstance();
        consoleService = ConsoleServiceImpl.getInstance();
    }

    @Override
    public void updateConfigs() {
        try {
            FileHandle fileHandle = Gdx.files.internal(CONFIG_FILE); //TODO - should be configurable external/internal
            applicationConfig = FileLoaderUtil.getApplicationConfig(fileHandle);
            update(applicationConfig);
        } catch (IOException e) {
            log.error("Can't update application configs");
        }
    }

    public void update(ApplicationConfig applicationConfig) {
        this.version = applicationConfig.getVersion();
        this.profile = setProfile(applicationConfig.getProfile());
        this.window = applicationConfig.getWindowConfig();
        this.console = applicationConfig.getConsoleConfig();
        this.audio = applicationConfig.getAudioConfig();
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
    public WindowConfig getWindowConfig() {
        return window;
    }

    @Override
    public ConsoleConfig getConsoleConfig() {
        return console;
    }

    @Override
    public AudioConfig getAudioConfig() {
        return audio;
    }

    public Profile setProfile(String profile) {
        Profile profileState = Profile.DEFAULT;

        if (null == profile || profile.equals(StringUtils.EMPTY)) {
            log.error("Profile couldn't be empty. Setting to DEFAULT");
            return profileState;
        }

        try {
            profileState = Profile.valueOf(profile.toUpperCase());
        } catch (Exception e) {
            log.error("Profile {} couldn't found. Setting to DEFAULT", profile.toUpperCase());
        }

        return profileState;
    }

    @Override
    public String getProfileString() {
        return profile.name().toUpperCase();
    }

}
