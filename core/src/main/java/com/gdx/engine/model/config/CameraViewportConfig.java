package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class CameraViewportConfig implements Serializable {
    private float width;
    private float height;
}
