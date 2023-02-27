package com.gdx.engine.interfaces.service;

import com.gdx.engine.model.config.*;

public interface ConfigService {
    void updateConfigs();
    String getProfileString();
    String getVersion();
    Profile getProfile();
    AssetConfig getAssetConfig();
    ScreenConfig getScreenConfig();
    ConsoleConfig getConsoleConfig();
    AudioConfig getAudioConfig();
    Box2DConfig getBox2DConfig();
    TiledMapConfig getTiledMapConfig();
    ApplicationConfig getApplicationConfig();
}
