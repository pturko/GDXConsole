package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class Box2DConfig implements Serializable {
    private boolean rendering;
    private boolean enableContacts;
    private boolean box2DDebugRendering;
    private boolean staticSpriteRendering;
    private boolean animatedSpriteRendering;
    private boolean doSleep;
    private float ppm;
    private Box2DStepConfig box2DStepConfig;
    private Box2DWorldGravityConfig box2DWorldGravityConfig;
    private Box2DLightsConfig box2DLightsConfig;
}
