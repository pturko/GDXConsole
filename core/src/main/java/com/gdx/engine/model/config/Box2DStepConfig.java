package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class Box2DStepConfig implements Serializable {
    private float timeStep;
    private int velocityIterations;
    private int positionIterations;
}
