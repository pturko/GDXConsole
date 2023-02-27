package com.gdx.engine.model.config;

import lombok.Data;

@Data
public class Box2DLightsConfig {
    private boolean rendering;
    private float ambientLight;
    private int numberOfRays;
    private int distance;
}
