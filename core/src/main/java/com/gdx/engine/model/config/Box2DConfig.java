package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class Box2DConfig implements Serializable {
    private boolean enable;
    private boolean box2DDebugRenderer;
    private boolean doSleep;
    private Box2DWorldGravityConfig box2DWorldGravityConfig;
}
