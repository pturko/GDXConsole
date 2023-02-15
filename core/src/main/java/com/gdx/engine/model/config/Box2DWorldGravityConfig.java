package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class Box2DWorldGravityConfig implements Serializable {
    private float x;
    private float y;
}
