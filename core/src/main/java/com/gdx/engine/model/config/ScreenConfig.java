package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScreenConfig implements Serializable {
    private float width;
    private float height;
    private boolean fullScreen;
    private DebugConfig debugConfig;
    private CameraConfig cameraConfig;
}
