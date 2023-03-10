package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApplicationConfig implements Serializable {
    private String version;
    private String profile;
    private AssetConfig assetConfig;
    private ScreenConfig screenConfig;
    private ConsoleConfig consoleConfig;
    private AudioConfig audioConfig;
    private Box2DConfig box2DConfig;
    private TiledMapConfig tiledMapConfig;
}
