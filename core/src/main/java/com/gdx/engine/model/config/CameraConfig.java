package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class CameraConfig implements Serializable {
    private CameraPositionConfig cameraPositionConfig;
    private CameraViewportConfig cameraViewportConfig;
}
