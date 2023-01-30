package com.gdx.engine.interfaces.service;

import com.gdx.engine.model.config.AudioConfig;
import com.gdx.engine.model.config.ConsoleConfig;
import com.gdx.engine.model.config.Profile;
import com.gdx.engine.model.config.WindowConfig;

public interface ConfigService {
    void updateConfigs();
    String getProfileString();
    String getVersion();
    Profile getProfile();
    WindowConfig getWindowConfig();
    ConsoleConfig getConsoleConfig();
    AudioConfig getAudioConfig();
}
