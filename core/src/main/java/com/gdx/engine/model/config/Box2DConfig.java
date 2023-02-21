package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class Box2DConfig implements Serializable {
    private boolean rendering;
    private boolean enableContacts;
    private boolean box2DDebugRenderer;
    private boolean staticSpriteRenderer;
    private boolean animatedSpriteRenderer;
    private boolean doSleep;
    private Box2DStepConfig box2DStepConfig;
    private Box2DWorldGravityConfig box2DWorldGravityConfig;
}
