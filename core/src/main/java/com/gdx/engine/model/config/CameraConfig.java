package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class CameraConfig implements Serializable {
    private float ppm;
    private CameraPositionConfig cameraPositionConfig;
    private CameraViewportConfig cameraViewportConfig;
}
