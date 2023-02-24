package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class WindowConfig implements Serializable {
    private float width;
    private float height;
    private float ppm;
    private CameraConfig cameraConfig;
}
